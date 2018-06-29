package arquitectura.mips;

import java.util.concurrent.Semaphore;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class BusData {

    public Semaphore lock;
    public static BusData busData;

    public BusData() {
    }


    public static BusData getBusDataInsance() {
        if (busData == null) {
            busData = new BusData();
        }
        return busData;
    }
}
