package arquitectura.mips.memory;

import arquitectura.mips.block.BlockInstructions;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class InstructionsMemory {

    private ArrayList<BlockInstructions> instrucciones;


    public InstructionsMemory() {
        instrucciones = new ArrayList<BlockInstructions>();
    }

    public ArrayList<BlockInstructions> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(ArrayList<BlockInstructions> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public void addBloqueInstruccion(BlockInstructions bloqueInstrucciones) {
        this.instrucciones.add(bloqueInstrucciones);
    }
}
