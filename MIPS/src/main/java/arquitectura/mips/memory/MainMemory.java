package arquitectura.mips.memory;

import arquitectura.mips.block.BlockData;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class MainMemory {

    private ArrayList<BlockData> datos;
    private int tamano;
    private static MainMemory mainMemory;

    public MainMemory(int tamano) {
        this.datos = new ArrayList<BlockData>();
        this.tamano = tamano;
    }

    public static MainMemory getMainMemoryInstance() {
        if (mainMemory == null) {
            mainMemory = new MainMemory(8);
        }
        return mainMemory;
    }

    public MainMemory() {
        this.datos = new ArrayList<BlockData>();
    }


}
