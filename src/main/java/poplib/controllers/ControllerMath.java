package poplib.controllers;

import java.lang.Math;

public class ControllerMath {
    public static double cube(double triggerVal) {
        return Math.pow(triggerVal, 3);
    }

    public static double applyDeadband(double initialVal, double deadband) {
        return Math.abs(initialVal) <  deadband ? 0 : (
            (initialVal - ((initialVal < 0 ? -1 : 1) * deadband)) 
            / (1 - deadband));
    }

    public static double logistic(double triggerVal, double k) {
        return 2/(1 + (Math.pow((Math.E), -k * (triggerVal)))) - 1;
    }
}