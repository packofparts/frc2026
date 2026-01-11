package poplib.swerve.swerve_templates;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.photonvision.EstimatedRobotPose;

import poplib.sensors.camera.Camera;
import poplib.sensors.camera.CameraConfig;
import poplib.sensors.camera.DetectedObject;
import poplib.sensors.camera.Limelight;
import poplib.sensors.camera.LimelightConfig;
import poplib.sensors.gyro.Gyro;
import poplib.swerve.swerve_modules.SwerveModule;

public abstract class VisionBaseSwerve extends BaseSwerve {
    protected final SwerveDrivePoseEstimator odom;
    private final SwerveDriveKinematics kinematics;
    public ArrayList<Camera> cameras;
    private ArrayList<Limelight> limelights;

    public VisionBaseSwerve(SwerveModule[] swerveMods, Gyro gyro, SwerveDriveKinematics kinematics,
                            Matrix<N3, N1> stateStdDevs, Matrix<N3, N1> visionMeasurementStdDevs, 
                            List<CameraConfig> cameraConfigs, List<LimelightConfig> limelightConfigs) {
        super(swerveMods, gyro);
        this.kinematics = kinematics;

        this.odom = new SwerveDrivePoseEstimator(kinematics, this.getGyro().getNormalizedRotation2dAngle(),
                                                this.getPose(), new Pose2d(0.0, 0.0, this.getGyro().
                                                getNormalizedRotation2dAngle()), stateStdDevs, visionMeasurementStdDevs);
        this.setPrevPose(this.odom.getEstimatedPosition());

        this.cameras = new ArrayList<Camera>();
        for (CameraConfig config : cameraConfigs) {
            this.cameras.add(new Camera(config));
        }
        this.limelights = new ArrayList<Limelight>();
        for (LimelightConfig config : limelightConfigs) {
            this.limelights.add(new Limelight(config));
        }
    }

    public VisionBaseSwerve(SwerveModule[] swerveMods, Gyro gyro, SwerveDriveKinematics kinematics,
                            List<CameraConfig> cameraConfigs, List<LimelightConfig> limelightConfigs) {
        this(swerveMods, gyro, kinematics, VecBuilder.fill(0.1, 0.1, 0.05), 
            VecBuilder.fill(0.9, 0.9, 0.9), cameraConfigs, limelightConfigs);
    }

    public void updateVisionPoses() {
        for (Camera camera : this.cameras) {
            Optional<EstimatedRobotPose> estPose = camera.getEstimatedPose(this.getOdomPose());
            if (estPose.isPresent()) {
                this.odom.addVisionMeasurement(estPose.get().estimatedPose.toPose2d(), estPose.get().timestampSeconds, camera.getVisionStdDevs());
            }
        }

    }

    public Optional<Pose2d> getFirstRelativeVisionPose() {
        Optional<Pose2d> pose = Optional.empty();
        for (Camera camera : this.cameras) {
            pose = camera.relativeDistanceFromCameraToAprilTag();
            if (pose.isPresent()) {
                return pose;
            }
        }
        return pose;
    }

    public Transform2d addVisionMovementAdjustment(Transform2d driverInput) {
        DetectedObject bestDetection = null;
        double bestArea = -1.0;

        for (Limelight limelight : this.limelights) {
            Optional<DetectedObject> detection = limelight.getLatestDetection();
            if (detection.isPresent() && ((DetectedObject) detection.get()).area > bestArea) {
                bestDetection = (DetectedObject) detection.get();
                bestArea = bestDetection.area;
            }
        }

        if (bestArea != -1.0 && bestDetection != null) {
            Rotation2d newAngle = driverInput.getRotation().plus(Rotation2d.fromDegrees(bestDetection.xAngleOffset / 10.0));
            double newY = driverInput.getY() + bestDetection.xAngleOffset / 52.0;
            double newX = driverInput.getX() + 1.0 / bestDetection.area;
            return new Transform2d(newX, newY, newAngle);
        } else {
            return driverInput;
        }
    }

    @Override
    public void driveRobotOriented(Translation2d vector, double rot) {
        SwerveModuleState[] states = this.kinematics.toSwerveModuleStates(new ChassisSpeeds(vector.getX(), vector.getY(), rot));
        this.driveRobotOriented(states);
    }

    @Override
    public void driveChassis(ChassisSpeeds chassisSpeeds) {
        SwerveModuleState[] states = this.kinematics.toSwerveModuleStates(chassisSpeeds);
        this.driveRobotOriented(states); 
    }

    public void setOdomPose(Pose2d pose) {
        this.odom.resetPosition(pose.getRotation(), this.getPose(), pose);
        this.setGyro(pose);
    }

    @Override
    public ChassisSpeeds getChassisSpeeds() {
        return this.kinematics.toChassisSpeeds(this.getStates());
    }

    @Override
    public Pose2d getOdomPose() {
        return this.odom.getEstimatedPosition();
    }

    @Override
    public void periodic() {
        super.periodic();
        this.odom.update(this.getGyro().getNormalizedRotation2dAngle(), this.getPose());
        updateVisionPoses();
    }
}