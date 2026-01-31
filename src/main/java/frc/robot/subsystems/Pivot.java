package frc.robot.subsystems;

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
            Constants.Pivot.GEAR_RATIO, 
            Constants.Pivot.FF, 
            Constants.Pivot.absoluteConfig, 
            Constants.Pivot.TUNING_MODE,
            "Pivot"
        );
    }
    

    @Override
    public void periodic() {
        super.periodic();
    }
}

