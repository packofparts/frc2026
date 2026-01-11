package poplib.sensors.camera;

import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Transform3d;

public class CameraConfig {
    public final String cameraName;
    public final Transform3d cameraToRobot;
    public final double poseAmbiguityThreshold;
    public final double poseDistanceThreshold;
    public final StdDevStrategy stdDevStrategy;
    public final AprilTagFields aprilTagField;

    public CameraConfig(String cameraName, Transform3d cameraToRobot, double poseAmbiguityThreshold, 
                        double poseDistanceThreshold, StdDevStrategy stdDevStrategy, AprilTagFields thisYearsField) {
        this.cameraName = cameraName;
        this.cameraToRobot = cameraToRobot;
        this.poseAmbiguityThreshold = poseAmbiguityThreshold; 
        this.poseDistanceThreshold = poseDistanceThreshold;
        this.stdDevStrategy = stdDevStrategy;
        this.aprilTagField = thisYearsField;
    }
}
