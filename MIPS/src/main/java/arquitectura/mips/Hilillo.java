package arquitectura.mips;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class Hilillo implements Runnable {

    private Contexto contexto;

    private ArrayList<Integer> instrucciones;

    @Override
    public void run() {

    }

    public Contexto getContexto() {
        return contexto;
    }

    public void setContexto(Contexto contexto) {
        this.contexto = contexto;
    }

    public ArrayList<Integer> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(ArrayList<Integer> instrucciones) {
        this.instrucciones = instrucciones;
    }
}
