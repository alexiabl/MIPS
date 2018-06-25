package arquitectura.mips.memory;

import arquitectura.mips.block.BlockData;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class MainMemory {

    private ArrayList<BlockData> datos;
    private int tamano;

    public MainMemory(int tamano) {
        this.datos = new ArrayList<BlockData>();
        this.tamano = tamano;
    }

    public MainMemory() {
        this.datos = new ArrayList<BlockData>();
    }


}
