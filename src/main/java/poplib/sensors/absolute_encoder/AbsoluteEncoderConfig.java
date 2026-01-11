package poplib.sensors.absolute_encoder;

import com.ctre.phoenix6.CANBus;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.Units;
import poplib.motor.ConversionConfig;

public class AbsoluteEncoderConfig {
    public final int id;
    public final CANBus bus;
    public final Rotation2d offset;
    public final boolean inversion;
    public final ConversionConfig conversionFactor;

    public AbsoluteEncoderConfig(int id, CANBus bus, Rotation2d offset, boolean inversion, ConversionConfig conversionFactor) {
        this.id = id;
        this.bus = bus;
        this.offset = offset;
        this.inversion = inversion;
        this.conversionFactor = conversionFactor;
    }


    public AbsoluteEncoderConfig(int id, CANBus bus, Rotation2d offset, boolean inversion) {
        this(id, bus, offset, inversion, new ConversionConfig(1.0, Units.Rotations));
    }


    public CANCoder getCANCoder() {
        return new CANCoder(this);
    }

    public DutyCycleAbsoluteEncoder getDutyCycleEncoder() {
        return new DutyCycleAbsoluteEncoder(this);
    }
}
