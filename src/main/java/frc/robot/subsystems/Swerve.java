package frc.robot.subsystems;

import java.util.ArrayList;

import frc.robot.Constants;
import poplib.sensors.camera.CameraConfig;
import poplib.sensors.camera.LimelightConfig;
import poplib.sensors.gyro.Pigeon;
import poplib.swerve.swerve_modules.SwerveModuleTalon;
import poplib.swerve.swerve_templates.VisionBaseSwerve;
import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Swerve extends VisionBaseSwerve{
    
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

    }

    public Command runSwerve(double x, double y, double rot) {
        //return new ParallelRaceGroup(run(() -> super.driveRobotOriented(new Translation2d(x,y), rot)), new WaitCommand(5));
        return run(() -> super.driveRobotOriented(new Translation2d(x,y), rot)).raceWith(new WaitCommand(5));
    }


    @Override
    public void periodic() {
        super.periodic();
    }
}
