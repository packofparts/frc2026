package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Meters;

import java.util.ArrayList;
import java.util.Optional;

import frc.robot.Constants;
import frc.robot.Constants.AutoAlign.CLIMB_MOVEMENT_SP;
import poplib.sensors.camera.Camera;
import poplib.sensors.camera.CameraConfig;
import poplib.sensors.camera.LimelightConfig;
import poplib.sensors.gyro.Pigeon;
import poplib.swerve.swerve_modules.SwerveModuleTalon;
import poplib.swerve.swerve_templates.VisionBaseSwerve;
import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Swerve extends VisionBaseSwerve{
    private PIDController x_pid; 
    private PIDController y_pid; 
    private PIDController alignRot_pid;
    private Camera climbCam;
    private Optional<Transform3d> cameraOutputBuffer;
    private int climbTagToUse;
    private Pose2d goalPoseForAuto;

    private static Swerve instance;

    public static Swerve getInstance() {
        if (instance == null) {
            instance = new Swerve();
        }
        return instance;
    }
    
    public Swerve() {
        super (
            new SwerveModuleTalon[] {
                new SwerveModuleTalon(Constants.Swerve.SWERVE_MODULE_CONSTANTS[0]),
                new SwerveModuleTalon(Constants.Swerve.SWERVE_MODULE_CONSTANTS[1]),
                new SwerveModuleTalon(Constants.Swerve.SWERVE_MODULE_CONSTANTS[2]),
                new SwerveModuleTalon(Constants.Swerve.SWERVE_MODULE_CONSTANTS[3]),
            },
            new Pigeon(Constants.Swerve.PIGEON_ID, Constants.Swerve.GYRO_INVERSION, Constants.swerveLoop),
            Constants.Swerve.SWERVE_KINEMATICS, new ArrayList<CameraConfig>(), new ArrayList<LimelightConfig>()
        );

        AutoBuilder.configure(
            super::getOdomPose, // Robot pose supplier
            super::setOdomPose, // Method to reset odometry (will be called if your auto has a starting pose)
            super::getChassisSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
            (speeds, feedforwards) -> driveChassis(speeds), // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds. Also optionally outputs individual module feedforwards
            Constants.Autos.pathFollower, // PPLTVController is the built in path following controller for differential drive trains
            Constants.Autos.getConfig(), // The robot configuration
            () ->  {
              // Boolean supplier that controls when the path will be mirrored for the red alliance
              // This will flip the path being followed to the red side of the field.
              // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

              var alliance = DriverStation.getAlliance();
              if (alliance.isPresent()) {
                return alliance.get() == DriverStation.Alliance.Red;
              }
              return false;
            },
            this
        );

        // climbCam = new Camera(Constants.AutoAlign.alignConfig);
        x_pid = new PIDController(3, 0, 0);
        y_pid = new PIDController(3, 0, 0);
        alignRot_pid = new PIDController(0.1, 0, 0);
        x_pid.setTolerance(0.1);
        y_pid.setTolerance(0.1);
        alignRot_pid.setTolerance(0.1);
        cameraOutputBuffer = Optional.empty();
        climbTagToUse = 15; // default red side
        goalPoseForAuto = new Pose2d();
    }


    public Command moveSwerveUsingPID(double x, double y, double rot) {
        return runOnce(() -> {x_pid.setSetpoint(field.getRobotPose().getX()+x); y_pid.setSetpoint(field.getRobotPose().getY()+y); alignRot_pid.setSetpoint(field.getRobotPose().getRotation().getDegrees() + rot);}).
        andThen(run(() -> {driveRobotOriented(new Translation2d(x_pid.calculate(field.getRobotPose().getX()), 
                                                         y_pid.calculate(field.getRobotPose().getY())), 
                                                         alignRot_pid.calculate(field.getRobotPose().getRotation().getDegrees()));})).
        until(() -> {return x_pid.atSetpoint() && y_pid.atSetpoint() && alignRot_pid.atSetpoint();}).
        andThen(() -> {driveRobotOriented(new Translation2d(0, 0), 0); x_pid.reset(); y_pid.reset(); alignRot_pid.reset();});
    }

    public Command autoSwerve(double y) {
        return run(() -> {driveRobotOriented(new Translation2d(0, y), 0);System.out.println("haha");}).
        raceWith(new WaitCommand(1.25)).  //1.2 good place to stop
        andThen(runOnce(() -> {driveRobotOriented(new Translation2d(0, 0), 0);System.out.println("hihihi");}));
    }

    public Command autoAlign(CLIMB_MOVEMENT_SP sp) {
        return 
        runOnce(() -> climbTagToUse = sp.getTag()).andThen(
        (run(() -> driveRobotOriented(new Translation2d(0,0), -getRotAlignPID(sp))).            
        until(() -> boolCheckForRot(sp)).
        andThen(run(() -> driveRobotOriented(new Translation2d(0, getYAlignPID(sp)), 0)).
        until(() -> boolCheckForY(sp)))).
        raceWith(new WaitCommand(10)));
    }
    
    public double getRotAlignPID(CLIMB_MOVEMENT_SP SP) {
        double rotation = field.getRobotPose().getRotation().getDegrees();
        return alignRot_pid.calculate(rotation, SP.getRot());
    }

    public Boolean boolCheckForRot(CLIMB_MOVEMENT_SP SP) {
        double rotation = field.getRobotPose().getRotation().getDegrees();
        if (Math.abs(rotation - SP.getRot()) < 3) {
            return true;
        } else {
            return false;
        }
    }

    public double getYAlignPID(CLIMB_MOVEMENT_SP SP) {
        double y = cameraOutputBuffer.get().getY();
        return -y_pid.calculate(y, SP.getY());
    }

    public Boolean boolCheckForY(CLIMB_MOVEMENT_SP SP) {
        double y = cameraOutputBuffer.get().getY();
        if (Math.abs(y - SP.getY()) < 0.005) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void periodic() {
        super.periodic();
        // var rawCameraOutput = climbCam.getCameraDiffs(climbTagToUse);
        // if (rawCameraOutput.isPresent()) {
        //     cameraOutputBuffer = rawCameraOutput;
        // }
    }
}