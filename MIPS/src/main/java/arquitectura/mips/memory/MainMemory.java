package arquitectura.mips.memory;

import arquitectura.mips.block.BlockData;
import arquitectura.mips.cache.BlockCache;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class MainMemory {

    private ArrayList<BlockData> datos;
    private int size;
    private static MainMemory mainMemory;

    public MainMemory(int size) {
        this.datos = new ArrayList<BlockData>();
        this.size = size;
        ArrayList<Integer> words = new ArrayList<Integer>();
        for (int j = 0; j < 4; j++) {
            words.add(1);
        }
        for (int i = 0; i < this.size; i++) {
            BlockData bloque = new BlockData();
            bloque.setNumBloque(i);
            bloque.setWords(words);
            this.datos.add(bloque);
            mainMemory = this;
        }
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

    public int getsize() {
        return size;
    }

    public void setsize(int size) {
        this.size = size;
    }

    public void setDatosBloque(int indice, ArrayList<Integer> palabras){
        BlockData temp= new BlockData();
        temp.setWords(palabras);
        datos.set(indice, temp);
    }
}
