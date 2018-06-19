package arquitectura.mips.cache;

import arquitectura.mips.bloque.BloqueInstrucciones;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class CacheInstrucciones {

    private ArrayList<BloqueInstrucciones> instrucciones;
    private int tamano;

    public CacheInstrucciones(int tamano) {
        this.instrucciones = new ArrayList<BloqueInstrucciones>();
        this.tamano = tamano;
        for (int i = 0; i < this.tamano; i++) {
            BloqueInstrucciones bloque = new BloqueInstrucciones();
            this.instrucciones.add(bloque);
        }
    }

    public CacheInstrucciones() {
        this.instrucciones = new ArrayList<BloqueInstrucciones>();
    }

    public ArrayList<BloqueInstrucciones> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(ArrayList<BloqueInstrucciones> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }
}
