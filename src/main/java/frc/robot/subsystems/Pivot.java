package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import frc.robot.Constants;
import poplib.subsystems.pivot.TalonPivot;


public class Pivot extends TalonPivot {
    
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
            Constants.Pivot.GEAR_RATIO, 
            Constants.Pivot.FF, 
            Constants.Pivot.TUNING_MODE,
            "Pivot"
        );
    }

    @Override
    public void periodic() {
        super.periodic();
    }

    public void setLaunchAngle(double angle) {
        angle = MathUtil.clamp(angle, Constants.Pivot.MIN_ANGLE, Constants.Pivot.MAX_ANGLE);
        double actualAngle = angle - Constants.Pivot.MIN_ANGLE; 
        moveWrist(actualAngle, 0.1);

    }
}

