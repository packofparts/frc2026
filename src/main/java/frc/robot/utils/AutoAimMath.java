package frc.robot.utils;

import java.util.Optional;

import frc.robot.Constants;

public class AutoAimMath {
    private static final double LAUNCHER_HEIGHT = 0.5; 
    private static final double HOOP_HEIGHT = 2.5; 
    private static final double MIN_ANGLE = Constants.Pivot.MIN_ANGLE; 
    private static final double MAX_ANGLE = Constants.Pivot.MAX_ANGLE;
    private static final double GRAVITY = 9.81; 
    private static final double[][] VELOS = {  // https://www.desmos.com/calculator/iblfijmk0u
        {
            7.2,
            25
        },
        {
            7.8,
            30
        },
        {
            8.2,
            33.3
        },
        {
            8.8,
            40
        },
        {
            9.4,
            50
        }
    };

    public static double[] calculateLaunchAngle(double distance/* , Translation2d robotVel*/) {
        for (int i = 0; i < VELOS.length; i++) {
            Optional<Double> angle = calculateLaunchAngleHelper(distance, i/*, robotVel*/);
            if (angle.isPresent()) {
                return new double[]{angle.get(), VELOS[i][1]};
            }
        }
        return new double[]{-1, -1}; // Impossible, would mean we need more velos
    }

    private static Optional<Double> calculateLaunchAngleHelper(double distance, int linVeloIndex/*, Translation2d robotVel*/) {
        double heightDiff = HOOP_HEIGHT - LAUNCHER_HEIGHT;
        double robotSpeedTowardHoop = 0; // robotVel.getNorm(); for shoot on move but bad
        double effectiveV0 = VELOS[linVeloIndex][0] + robotSpeedTowardHoop;
        double v0Squared = effectiveV0 * effectiveV0;
        double discriminant = Math.pow(v0Squared, 2) - GRAVITY * (GRAVITY * distance * distance + 2 * heightDiff * v0Squared);
        if (discriminant < 0) {
            return Optional.empty();
        }
        double tanTheta = (v0Squared + Math.sqrt(discriminant)) / (GRAVITY * distance);
        double angle = Math.toDegrees(Math.atan(tanTheta));
        if (angle >= MIN_ANGLE && angle <= MAX_ANGLE) {
            return Optional.of(angle);
        } else {
            return Optional.empty();
        }
    }
}