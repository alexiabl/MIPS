package arquitectura.mips.cache;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

/**
 * Created by alexiaborchgrevink on 6/4/18.
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
}
