package arquitectura.mips;

import arquitectura.mips.cache.DataCache;
import arquitectura.mips.cache.InstructionCache;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class Core {

    private DataCache dataCache;
    //arrayblockingqueue
    private InstructionCache instructionCache;
    private Thread thread;


    public Core(int tamano) {

    }

    public DataCache getDataCache() {
        return dataCache;
    }

    public void setDataCache(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    public InstructionCache getInstructionCache() {
        return instructionCache;
    }

    public void setInstructionCache(InstructionCache instructionCache) {
        this.instructionCache = instructionCache;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }


}
