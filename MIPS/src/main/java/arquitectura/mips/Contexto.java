package arquitectura.mips;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class Contexto {

    //cola de contextos
    private int PC;
    private ArrayList<Integer> registros;
    //todos los registros comienzan en 0
    private UUID id;
    //start time
    //end time

    public Contexto() {
        this.registros = new ArrayList<Integer>(32);
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public ArrayList<Integer> getRegistros() {
        return registros;
    }

    public void setRegistros(ArrayList<Integer> registros) {
        this.registros = registros;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
