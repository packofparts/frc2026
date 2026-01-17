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

    public Pivot(FFConfig ffConfig, AbsoluteEncoderConfig absoluteConfig, boolean tuningMode, String subsystemName) {
        super(subsystemName);

        absoluteEncoder = absoluteConfig.getDutyCycleEncoder();
        ff = ffConfig.getArmFeedforward();

        setpoint = new TunableNumber("Pivot Setpoint " + subsystemName, 0, tuningMode);
    }

    @Override
    public abstract void periodic();


    public Command moveWrist(double position, double error) {
        return run(() -> {
            setpoint.setDefault(position);
        }).until(() -> atSetpoint(error, position));
    }

    public Command moveWristBy(double value, double error) {
        return moveWrist(setpoint.get() + value, error);
    }
    
    public abstract boolean atSetpoint(double error, double setpoint);

    public abstract void resetToAbsolutePosition();

    public double getAbsolutePosition() {
        return absoluteEncoder.getDegreeNormalizedPosition();
    }

    public void log() {
        SmartDashboard.putNumber("Absolute Position " + getName(), getAbsolutePosition()); 
    }

    public static Pivot getInstance() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInstance'");
    }
}
