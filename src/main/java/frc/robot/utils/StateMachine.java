package frc.robot.utils;

public class StateMachine {
    private static StateMachine instance;
    public TurretState turret;
    public ClimbState climb;
    public boolean enableAutoAim;    
    public boolean intakeOnly;


    public static StateMachine getInstance() {
        if (instance == null) {
            instance = new StateMachine();
        }
        return instance;
    }

    private StateMachine() {
        turret = TurretState.NONE;
        climb = ClimbState.IDLE;
        enableAutoAim = false;
        intakeOnly = false;
    }
}
