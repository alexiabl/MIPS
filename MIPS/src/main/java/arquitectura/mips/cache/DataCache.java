package arquitectura.mips.cache;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class DataCache {

    private ArrayList<BlockCache> cache;
    private int tamano;

    public DataCache(int tamano) {
        this.cache = new ArrayList<BlockCache>();
        this.tamano = tamano;
        for (int i = 0; i < this.tamano; i++) {
            BlockCache bloque = new BlockCache();
            this.cache.add(bloque);
        }
    }

    public DataCache() {
        this.cache = new ArrayList<BlockCache>();
    }

    public ArrayList<BlockCache> getCache() {
        return cache;
    }

    public void setCache(ArrayList<BlockCache> cache) {
        this.cache = cache;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }
}
