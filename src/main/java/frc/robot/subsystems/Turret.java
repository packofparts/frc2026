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

    /**
     * Rotates the turret into a certain position
     * @param position
     * @param error
     * @return the Command that rotates the turret into a certain position
     */
    public Command turnTurret(double position, double error) {
        return moveWrist((position % 360) < 0 ? 360+(position % 360) : (position % 360), error);
    }

    /**
     * Rotates turret by certain degree
     * @param offset
     * @param error
     * @return the Command that rotates turret by certain degree
     */
    public Command turnTurretBy(double offset, double error) {
        return turnTurret(backToDegrees(setpoint.get()) + offset, error);
    }
}