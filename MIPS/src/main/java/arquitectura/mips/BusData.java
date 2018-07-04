package arquitectura.mips;

import java.util.concurrent.Semaphore;

/**
 * Bus for Data operations. Uses singleton pattern as we need the BusData to be shared in our program.
 */
public class BusData {

    public Semaphore lock;
    public static BusData busData;

    public BusData() {
        this.lock = new Semaphore(1);
    }


    public static BusData getBusDataInsance() {
        if (busData == null) {
            busData = new BusData();
        }
        return busData;
    }
}
