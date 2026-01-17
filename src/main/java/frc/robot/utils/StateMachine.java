package frc.robot.utils;

public class StateMachine {
    private static StateMachine instance;
    public TurretState turret;
    public IntakeState intake;

    public static StateMachine getInstance() {
        if (instance == null) {
            instance = new StateMachine();
        }
        return instance;
    }

    private StateMachine() {
        turret = TurretState.NONE;
        intake = IntakeState.UP;
    }
}
