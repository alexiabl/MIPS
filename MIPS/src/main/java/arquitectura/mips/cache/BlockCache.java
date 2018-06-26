package arquitectura.mips.cache;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class BlockCache {

    private int etiqueta;
    private char estado;
    private ArrayList<Integer> palabras;

    public BlockCache() {
        this.palabras = new ArrayList<Integer>();

    }

    public int getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(int etiqueta) {
        this.etiqueta = etiqueta;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public ArrayList<Integer> getPalabras() {
        return palabras;
    }

    public void setPalabras(ArrayList<Integer> palabras) {
        this.palabras = palabras;
    }
}
