package arquitectura.mips.cache;

import java.util.ArrayList;

/**
 *  Block that holds the information in the Caches.
 */
public class BlockCache {

    private int etiqueta;
    private char estado;
    private ArrayList<Integer> palabras;

    public BlockCache() {
        this.palabras = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            this.palabras.add(0);
        }

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

    public void setPalabra(int pos, int value) { this.palabras.set(pos, value); }
}
