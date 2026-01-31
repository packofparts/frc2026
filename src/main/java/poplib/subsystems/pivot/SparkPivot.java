package poplib.subsystems.pivot;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkMax;
import poplib.control.FFConfig;
import poplib.motor.MotorConfig;
import poplib.smart_dashboard.PIDTuning;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SparkPivot extends Pivot {
    public final SparkMax leadMotor;
    private final PIDTuning pid;

    public SparkPivot(MotorConfig leadConfig, double gearRatio, FFConfig ffConfig, boolean tuningMode, String subsystemName) {
        super(ffConfig, gearRatio, tuningMode, subsystemName);
        leadMotor = leadConfig.createSparkMax();

        pid = leadConfig.genPIDTuning("Pivot Motor " + subsystemName, tuningMode);
    }

    @Override
    public boolean atSetpoint(double error, double setpoint) {
        return getError(setpoint) < error;
    }

    public double getError(double setpoint) {
        return Math.abs(leadMotor.getEncoder().getPosition() - setpoint);
    }

    @Override
    public void log() {
        super.log();
        SmartDashboard.putNumber("Lead Position " + getName(), leadMotor.getEncoder().getPosition());
    }

    @Override
    public void periodic() {
        pid.updatePID(leadMotor);

        // TODO: Move to mutable units
        leadMotor.getClosedLoopController().setSetpoint(
            setpoint.get(), 
            ControlType.kPosition,
            ClosedLoopSlot.kSlot0,
            ff.calculate(leadMotor.getEncoder().getPosition(), 0)
        );
    }
}