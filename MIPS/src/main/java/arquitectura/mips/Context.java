package arquitectura.mips;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Context holds information that will be used for each of the Hilillos that will be run.
 */
public class Context {

    //cola de contextos
    private int PCinitial;
    private int PCfinal;
    private ArrayList<Integer> registers;
    private UUID id;
    //start time
    //end time

    public Context(int PCinitial) {
        this.id = UUID.randomUUID();
        this.registers = new ArrayList<Integer>(32);

        this.PCinitial = PCinitial;
    }

    public int getPCinitial() {
        return PCinitial;
    }

    public void setPCinitial(int PCinitial) {
        this.PCinitial = PCinitial;
    }

    public int getPCfinal() {
        return PCfinal;
    }

    public void setPCfinal(int PCfinal) {
        this.PCfinal = PCfinal;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ArrayList<Integer> getRegisters() {
        return this.registers;
    }

    public void setRegisters(ArrayList<Integer> registros) {
        this.registers = registros;
    }

    public UUID getId() {
        return this.id;
    }
}
