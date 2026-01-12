// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import poplib.control.PIDConfig;
import com.ctre.phoenix6.CANBus;
import poplib.control.FFConfig;
import poplib.motor.ConversionConfig;
import poplib.motor.FollowerConfig;
import poplib.motor.Mode;
import poplib.motor.MotorConfig;
import poplib.sensors.absolute_encoder.AbsoluteEncoderConfig;
import poplib.swerve.swerve_constants.SDSModules;
import poplib.swerve.swerve_constants.SwerveModuleConstants;

public final class Constants {

    public static final String CANIVORE_NAME = "cantBUS";

    public static class Swerve {
        static final SDSModules MODULE_TYPE = SDSModules.MK4iL3;
        static final boolean TUNING_MODE = false;
        static final int SWERVE_CAN_ID_OFFSET = 5;      

        static final MotorConfig DRIVE_CONFIG = new MotorConfig(CANIVORE_NAME, 80, false, PIDConfig.getPid(0.01, 0.2), Mode.BRAKE);
        static final MotorConfig ANGLE_CONFIG = new MotorConfig(CANIVORE_NAME, 25, false, PIDConfig.getPid(5.0), Mode.BRAKE);
        
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

        public static final int PIGEON_ID = 20;
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

    public static class Pivot {
        /** 
         * TODO: Define MotorConfig for the SparkMax (Neo) Motor
         * Motor Information: Can Id = 20
         * Canbus = Constants.CANBUS
         * Current Limit = 40
         * Brake Mode
         * PIDConfig of 0.1, 0, 0
         * No inversion 
         */ 
        public static final MotorConfig LEAD_CONFIG = new MotorConfig(
            20,
            CANIVORE_NAME,
            40,
            false,
            PIDConfig.getPid(0.1, 0, 0),
            Mode.BRAKE,
            new ConversionConfig()
        );

        public static final double GEAR_RATIO = 1.0;
        public static final FFConfig FF_CONFIG = new FFConfig(0.0);
        public static final AbsoluteEncoderConfig ABSOLUTE_ENCODER_CONFIG =
            new AbsoluteEncoderConfig(0, new CANBus(CANIVORE_NAME), Rotation2d.fromDegrees(0), false);

        public static final boolean TUNING_MODE = false;
        public static final String SUBSYSTEM_NAME = "Pivot";
    }

    public static class Flywheel {
        /** 
         * TODO: Define MotorConfigs for the SparkMax (Neo) Motors
         * Motor Information: 
         * Can Id = 21
         * Canbus = Constants.CANBUS
         * Current Limit = 40
         * Brake Mode
         * PIDConfig of 0.1, 0, 0
         * No inversion 
         * 
         * Follower Information:
         * Can Id = 22
         * No inversion
         */ 
        public static final MotorConfig LEAD_CONFIG = new MotorConfig(
            21,
            CANIVORE_NAME,
            40,
            false,
            PIDConfig.getPid(0.1, 0, 0),
            Mode.BRAKE,
            new ConversionConfig()
        );

        public static final FollowerConfig FOLLOWER_CONFIG = new FollowerConfig(LEAD_CONFIG, false, 22);

        public static final boolean TUNING_MODE = false;
        public static final String SUBSYSTEM_NAME = "Flywheel";
    }

    public static class Turret {
        // ignore for now
    }
}
