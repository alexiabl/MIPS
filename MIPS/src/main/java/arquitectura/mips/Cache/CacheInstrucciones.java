package arquitectura.mips.Cache;

import java.util.Stack;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class CacheInstrucciones {

//cuando hay fallo de CI?

    private Stack instrucciones;
    private int tamano;

    public CacheInstrucciones(int tamano) {
        this.instrucciones = new Stack();
        this.tamano = tamano;
    }

    public CacheInstrucciones() {
        this.instrucciones = new Stack();
    }

    public Stack getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(Stack instrucciones) {
        this.instrucciones = instrucciones;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }
}
