package main.actions;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by tim on 9/17/16.
 */
public final class TrackerSchedular {
    private static final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

    private TrackerSchedular() {}

    public static void scheduleAtFixedRate(Runnable runnable, long start, long inc, TimeUnit unit) {
        exec.scheduleAtFixedRate(runnable, start, inc, unit);
    }

    public static void schedule(Runnable runnable, long countDown, TimeUnit unit) {
        exec.schedule(runnable, countDown, unit);
    }
}
