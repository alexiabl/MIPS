package arquitectura.mips.Memoria;

import arquitectura.mips.Bloque.BloqueInstrucciones;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class MemoriaInstrucciones {

    private ArrayList<BloqueInstrucciones> instrucciones;


    public MemoriaInstrucciones() {
        instrucciones = new ArrayList<>();
    }

    public ArrayList<BloqueInstrucciones> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(ArrayList<BloqueInstrucciones> instrucciones) {
        this.instrucciones = instrucciones;
    }
}
