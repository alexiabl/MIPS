package arquitectura.mips.Memoria;

import arquitectura.mips.Bloque.BloqueDatos;
import arquitectura.mips.Bloque.BloqueInstrucciones;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class MemoriaPrincipal {

    private ArrayList<BloqueDatos> datos;
    private int tamano;

    public MemoriaPrincipal(int tamano) {
        this.datos = new ArrayList<>();
        this.tamano = tamano;
    }

    public MemoriaPrincipal() {
        this.datos = new ArrayList<>();
    }


}
