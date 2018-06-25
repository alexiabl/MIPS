package arquitectura.mips.cache;

import arquitectura.mips.block.BlockInstructions;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class CacheInstrucciones {

    private ArrayList<BlockInstructions> instrucciones;
    private int tamano;

    public CacheInstrucciones(int tamano) {
        this.instrucciones = new ArrayList<BlockInstructions>();
        this.tamano = tamano;
        for (int i = 0; i < this.tamano; i++) {
            BlockInstructions bloque = new BlockInstructions();
            this.instrucciones.add(bloque);
        }
    }

    public CacheInstrucciones() {
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
