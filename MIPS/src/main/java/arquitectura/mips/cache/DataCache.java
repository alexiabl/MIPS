package arquitectura.mips.cache;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

/**
 * Cache of Data in our processor
 */
public class DataCache {

    private ArrayList<BlockCache> cache;
    private int size;
    public Semaphore dataCacheLock;
    private DataCache remoteCache;

    public DataCache(int size) {
        this.cache = new ArrayList<BlockCache>();
        this.size = size;
        for (int i = 0; i < this.size; i++) {
            BlockCache bloque = new BlockCache();
            bloque.setEstado('I');
            this.cache.add(bloque);
        }
        //this.dataCacheLock.unlock();
    }


    public ArrayList<BlockCache> getCache() {
        return cache;
    }

    public void setCache(ArrayList<BlockCache> cache) {
        this.cache = cache;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public DataCache getRemoteCache() {
        return remoteCache;
    }

    public void setRemoteCache(DataCache remoteCache) {
        this.remoteCache = remoteCache;
    }

    public void setBloqueCache(int posicion, ArrayList<Integer> palabras) {
        BlockCache bloque = new BlockCache();
        bloque.setPalabras(palabras);
        bloque.setEtiqueta(posicion);
        cache.set(posicion % this.size, bloque);
    }
}
