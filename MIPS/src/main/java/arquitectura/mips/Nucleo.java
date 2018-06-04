package arquitectura.mips;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class Nucleo {

    private CacheDatos cacheDatos;
    private CacheInstrucciones cacheInstrucciones;
    private boolean dobleHilo;

    public Nucleo(boolean dobleHilo, int tamano) {
        this.cacheDatos = new CacheDatos(tamano);
        this.cacheInstrucciones = new CacheInstrucciones(tamano);
        this.dobleHilo = dobleHilo;
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
}
