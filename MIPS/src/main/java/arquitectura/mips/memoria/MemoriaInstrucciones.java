package arquitectura.mips.memoria;

import arquitectura.mips.bloque.BloqueInstrucciones;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class MemoriaInstrucciones {

    private ArrayList<BloqueInstrucciones> instrucciones;


    public MemoriaInstrucciones() {
        instrucciones = new ArrayList<BloqueInstrucciones>();
    }

    public ArrayList<BloqueInstrucciones> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(ArrayList<BloqueInstrucciones> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public void addBloqueInstruccion(BloqueInstrucciones bloqueInstrucciones) {
        this.instrucciones.add(bloqueInstrucciones);
    }
}
