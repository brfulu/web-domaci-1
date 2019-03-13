package app;

import java.util.Random;

public class Helper {
    public static Random r = new Random();

    public static double getRandomDouble(double rangeMin, double rangeMax) {
        return rangeMin + (rangeMax - rangeMin) * r.nextDouble();
    }

    public static int getRandomInt(int rangeMin, int rangeMax) {
        return r.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
    }
}
