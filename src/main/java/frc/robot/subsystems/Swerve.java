package frc.robot.subsystems;

import java.util.ArrayList;

import frc.robot.Constants;
import frc.robot.utils.StateMachine;
import frc.robot.utils.TurretState;
import poplib.sensors.camera.Camera;
import poplib.sensors.camera.CameraConfig;
import poplib.sensors.camera.LimelightConfig;
import poplib.sensors.gyro.Pigeon;
import poplib.swerve.swerve_modules.SwerveModuleTalon;
import poplib.swerve.swerve_templates.VisionBaseSwerve;
import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Swerve extends VisionBaseSwerve{
    
    private double hub_x = 4.02 + 0.5969;
    private double hub_Y = 4.03;

    private static Swerve instance;
    private Camera turretCam;
    private Field2d tester;
    private PIDController rot_pid;

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

        turretCam = new Camera(Constants.AutoAim.turretConfig);
        tester = new Field2d();
        rot_pid = new PIDController(0.15, 0, 0);

    }

    @Override
    public void driveRobotOriented(Translation2d vector, double rot) {
        var multiTagPose = turretCam.getMultiTagCamPos();
        if (multiTagPose.isPresent()) {
            double x = multiTagPose.get().getX();
            double y = multiTagPose.get().getY();
            double rotation = field.getRobotPose().getRotation().getDegrees();
            tester.setRobotPose(x, y, multiTagPose.get().getRotation().toRotation2d());
            double x_diff = hub_x - x;
            double y_diff = hub_Y - y;
            double theta = Math.toDegrees(Math.atan(y_diff/x_diff));
            SmartDashboard.putNumber("theta wanted", theta);
            if (StateMachine.getInstance().turret == TurretState.HUB) {
                rot = -rot_pid.calculate(rotation, theta);
            }
        }
        SwerveModuleState[] states = super.kinematics.toSwerveModuleStates(new ChassisSpeeds(vector.getX(), vector.getY(), rot));
        this.driveRobotOriented(states);
    }

    @Override
    public void periodic() {
        super.periodic();
        // var thingy = turretCam.getCameraDiffs();
        // if (thingy.isPresent()) {
        //     SmartDashboard.putNumber("cam x", thingy.get().getX());
        //     SmartDashboard.putNumber("cam x", thingy.get().getY());
        // }

        

        SmartDashboard.putData("test field", tester);
        var hi = turretCam.getMultiTagCamPos();
        this.odom.addVisionMeasurement(prevPose, MAX_SKID_ACCEL);

        // var thingy = turretCam.getCameraRotOffset();
        // if (thingy.isPresent()) {
        //     SmartDashboard.putNumber("cam yaw", thingy.get().doubleValue());
        // }
    }
}
