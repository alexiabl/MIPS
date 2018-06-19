package arquitectura.mips.bloque;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class BloqueInstrucciones extends Bloque {

    private ArrayList<Integer> instrucciones;

    public BloqueInstrucciones() {
        this.instrucciones = new ArrayList<Integer>();
    }

    public ArrayList<Integer> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(ArrayList instrucciones) {
        this.instrucciones = instrucciones;
    }
}
