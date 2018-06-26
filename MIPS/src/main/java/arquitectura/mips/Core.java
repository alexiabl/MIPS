package arquitectura.mips;

import arquitectura.mips.cache.DataCache;
import arquitectura.mips.cache.InstructionCache;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class Core {

    private DataCache dataCache;
    private InstructionCache instructionCache;
    private boolean dobleHilo;
    private Thread thread1;
    private Thread thread2;

    public Core(boolean dobleHilo, int tamano) {
        this.dataCache = new DataCache(tamano);
        this.instructionCache = new InstructionCache(tamano);
        this.dobleHilo = dobleHilo;
        this.thread1 = new Thread();
        if (dobleHilo) {
            this.thread2 = new Thread();
        }
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

    public boolean isDobleHilo() {
        return dobleHilo;
    }

    public void setDobleHilo(boolean dobleHilo) {
        this.dobleHilo = dobleHilo;
    }

    public Thread getThread1() {
        return thread1;
    }

    public void setThread1(Thread thread1) {
        this.thread1 = thread1;
    }

    public Thread getThread2() {
        return thread2;
    }

    public void setThread2(Thread thread2) {
        this.thread2 = thread2;
    }
}
