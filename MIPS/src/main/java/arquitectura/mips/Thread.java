package arquitectura.mips;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class Thread { //corre el hilillo

//tiempo de ejecucion de cada hilillo

    private ArrayList<Integer> IR;
    private ArrayList<Integer> registers = new ArrayList<Integer>(32);
    private int PC;
    private Hilillo hilillo;

    public Thread(Hilillo hilillo) {
        this.hilillo = hilillo;
    }

    public Thread() {

    }

    public void execute(ArrayList<Integer> instruction) { //despues de cada instruccion se le quita quantum
        switch (instruction.get(0)) {
            case 8: //DADDI
                DADDI();
                this.hilillo.quitarQuantum();
                break;
            case 32:
                DADD();
                this.hilillo.quitarQuantum();
                break;
            case 34:
                DSUB();
                this.hilillo.quitarQuantum();
                break;
            case 12:
                DMUL();
                this.hilillo.quitarQuantum();
                break;
            case 14:
                DDIV();
                this.hilillo.quitarQuantum();
                break;
            case 4:
                BEQZ();
                this.hilillo.quitarQuantum();
                break;
            case 5:
                BNEZ();
                this.hilillo.quitarQuantum();
                break;
            case 3:
                JAL();
                this.hilillo.quitarQuantum();
                break;
            case 2:
                JR();
                this.hilillo.quitarQuantum();
                break;
            case 35:
                //LW
                this.hilillo.quitarQuantum();
                break;
            case 43:
                //SW
                this.hilillo.quitarQuantum();
                break;
            case 63:
                FIN();
                this.hilillo.quitarQuantum();
                break;
        }
    }


    public void DADDI() {
        int resultado = this.registers.get(IR.get(1)) + IR.get(3);
        this.registers.set(2, resultado);
    }

    public void DADD() {
        int resultado = this.registers.get(IR.get(1)) + this.registers.get(IR.get(2));
        this.registers.set(3, resultado);
        //System.out.println(registers.get(3));
    }

    public void DSUB() {
        int resultado = registers.get(IR.get(1)) - registers.get(IR.get(2));
        registers.set(3, resultado);
    }

    public void DMUL() {
        int resultado = registers.get(IR.get(1)) * registers.get(IR.get(2));
        registers.set(3, resultado);
    }

    public void DDIV() {
        if (registers.get(IR.get(2)) != 0) {
            int resultado = registers.get(IR.get(1)) / registers.get(IR.get(2));
            registers.set(3, resultado);
        } else {
            System.out.println("Advertencia! Está dividiendo entre 0.");
        }
    }

    public void BEQZ() {
        if (registers.get(IR.get(1)) == 0) {
            PC = PC + 4 * IR.get(3);
        }
    }

    public void BNEZ() {
        if (registers.get(IR.get(1)) != 0) {
            PC = PC + 4 * IR.get(3);
        }
    }

    public void JAL() {
        registers.set(31, PC);
        PC = PC + IR.get(3);
    }

    public void JR() {
        PC = registers.get(IR.get(1));
    }

    public void FIN() {

    }

    public void setIR(ArrayList<Integer> p) {
        this.IR = p;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public void setHilillo(Hilillo hilillo) {
        this.hilillo = hilillo;
    }

    public Hilillo getHilillo() {
        return this.hilillo;
    }


}
