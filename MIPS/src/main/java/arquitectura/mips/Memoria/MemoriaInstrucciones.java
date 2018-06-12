package arquitectura.mips.Memoria;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class MemoriaInstrucciones {

    private ArrayList<Integer> instrucciones;

    public MemoriaInstrucciones() {
        instrucciones = new ArrayList<>();
    }

    public ArrayList<Integer> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(ArrayList<Integer> instrucciones) {
        this.instrucciones = instrucciones;
    }
}
