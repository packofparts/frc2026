package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import poplib.subsystems.pivot.SparkPivot;

public class Turret extends SparkPivot{
    public static Turret instance;

    public static Turret getInstance() {
        if (instance == null) {
            instance = new Turret();
        }
        return instance;
    }

    private Turret () {
        super(Constants.Turret.ROT_CONFIG, 
        Constants.Turret.GEAR_RATIO,
        Constants.Turret.FF_CONFIG, 
        Constants.Turret.ABSOLUTE_CONFIG, 
        false, 
        "Turret"); //Check gear ratio
    }

    public Command turnTurret(double position, double error) {
        return moveWrist(position % 360, error);
    }
}