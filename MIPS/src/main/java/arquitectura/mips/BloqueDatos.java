package arquitectura.mips;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class BloqueDatos {

    private ArrayList<Integer> palabras;

    public BloqueDatos() {
        this.palabras = new ArrayList<>();
    }

    public ArrayList<Integer> getPalabras() {
        return palabras;
    }

    public void setPalabras(ArrayList<Integer> palabras) {
        this.palabras = palabras;
    }
}
