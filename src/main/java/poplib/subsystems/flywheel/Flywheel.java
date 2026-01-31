package poplib.subsystems.flywheel;

import poplib.control.PIDConfig;
import poplib.smart_dashboard.PIDTuning;
import poplib.smart_dashboard.TunableNumber;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class Flywheel extends SubsystemBase {
    protected final TunableNumber setpoint;
    protected PIDTuning leadPidTuning;
    protected double gearRatio;
 
    protected Flywheel(String subsystemName, boolean tuningMode, double gearRatio) {
        super(subsystemName);
        this.setpoint = new TunableNumber(subsystemName + " flywheel setpoint", 0, tuningMode);
        this.gearRatio = gearRatio;
        this.leadPidTuning = new PIDTuning(subsystemName + " flywheel", PIDConfig.getZeroPid(), tuningMode);
    } 

    @Override
    public abstract void periodic();

    public abstract double getError(double setpoint);

    public double convertWithRatio(double input) {
        return input * gearRatio;
    }
 
    public void updateSetpoint(double setpoint) {
        this.setpoint.setDefault(convertWithRatio(setpoint));
    }

    public Command updateSetpointCommand(double setpoint) {
        return runOnce(() -> this.setpoint.setDefault(convertWithRatio(setpoint)));
    }

    public Command updateSetpointCommand(double setpoint, double maxError) {
        return run(() -> { 
            this.setpoint.setDefault(convertWithRatio(maxError));
        }).until(() -> getError(convertWithRatio(setpoint)) < maxError);
    }
}
