package poplib.controllers.io;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class JoysticksIO extends IO {
    public static JoysticksIO instance;

    private CommandJoystick driveJoystick;
    private CommandJoystick transJoystick;
    private CommandXboxController operatorController;
    
    public static JoysticksIO getInstance() {
        if (instance == null) {
            instance = new JoysticksIO();
        }

        return instance;
    } 

    public static final class OIConstants  {
        public static final int DRIVE_PORT = 0;
        public static final int TRANS_PORT = 1;
        public static final int OPERATOR_PORT = 2;
    }

    public JoysticksIO() {
        driveJoystick = new CommandJoystick(OIConstants.DRIVE_PORT);
        transJoystick = new CommandJoystick(OIConstants.TRANS_PORT);
        operatorController = new CommandXboxController(OIConstants.OPERATOR_PORT);
    }

    @Override
    public double getDriveTrainRotation() {
        return transJoystick.getX();
    }

    @Override
    public double getDriveTrainTranslationY() {
        return driveJoystick.getY();
    }

    @Override
    public double getDriveTrainTranslationX() {
        return driveJoystick.getX();
        
    }

    @Override
    public CommandGenericHID getDriverController() {
        return driveJoystick;
    }

    @Override
    public CommandGenericHID getOperatorController() {
        return operatorController;
    }
}
