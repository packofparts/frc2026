package frc.robot.subsystems;

import frc.robot.Constants;
import poplib.subsystems.flywheel.SparkFlywheel;

public class Flywheel extends SparkFlywheel {
    
    // Ignore this
    private static Flywheel instance;
    public static Flywheel getInstance() {
        if (instance == null) {
            instance = new Flywheel();
        }
        return instance;
    }
    
    public Flywheel() {
        super(
            Constants.Flywheel.LEAD_CONFIG,
            Constants.Flywheel.FOLLOWER_CONFIG,
            Constants.Flywheel.SUBSYSTEM_NAME,
            Constants.Flywheel.TUNING_MODE
        );
    }

    @Override
    public void periodic() {
        super.periodic();
    }

}
