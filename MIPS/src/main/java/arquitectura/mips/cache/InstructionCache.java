package arquitectura.mips.cache;

import arquitectura.mips.block.BlockInstructions;
import arquitectura.mips.memory.InstructionsMemory;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

/**
 * Cache of instructions in our processor
 */
public class InstructionCache {

    private ArrayList<BlockInstructions> instrucciones;
    private int tamano;
    public Semaphore instructionCacheLock;
    private InstructionCache remoteInstructionCache;

    public InstructionCache(int tamano) {
        this.instrucciones = new ArrayList<BlockInstructions>();
        this.tamano = tamano;
        for (int i = 0; i < this.tamano; i++) {
            BlockInstructions bloque = new BlockInstructions();
            this.instrucciones.add(bloque);
        }
        this.instructionCacheLock = new Semaphore(1);
    }

    public InstructionCache() {
        this.instrucciones = new ArrayList<BlockInstructions>();
    }

    public ArrayList<BlockInstructions> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(ArrayList<BlockInstructions> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public InstructionCache getRemoteInstructionCache() {
        return remoteInstructionCache;
    }

    public void setRemoteInstructionCache(InstructionCache remoteInstructionCache) {
        this.remoteInstructionCache = remoteInstructionCache;
    }
}
