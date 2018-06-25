package arquitectura.mips;

import arquitectura.mips.cache.CacheDatos;
import arquitectura.mips.cache.CacheInstrucciones;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class Core {

    private CacheDatos cacheDatos;
    private CacheInstrucciones cacheInstrucciones;
    private boolean dobleHilo;
    private Thread thread1;
    private Thread thread2;

    public Core(boolean dobleHilo, int tamano) {
        this.cacheDatos = new CacheDatos(tamano);
        this.cacheInstrucciones = new CacheInstrucciones(tamano);
        this.dobleHilo = dobleHilo;
        this.thread1 = new Thread();
        if (dobleHilo) {
            this.thread2 = new Thread();
        }
    }

    public CacheDatos getCacheDatos() {
        return cacheDatos;
    }

    public void setCacheDatos(CacheDatos cacheDatos) {
        this.cacheDatos = cacheDatos;
    }

    public CacheInstrucciones getCacheInstrucciones() {
        return cacheInstrucciones;
    }

    public void setCacheInstrucciones(CacheInstrucciones cacheInstrucciones) {
        this.cacheInstrucciones = cacheInstrucciones;
    }

    public boolean isDobleHilo() {
        return dobleHilo;
    }

    public void setDobleHilo(boolean dobleHilo) {
        this.dobleHilo = dobleHilo;
    }

    public Thread getThread1() {
        return thread1;
    }

    public void setThread1(Thread thread1) {
        this.thread1 = thread1;
    }

    public Thread getThread2() {
        return thread2;
    }

    public void setThread2(Thread thread2) {
        this.thread2 = thread2;
    }
}
