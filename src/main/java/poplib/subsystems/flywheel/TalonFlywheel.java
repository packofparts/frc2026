package poplib.subsystems.flywheel;

import com.ctre.phoenix6.controls.CoastOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import poplib.motor.MotorConfig;
import poplib.swerve.swerve_modules.SwerveModule;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.sysid.SysIdRoutineLog;

public class TalonFlywheel extends Flywheel {
    public TalonFX leadMotor; 
    public TalonFX followerMotor; 

    private VelocityDutyCycle velocity;
    private VoltageOut voltageOut;
    private CoastOut idleControl;
    
    protected TalonFlywheel(MotorConfig leadConfig, MotorConfig followerConfig, String subsystemName, boolean tuningMode, boolean motorsInverted, double gearRatio) {
        super(subsystemName, tuningMode, gearRatio);

        this.leadMotor = leadConfig.createTalon();
        this.followerMotor = followerConfig.createTalon();

        this.velocity = new VelocityDutyCycle(0.0);
        this.idleControl = new CoastOut();
        this.voltageOut = new VoltageOut(0);

        followerMotor.setControl(new Follower(leadConfig.canId, motorsInverted ? MotorAlignmentValue.Opposed : MotorAlignmentValue.Aligned));
    } 

    protected TalonFlywheel(MotorConfig leadConfig, String subsystemName, boolean tuningMode, double gearRatio) {
        super(subsystemName, tuningMode, gearRatio);

        this.leadMotor = leadConfig.createTalon();
        this.followerMotor = null;
        this.voltageOut = new VoltageOut(0);

        this.velocity = new VelocityDutyCycle(0.0);
        this.idleControl = new CoastOut();
    } 

    @Override
    public double getError(double setpoint) {
        return Math.abs(leadMotor.getVelocity().getValueAsDouble() - setpoint);
    }

    public void log() {
        SmartDashboard.putNumber(getName() + " velocity ", leadMotor.getVelocity().getValueAsDouble());
    }

    public double getVelocity() {
        return leadMotor.getVelocity().getValueAsDouble();
    }

    @Override
    public void periodic() {
        leadPidTuning.updatePID(leadMotor);
        log();
        if (setpoint.hasChanged()) {
            leadMotor.setControl(setpoint.get() != 0 ? velocity.withVelocity(setpoint.get()).withFeedForward() : idleControl);
        }
     } 

    public void runSysIdRoutine(Voltage voltage) {
        leadMotor.setControl(voltageOut.withOutput(voltage));
    }

    public void logSysId(SysIdRoutineLog log) {
        log.motor("Flywheel")
            .angularPosition(leadMotor.getPosition().getValue())
            .angularVelocity(leadMotor.getVelocity().getValue())
            .voltage(leadMotor.getMotorVoltage().getValue());
    }
}
