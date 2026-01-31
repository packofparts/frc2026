package poplib.subsystems.pivot;

import poplib.control.FFConfig;
import poplib.sensors.absolute_encoder.AbsoluteEncoder;
import poplib.sensors.absolute_encoder.AbsoluteEncoderConfig;
import poplib.smart_dashboard.TunableNumber;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class Pivot extends SubsystemBase {
    protected final AbsoluteEncoder absoluteEncoder;
    protected final ArmFeedforward ff;
    protected final TunableNumber setpoint;
    protected final double gearRatio;

    public Pivot(FFConfig ffConfig, AbsoluteEncoderConfig absoluteConfig, double gearRatio, boolean tuningMode, String subsystemName) {
        super(subsystemName);

        absoluteEncoder = absoluteConfig.getDutyCycleEncoder();
        ff = ffConfig.getArmFeedforward();
        this.gearRatio = gearRatio;
        setpoint = new TunableNumber("Pivot Setpoint " + subsystemName, 0, tuningMode);
    }

    @Override
    public abstract void periodic();

    public double convertWithRatio(double input) {
        return (input / 360) * gearRatio;
    }

    public double backToDegrees(double input) {
        return input * 360 * (1/gearRatio);
    }

    public Command moveWrist(double position, double error) {
        return run(() -> {
            setpoint.setDefault(convertWithRatio(position));
        }).until(() -> atSetpoint(error, convertWithRatio(position)));
    }

    public Command moveWristBy(double value, double error) {
        return moveWrist(setpoint.get() + convertWithRatio(value), error);
    }
    
    public abstract boolean atSetpoint(double error, double setpoint);

    public abstract void resetToAbsolutePosition();

    public double getAbsolutePosition() {
        return absoluteEncoder.getDegreeNormalizedPosition();
    }

    public void log() {
        SmartDashboard.putNumber("Absolute Position " + getName(), getAbsolutePosition()); 
    }
}
