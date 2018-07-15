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
    public Semaphore dataCacheLock = new Semaphore(1);
    private DataCache remoteCache;

    //Our data cache is initalized with Blocks set to 1s and Invalid state
    public DataCache(int size) {
        this.cache = new ArrayList<BlockCache>();
        this.size = size;
        for (int i = 0; i < this.size; i++) {
            BlockCache bloque = new BlockCache();
            bloque.setEtiqueta(1);
            bloque.setEstado('I');
            this.cache.add(bloque);
        }
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

    //Set words in a Block in the position
    public void setBloqueCache(int posicion, ArrayList<Integer> palabras) {
        BlockCache bloque = new BlockCache();
        bloque.setPalabras(palabras);
        bloque.setEtiqueta(posicion);
        cache.set(posicion % this.size, bloque);
    }

    //Update a specific word in the cache with the given value
    public void updateWord(int cachePosition, int wordsIndex, int value) {
        this.getCache().get(cachePosition).getPalabras().set(wordsIndex, value);
    }
}
