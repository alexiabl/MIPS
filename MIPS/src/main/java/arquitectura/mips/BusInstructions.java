package arquitectura.mips;

import java.util.concurrent.Semaphore;

/**
 * Created by alexiaborchgrevink on 6/25/18.
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
