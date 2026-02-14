// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import poplib.control.FFConfig;
import poplib.control.PIDConfig;
import poplib.motor.ConversionConfig;
import poplib.motor.FollowerConfig;
import poplib.motor.Mode;
import poplib.motor.MotorConfig;
import poplib.sensors.camera.CameraConfig;
import poplib.sensors.camera.StdDevStrategy;
import poplib.swerve.swerve_constants.SDSModules;
import poplib.swerve.swerve_constants.SwerveModuleConstants;



public final class Constants {

    public static final String swerveLoop = "cantBUS";
    public static final String manipulatorLoop = "cantNotBus"; 

    public static class Swerve {
        static final SDSModules MODULE_TYPE = SDSModules.MK4iL3;
        static final boolean TUNING_MODE = false;
        static final int SWERVE_CAN_ID_OFFSET = 5;      

        static final MotorConfig DRIVE_CONFIG = new MotorConfig(swerveLoop, 80, false, PIDConfig.getPid(0.01, 0.2), Mode.BRAKE);
        static final MotorConfig ANGLE_CONFIG = new MotorConfig(swerveLoop, 25, false, PIDConfig.getPid(5.0), Mode.BRAKE);
        
        public static final SwerveModuleConstants[] SWERVE_MODULE_CONSTANTS = SwerveModuleConstants.generateConstants(
            new Rotation2d[] {
                Rotation2d.fromDegrees(134.2),              // set offsets
                Rotation2d.fromDegrees(32.4),           // set offsets
                Rotation2d.fromDegrees(226.4),             // set offsets
                Rotation2d.fromDegrees(348.8)              //  set offsets
            },
            MODULE_TYPE, 
            TUNING_MODE, 
            DRIVE_CONFIG, 
            ANGLE_CONFIG,
            SWERVE_CAN_ID_OFFSET
        );

        public static final int PIGEON_ID = 19;
        public static final boolean GYRO_INVERSION = false;      // change if needed - gyro should be ccw+ and cw-

        public static final double WHEEL_BASE =  edu.wpi.first.math.util.Units.inchesToMeters(23);
        public static final double TRACK_WIDTH = edu.wpi.first.math.util.Units.inchesToMeters(23); 
        public static final SwerveDriveKinematics SWERVE_KINEMATICS = new SwerveDriveKinematics(
            new Translation2d(WHEEL_BASE / 2.0, TRACK_WIDTH / 2.0),
            new Translation2d(WHEEL_BASE / 2.0, -TRACK_WIDTH / 2.0),
            new Translation2d(-WHEEL_BASE / 2.0, -TRACK_WIDTH / 2.0),
            new Translation2d(-WHEEL_BASE / 2.0, TRACK_WIDTH / 2.0)
        );
    }

    public static class Intake {
        public static final MotorConfig MOTOR_CONFIG = new MotorConfig(20, manipulatorLoop, 25, false, Mode.COAST);
        public static final FollowerConfig FOLLOWER_CONFIG = new FollowerConfig(MOTOR_CONFIG, false, 21);
        public static final double SPEED = 0.65; 
    }

    public static class Indexer {
        public static final MotorConfig MOTOR_CONFIG = new MotorConfig(30, manipulatorLoop, 25, false, Mode.COAST);
        public static final MotorConfig SPINDEXER_CONFIG = new MotorConfig(31, manipulatorLoop, 25, false, Mode.BRAKE);
        public static final int BEAMBREAK_ID = 3;
        public static final double SPEED = 0.65; // adjust as necessary
    }

    public static class Turret {
        public static final MotorConfig ROT_CONFIG = new MotorConfig(40, manipulatorLoop, 25, false, PIDConfig.getPid(0.1, 0, 0, 0), Mode.BRAKE);
        public static final int LIMIT_SWITCH_ID = 4;
        public static final int GEAR_RATIO = 1; //check
        public static final FFConfig FF_CONFIG = new FFConfig(0, 0, 0);
    }

    public static class Pivot {
        public static final boolean TUNING_MODE = false;
        public static final MotorConfig PIVOT_MOTOR = new MotorConfig(41, manipulatorLoop, 40, false, new PIDConfig(0.1, 0, 0), Mode.BRAKE, new ConversionConfig());
        public static final int LIMIT_SWITCH_ID = 5;
        public static final double GEAR_RATIO = 1.0; // TODO: update with real gear ratio
        public static final FFConfig FF = new FFConfig(0);  // TODO: tune feedforward values
    }

    public static class Flywheel {
        public static final boolean TUNING_MODE = true;
        public static final MotorConfig leadConfig = new MotorConfig(42, "rio", 40, false, new PIDConfig(0.1, 0, 0), Mode.BRAKE, new ConversionConfig());
    }

    public static class Climb {
        public static final boolean TUNING_MODE = false;
        public enum CLIMB_SETPOINT {
            IDLE(0),
            L1(20),
            L2(35),
            L3(50);
            private double climb;

            private CLIMB_SETPOINT(double climb) {
            this.climb = climb;
            }
            public double getHi() {
                return climb;
            }
        }
        public static final MotorConfig MOTOR_CONFIG = new MotorConfig(50, manipulatorLoop, 40, false, new PIDConfig(0.1, 0, 0), Mode.BRAKE);
    }


    public static class Autos {
        public static PPHolonomicDriveController pathFollower = new PPHolonomicDriveController(
            new PIDConstants(5, 0, 0), 
            new PIDConstants(5, 0, 0)
        );
        public static RobotConfig getConfig() {
            RobotConfig config = null;
            try {
                config = RobotConfig.fromGUISettings();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return config;
        } 
    }

    public static class AutoAim {
        public static final CameraConfig turretConfig = new CameraConfig("turretCamera", new Transform3d(), 0, 0, StdDevStrategy.AMBIGUITY, AprilTagFields.k2026RebuiltWelded);
    }

    public static class AutoAlign {
        public static final CameraConfig climbConfig = new CameraConfig("climbCamera", new Transform3d(), 0, 0, StdDevStrategy.AMBIGUITY, AprilTagFields.k2026RebuiltWelded);
        public enum CLIMB_MOVEMENT_SP {
            RED_RIGHT(0,0),
            RED_LEFT(0,0),
            BLUE_RIGHT(0,0),
            BLUE_LEFT(0,0);
            private double y;
            private double rot;

            private CLIMB_MOVEMENT_SP(double rot, double y) {
            this.rot = rot;
            this.y = y;
            }

            public double getY() {
                return y;
            }

            public double getRot() {
                return rot;
            }
        }
    }
}