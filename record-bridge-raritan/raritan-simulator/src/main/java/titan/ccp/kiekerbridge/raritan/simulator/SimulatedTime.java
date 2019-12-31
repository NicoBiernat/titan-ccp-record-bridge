package titan.ccp.kiekerbridge.raritan.simulator;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A simple simulated time that can be used to speed up or slow down real time.
 * Note that this clock is not as precise as System.currentTimeMillis(),
 * because it updates the time at a fixed interval (e.g. every second). In this period the time will stay the same.
 */
public class SimulatedTime {

    private static long currentSimulatedTime = System.currentTimeMillis() ;//+ 1000L * 60L * 60L * 24L * 30L */*increment in months: */ 1;//TODO

    private static ScheduledExecutorService scheduler;
    private static ZoneId zoneId;

    private static long incrementInterval = 1;
    private static TimeUnit incrementIntervalUnit = TimeUnit.SECONDS;
    private static long incrementAmount = 1;
    private static TimeUnit incrementAmountUnit = TimeUnit.SECONDS;

    public static void start() {
        Runnable timeIncrementer = () -> {
            currentSimulatedTime += incrementAmountUnit.toMillis(incrementAmount); // advance time one hour every second
//        System.out.println("[SimulatedTime] Advancing time by "+incrementAmount+" "+incrementAmountUnit.toString()); // TODO
        };
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(timeIncrementer, 0, incrementInterval, incrementIntervalUnit);
    }


    /**
     * Simulation of System.currentTimeMillis().
     * @return
     * The current simulated time in milliseconds.
     */
    public static long currentTimeMillis() {
        return currentSimulatedTime;
    }

    /**
     * Returns the start time for {@link SimulatedSensor}s.
     * It is set to the last monday at 00:00h before execution start.
     *
     * This makes sure that all sensors start (have their start timestamp) at exactly the same time.
     * So that it is possible to debug by comparing the generated values
     * for each day and hour of week to the arriving values.
     * Also when using seeds for generating the data, this synchronizes the sensors correctly.
     *
     * @return
     * The start time in ms.
     */
    public static long startTimeMillis() {
        LocalDateTime now = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Europe/Paris"));
        LocalDateTime lastMonday;
        if(now.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            lastMonday = now.truncatedTo(ChronoUnit.DAYS);
        } else {
            int diff = now.getDayOfWeek().getValue()-1;
            lastMonday = now.minusDays(diff).truncatedTo(ChronoUnit.DAYS);
        }

//        System.err.println("Last monday: "+"(day)"+lastMonday.getDayOfWeek()+"(time)"+
//               lastMonday.getHour()+":"+lastMonday.getMinute()+":"+lastMonday.getSecond());
//        System.err.println("Last monday tostring: "+lastMonday.toString());
        return lastMonday
                .atZone(ZoneId.of("Europe/Paris"))
                .toInstant()
                .toEpochMilli();
    }

    public static long getIncrementIntervalMillis() {
        return incrementIntervalUnit.toMillis(incrementInterval);
    }

    public static long getIncrementAmountMillis() {
        return incrementAmountUnit.toMillis(incrementAmount);
    }

    public static long getSpeed() {
        return incrementAmountUnit.toMillis(incrementAmount) / incrementIntervalUnit.toMillis(incrementInterval);
    }

    public static ZoneId getTimeZone() {
        return zoneId;
    }

    /**
     * Set the speed of the simulated time.
     * For example: let one hour pass every second of real time.
     * @param incrementInterval
     * How often the time should be incremented.
     * @param incrementAmount
     * By how much the time should be incremented.
     */
    public static void setTimeSpeed(long incrementInterval,
                                          TimeUnit incrementIntervalUnit,
                                          long incrementAmount,
                                          TimeUnit incrementAmountUnit) {
        SimulatedTime.incrementInterval = incrementInterval;
        SimulatedTime.incrementIntervalUnit = incrementIntervalUnit;
        SimulatedTime.incrementAmount = incrementAmount;
        SimulatedTime.incrementAmountUnit = incrementAmountUnit;
    }

    public static void setTimeZone(ZoneId zoneId) {
        SimulatedTime.zoneId = zoneId;
    }
}
