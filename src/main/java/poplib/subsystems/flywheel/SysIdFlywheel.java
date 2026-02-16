package poplib.subsystems.flywheel;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public class SysIdFlywheel {
   SysIdRoutine routine;
   
   public SysIdFlywheel(TalonFlywheel flywheel) {
        routine = new SysIdRoutine(
            new SysIdRoutine.Config(),
            new SysIdRoutine.Mechanism(flywheel::runSysIdRoutine, flywheel::logSysId, flywheel, "Flywheel")
        );
   }

    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return routine.quasistatic(direction);
    }

    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return routine.dynamic(direction);
    }
}
