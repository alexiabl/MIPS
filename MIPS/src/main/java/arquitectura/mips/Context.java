package arquitectura.mips;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by alexiaborchgrevink on 6/4/18.
 */
public class Context {

    //cola de contextos
    private int PCinitial;
    private int PCfinal;
    private ArrayList<Integer> registers;
    private UUID id;
    //start time
    //end time

    public Context(int pc) {
        this.id = UUID.randomUUID();
        this.registers = new ArrayList<Integer>(32);

        for (int i = 0; i < this.registers.size(); i++) {
            Integer value = new Integer(0);
            this.registers.add(value);
        }
        this.PCinitial = pc;
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
