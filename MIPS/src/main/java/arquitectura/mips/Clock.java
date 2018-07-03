package arquitectura.mips;

import java.util.concurrent.Semaphore;

/**
 * Clock that will manage the simulations cycles.
 */
public class Clock {

    /// Number of cores that will be run in our simulation.
    private static int coreCount = 2;
    /// Number of cores that already finished a instruction.
    private static int counter = 0;
    /// Lock that will prevent concurency problems regarding variables' modifications.
    private static final Semaphore mutex = new Semaphore(1);
    /// Barrier that prevent our different cores from continuing, if other cores are yet to finish a instruction.
    private static final Semaphore barrier = new Semaphore(1);
    /// Number if cycles that occurred since the beginning of our simulation.
    private static int cycle = 0;

    private static Clock instance = null;

    private Clock() {
    }

    /// Create a new instance of our clock.
    public static Clock getInstance() {
        if (instance == null) {
            instance = new Clock();
        }
        return instance;
    }


    /**
     * Change the number of cores that are left running in our processors.
     */
    public static void reduceCoreCount() {
        try {
            mutex.acquire();
            coreCount -= 1;
            if (counter >= coreCount) {
                barrier.release(counter);
                counter = 0;
            }
            mutex.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns the cycle count, since the beginning of our simulation.
     *
     * @return Cycles counted.
     */
    public static int getCycle() {
        return cycle;
    }

    /**
     * Reset the cycle count back to 0.
     */
    public static void resetCycle() {
        cycle = 0;
    }

    /**
     * Executes our barrier, so that no thread can go on, if we have other running threads.
     */
    public static void executeBarrier() {
        try {
            // Increase counter.
            mutex.acquire();
            // Should the counter be less than our core count, we need to wait in our barrier.
            if (counter + 1 < coreCount && coreCount > 1) {
                counter++;
                mutex.release();
                // Acquire our barrier and wait for the last core to finish.
                barrier.acquire();
            } else {
                cycle++;
                // Release our barrier.
                barrier.release(counter);
                // After the last core finishes, we must increase our cycle count and reduce our counter.
                counter = 0;
                mutex.release();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
