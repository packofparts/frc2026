package poplib.subsystems.pivot;

import poplib.control.FFConfig;
import poplib.smart_dashboard.TunableNumber;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class Pivot extends SubsystemBase {
    protected final ArmFeedforward ff;
    protected final TunableNumber setpoint;
    protected final double gearRatio;

    public Pivot(FFConfig ffConfig, double gearRatio, boolean tuningMode, String subsystemName) {
        super(subsystemName);
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

    public void log() {
        SmartDashboard.putNumber(getName() + " SP", setpoint.get()); 
    }
}
