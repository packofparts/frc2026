package frc.robot.subsystems;

import frc.robot.Constants;
import poplib.subsystems.flywheel.TalonFlywheel;

public class Flywheel extends TalonFlywheel {
    
    private static Flywheel instance;
    public static Flywheel getInstance() {
        if (instance == null) {
            instance = new Flywheel();
        }
        return instance;
    }
    
    public Flywheel() {
        super( 
            Constants.Flywheel.leadConfig, 
            "Flywheel", 
            Constants.Flywheel.TUNING_MODE,
            1.0);
    }

    @Override
    public void periodic() {
        super.periodic();
    }

}
