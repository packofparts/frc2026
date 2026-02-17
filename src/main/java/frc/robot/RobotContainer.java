// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.AutoAlign.CLIMB_MOVEMENT_SP;
import frc.robot.Constants.Climb.CLIMB_SETPOINT;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.Turret;
import frc.robot.utils.ClimbState;
import frc.robot.utils.StateMachine;
import poplib.controllers.io.XboxIO;
import poplib.swerve.commands.TeleopSwerveDrive;



public class RobotContainer {
    XboxIO controller = XboxIO.getInstance();

    Flywheel flywheel = Flywheel.getInstance();
    Swerve swerve = Swerve.getInstance();
    Indexer indexer = Indexer.getInstance();
    Turret turret = Turret.getInstance();
    Pivot pivot = Pivot.getInstance();
    Climb climb = Climb.getInstance();
    Intake intake = Intake.getInstance();
    private final SendableChooser<Command> autoChooser;



    public RobotContainer() {
        autoChooser = AutoBuilder.buildAutoChooser();    

        swerve.setDefaultCommand(new TeleopSwerveDrive(swerve, controller));
        NamedCommands.registerCommand("Extend To L1", extendToL1());
        NamedCommands.registerCommand("Retract To Idle", extendToIdle());
        NamedCommands.registerCommand("Shoot Fuel", shootFuel());
        NamedCommands.registerCommand("Collect Fuel", collectFuel());
        NamedCommands.registerCommand("Zero Pivot", pivot.reZero());
        NamedCommands.registerCommand("Zero Turret", turret.reZero());

        SmartDashboard.putData("Auto Chooser", autoChooser);

        configureBindings();
    }

    private void configureBindings() {
        controller.getOperatorTrigger(XboxController.Axis.kRightTrigger.value).onTrue(flywheel.updateSetpointCommand(1000)).onFalse(flywheel.updateSetpointCommand(0));
        controller.getOperatorTrigger(XboxController.Axis.kLeftTrigger.value).onTrue(flywheel.updateSetpointCommand(-1000)).onFalse(flywheel.updateSetpointCommand(0));

        controller.getOperatorTrigger(XboxController.Axis.kLeftX.value).onTrue(pivot.moveWristBy(-controller.getRawAxis(XboxController.Axis.kLeftX.value, controller.getOperatorController()), 0.1));
        controller.getOperatorTrigger(XboxController.Axis.kRightY.value).onTrue(turret.turnTurretBy(controller.getRawAxis(XboxController.Axis.kRightY.value, controller.getOperatorController()), 0.1));

        controller.getOperatorButton(XboxController.Button.kX.value).onTrue(indexer.runHandoff()).onFalse(indexer.stopHandoff());
        controller.getOperatorButton(XboxController.Button.kB.value).onTrue(indexer.reverseHandoff()).onFalse(indexer.stopHandoff());    

        controller.getOperatorButton(XboxController.Button.kRightBumper.value).onTrue(intake.runIntake()).onFalse(intake.stopIntake());
        controller.getOperatorButton(XboxController.Button.kLeftBumper.value).onTrue(intake.reverseIntake()).onFalse(intake.stopIntake()); 
           
        controller.getDriverController().povUp().onTrue(toggleToL1());
        controller.getDriverButton(XboxController.Button.kStart.value).onTrue(swerve.resetGyroCommand());

        controller.getDriverController().povLeft().onTrue(alignClimbLeft());
        controller.getDriverController().povRight().onTrue(alignClimbRight());
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

    public Command shootFuel() {
        // An example command will be run in autonomous
        return null;
    }
    public Command collectFuel() {
        // An example command will be run in autonomous
        return null;
    }

    public Command toggleToL1() {
        return StateMachine.getInstance().climb == ClimbState.IDLE ? extendToL1() : extendToIdle();
    }

    public Command extendToL1() {
        return climb.climbTo(CLIMB_SETPOINT.L1)
        .andThen(() -> {StateMachine.getInstance().climb = ClimbState.EXTENDED_TO_L1;});
    }

    public Command extendToIdle() {
        return climb.climbTo(CLIMB_SETPOINT.L1)
        .andThen(() -> {StateMachine.getInstance().climb = ClimbState.EXTENDED_TO_L1;});
    }

    public Command alignClimbLeft() {
        if (DriverStation.getAlliance().isPresent()) {
            if (DriverStation.getAlliance().get() == Alliance.Blue) {
                return swerve.autoAlign(CLIMB_MOVEMENT_SP.BLUE_LEFT);
            } else {
                return swerve.autoAlign(CLIMB_MOVEMENT_SP.RED_LEFT);
            }
        } else {
            return new WaitCommand(1).
            andThen(() -> DriverStation.reportWarning("CANNOT GET THE ALLIANCE, AUTOALIGN WILL NOT WORK", false));        
        }
    }

    public Command alignClimbRight() {
        if (DriverStation.getAlliance().isPresent()) {
            if (DriverStation.getAlliance().get() == Alliance.Blue) {
                return swerve.autoAlign(CLIMB_MOVEMENT_SP.BLUE_RIGHT);
            } else {
                return swerve.autoAlign(CLIMB_MOVEMENT_SP.RED_RIGHT);
            }
        } else {
            return new WaitCommand(1).
            andThen(() -> DriverStation.reportWarning("CANNOT GET THE ALLIANCE, AUTOALIGN WILL NOT WORK", false));
        }
    }
}
