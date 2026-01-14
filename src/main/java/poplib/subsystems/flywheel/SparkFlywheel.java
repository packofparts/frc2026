package poplib.subsystems.flywheel;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkMax;
import poplib.math.MathUtil;
import poplib.motor.FollowerConfig;
import poplib.motor.MotorConfig;
import poplib.smart_dashboard.PIDTuning;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SparkFlywheel extends Flywheel {
    SparkMax leadMotor; 
    SparkMax followerMotor; 
    PIDTuning leadPidTuning;
 
    protected SparkFlywheel(MotorConfig leadConfig, FollowerConfig followerConfig, String subsystemName, boolean tuningMode) {
        super(subsystemName, tuningMode);

        this.leadMotor = leadConfig.createSparkMax();
        this.followerMotor = followerConfig.createSparkMax();
    }

    @Override
    public double getError(double setpoint) {
        return MathUtil.getError(leadMotor, setpoint);
    }

    public void log() {
        SmartDashboard.putNumber(getName() + " velocity ", leadMotor.getEncoder().getVelocity());
    }

    @Override
    public void periodic() {
        leadPidTuning.updatePID(leadMotor);
        leadMotor.getClosedLoopController().setSetpoint(setpoint.get(), ControlType.kVelocity);
    }
}
