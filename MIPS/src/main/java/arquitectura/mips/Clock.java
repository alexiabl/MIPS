package arquitectura.mips;

import java.util.concurrent.Semaphore;

/**
 * Clock that will manage the simulations cycles.
 */
public class Clock {

    private static int coreCount = 2;
    private static int counter = 0;
    private static final Semaphore mutex = new Semaphore(1);
    private static final Semaphore barrier = new Semaphore(1);
    private static int cycle = 0;

    private static Clock instance = null;

    private Clock() {
    }

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
            mutex.acquire();
            if (counter + 1 < coreCount && coreCount > 1) {
                counter++;
                mutex.release();
                barrier.acquire();
            } else {
                cycle++;
                barrier.release(counter);
                counter = 0;
                mutex.release();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
