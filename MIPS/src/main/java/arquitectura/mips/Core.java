package arquitectura.mips;

import arquitectura.mips.cache.DataCache;
import arquitectura.mips.cache.InstructionCache;

import java.util.ArrayList;

/**
 * Core that executes a thread
 */
public class Core {

    private DataCache dataCache;
    private InstructionCache instructionCache;
    private Thread thread;
    private ArrayList<Integer> registers = new ArrayList<Integer>(32);


    public Core() {
        for (int i = 0; i < 32; i++) {
            Integer value = new Integer(0);
            this.registers.add(value);
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

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public ArrayList<Integer> getRegisters() {
        return this.registers;
    }

    public void setRegisters(ArrayList<Integer> registers) {
        this.registers = registers;
    }
}
