package frc.robot.subsystems;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.CANdi;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import poplib.subsystems.pivot.TalonPivot;


public class Pivot extends TalonPivot {
    public static CANdi limitSwitchFR;
    private static Pivot instance;
    public static Pivot getInstance() {
        if (instance == null) {
            instance = new Pivot();
        }
        return instance;
    }
    
    public Pivot(){
        super(
            Constants.Pivot.PIVOT_MOTOR, 
            Constants.Pivot.LIMIT_SWITCH_ID,
            1, 
            Constants.Pivot.FF, 
            Constants.Pivot.TUNING_MODE,
            "Pivot"
        );
        limitSwitchFR = new CANdi(Constants.Pivot.CANDI_ID, new CANBus("rio"));
        super.leadMotor.setPosition(0);
    }

    @Override
    public boolean isLimitSwitchPressed() {
        return limitSwitchFR.getS1Closed().getValue() || limitSwitchFR.getS2Closed().getValue();
    }

    @Override
    public void periodic() {
        super.periodic();
        SmartDashboard.putBoolean("Pivot Limit", isLimitSwitchPressed());
    }

    public void setLaunchAngle(double angle) {
        angle = MathUtil.clamp(angle, Constants.Pivot.MIN_ANGLE, Constants.Pivot.MAX_ANGLE);
        double actualAngle = angle - Constants.Pivot.MIN_ANGLE; 
        moveWrist(actualAngle, 0.1);
    }
}

