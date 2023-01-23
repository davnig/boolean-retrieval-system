package com.davnig.units.util;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class ComputationTimeCalculator {

    private static long start;
    private static long end;

    public static void startComputationTimer() {
        start = System.nanoTime();
    }

    public static void stopComputationTimer() {
        end = System.nanoTime();
    }

    public static long getComputationTimeInMillis() {
        return NANOSECONDS.toMillis(end - start);
    }

}
