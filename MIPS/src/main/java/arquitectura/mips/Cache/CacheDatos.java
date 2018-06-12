package arquitectura.mips.Cache;

import arquitectura.mips.Cache.BloqueCache;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class CacheDatos {

    private ArrayList<BloqueCache> cache;
    private int tamano;

    public CacheDatos(int tamano) {
        this.cache = new ArrayList<>();
        this.tamano = tamano;
    }

    public CacheDatos() {
        this.cache = new ArrayList<>();
    }

    public ArrayList<BloqueCache> getCache() {
        return cache;
    }

    public void setCache(ArrayList<BloqueCache> cache) {
        this.cache = cache;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }
}
