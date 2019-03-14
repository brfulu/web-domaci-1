package app;

import java.util.Random;

public class Helper {
    public static Random r = new Random();

    public static int getRandomInt(int rangeMin, int rangeMax) {
        return rangeMin + r.nextInt((rangeMax - rangeMin) + 1);
    }

    public static boolean getRandomBoolean() {
        return r.nextBoolean();
    }
}
