package arquitectura.mips;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class Hilillo {
    //hilo ejecuta el hillillo

    private Contexto contexto;
    private int direccionInstruccion;

    public void ejecutar() {

    }

    public Contexto getContexto() {
        return contexto;
    }

    public void setContexto(Contexto contexto) {
        this.contexto = contexto;
    }

    public int getDireccionInstruccion() {
        return direccionInstruccion;
    }

    public void setDireccionInstruccion(int direccionInstruccion) {
        this.direccionInstruccion = direccionInstruccion;
    }
}
