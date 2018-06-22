package arquitectura.mips;

import arquitectura.mips.cache.CacheDatos;
import arquitectura.mips.cache.CacheInstrucciones;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class Nucleo {

    private CacheDatos cacheDatos;
    private CacheInstrucciones cacheInstrucciones;
    private boolean dobleHilo;
    private Hilo hilo1;
    private Hilo hilo2;

    public Nucleo(boolean dobleHilo, int tamano) {
        this.cacheDatos = new CacheDatos(tamano);
        this.cacheInstrucciones = new CacheInstrucciones(tamano);
        this.dobleHilo = dobleHilo;
        this.hilo1 = new Hilo();
        if (dobleHilo) {
            this.hilo2 = new Hilo();
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

    public Hilo getHilo1() {
        return hilo1;
    }

    public void setHilo1(Hilo hilo1) {
        this.hilo1 = hilo1;
    }

    public Hilo getHilo2() {
        return hilo2;
    }

    public void setHilo2(Hilo hilo2) {
        this.hilo2 = hilo2;
    }
}
