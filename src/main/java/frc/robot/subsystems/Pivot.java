package frc.robot.subsystems;

import poplib.subsystems.pivot.SparkPivot;
import frc.robot.Constants;

public class Pivot extends SparkPivot {
    
    // Ignore this
    private static Pivot instance;
    public static Pivot getInstance() {
        if (instance == null) {
            instance = new Pivot();
        }
        return instance;
    }
    
    public Pivot() {
        super(
            Constants.Pivot.LEAD_CONFIG,
            Constants.Pivot.GEAR_RATIO,
            Constants.Pivot.FF_CONFIG,
            Constants.Pivot.ABSOLUTE_ENCODER_CONFIG,
            Constants.Pivot.TUNING_MODE,
            Constants.Pivot.SUBSYSTEM_NAME
        );
    }

    @Override
    public void periodic() {
        super.periodic();
    }
}
