package arquitectura.mips.Bloque;

import arquitectura.mips.Bloque.Bloque;

import java.util.Stack;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class BloqueInstrucciones extends Bloque {

    private Stack instrucciones;

    public BloqueInstrucciones() {
        this.instrucciones = new Stack();
    }

    public Stack getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(Stack instrucciones) {
        this.instrucciones = instrucciones;
    }
}
