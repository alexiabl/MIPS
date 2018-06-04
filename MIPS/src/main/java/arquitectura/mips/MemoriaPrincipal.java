package arquitectura.mips;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class MemoriaPrincipal {

    private ArrayList<BloqueDatos> datos;
    private ArrayList<BloqueInstrucciones> instrucciones;
    private int numBloque;
    private int tamano;

    public MemoriaPrincipal(int tamano) {
        this.datos = new ArrayList<>();
        this.instrucciones = new ArrayList<>();
        this.tamano = tamano;
    }

    public MemoriaPrincipal() {
        this.datos = new ArrayList<>();
        this.instrucciones = new ArrayList<>();
    }


}
