package poplib.swerve.commands;

import poplib.controllers.ControllerMath;
import poplib.controllers.io.IO;
import poplib.swerve.swerve_templates.BaseSwerve;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import java.util.function.Supplier;

/**
 * Command that controls teleop swerve.
 */
public class TeleopSwerveDrive extends Command {
    private final BaseSwerve swerve;

    private final Supplier<Double> xAxisSupplier;
    private final Supplier<Double> yAxisSupplier;
    private final Supplier<Double> rotSupplier;
    private final Supplier<Double> speedMultiplier;
    private final double stickDeadBand;

    /**
     * Pass in default speed multiplier of 1.0
     */
    public TeleopSwerveDrive(BaseSwerve swerve,
            Supplier<Double> xAxisSupplier,
            Supplier<Double> yAxisSupplier,
            Supplier<Double> rotSupplier,
            double stickDeadBand) {
        this(
            swerve,
            xAxisSupplier,
            yAxisSupplier,
            rotSupplier,
            () -> 1.0,
            stickDeadBand
        );
    }

    public TeleopSwerveDrive(BaseSwerve swerve, IO oi) {
        this(
            swerve,
            oi::getDriveTrainTranslationX,
            oi::getDriveTrainTranslationY,
            oi::getDriveTrainRotation,
            () -> 1.0,
            IO.DEADBAND
        );
    }

    public TeleopSwerveDrive(BaseSwerve swerve, IO oi, Supplier<Double> rotSupplier) {
        this(
            swerve,
            oi::getDriveTrainTranslationX,
            oi::getDriveTrainTranslationY,
            oi::getDriveTrainRotation,
            rotSupplier,
            IO.DEADBAND
        );
    }

    /**
     * Set swerve subsytem, controllers, axes, and other swerve parameters.
     */
    public TeleopSwerveDrive(BaseSwerve swerve,
            Supplier<Double> xAxisSupplier,
            Supplier<Double> yAxisSupplier,
            Supplier<Double> rotSupplier,
            Supplier<Double> speedMultiplier,
            double stickDeadBand) {
        this.swerve = swerve;

        this.xAxisSupplier = xAxisSupplier;
        this.yAxisSupplier = yAxisSupplier;
        this.rotSupplier = rotSupplier;
        this.speedMultiplier = speedMultiplier;
        this.stickDeadBand = stickDeadBand;

        addRequirements(swerve);
    }

    @Override
    public void execute() {
        double forwardBack = yAxisSupplier.get() * speedMultiplier.get();
        double leftRight = xAxisSupplier.get() * speedMultiplier.get();
        double rot = rotSupplier.get() * speedMultiplier.get();

        forwardBack = ControllerMath.applyDeadband(forwardBack, stickDeadBand);
        leftRight = ControllerMath.applyDeadband(leftRight, stickDeadBand);

        Translation2d translation = new Translation2d(forwardBack, leftRight);

        swerve.drive(
            translation,
            ControllerMath.cube(rot),
            DriverStation.getAlliance().get()
        );
    }
}