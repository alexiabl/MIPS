package arquitectura.mips;

import java.util.concurrent.Semaphore;

/**
 * Bus for instructions. Uses singleton pattern as we need the BusInstructions to be shared in our program.
 */
public class BusInstructions {

    public Semaphore lock;
    public static BusInstructions busInstructions;

    public BusInstructions() {
    }

    public static BusInstructions getBusInstructionsInstance() {
        if (busInstructions == null) {
            busInstructions = new BusInstructions();
        }
        return busInstructions;
    }

}
