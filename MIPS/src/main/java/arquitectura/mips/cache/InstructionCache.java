package arquitectura.mips.cache;

import arquitectura.mips.block.BlockInstructions;
import arquitectura.mips.memory.InstructionsMemory;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class InstructionCache {

    private ArrayList<BlockInstructions> instrucciones;
    private int tamano;
    public static InstructionCache instructionCache;

    public InstructionCache(int tamano) {
        this.instrucciones = new ArrayList<BlockInstructions>();
        this.tamano = tamano;
        for (int i = 0; i < this.tamano; i++) {
            BlockInstructions bloque = new BlockInstructions();
            this.instrucciones.add(bloque);
        }
    }

    public static InstructionCache getInstructionsCache() {
        if (instructionCache == null) {
            instructionCache = new InstructionCache();
        }
        return instructionCache;
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
}
