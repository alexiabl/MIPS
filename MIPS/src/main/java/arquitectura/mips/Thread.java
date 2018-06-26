package arquitectura.mips;

import arquitectura.mips.block.BlockInstructions;
import arquitectura.mips.cache.InstructionCache;
import arquitectura.mips.memory.InstructionsMemory;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class Thread implements Runnable { //corre el hilillo

//tiempo de ejecucion de cada hilillo

    private ArrayList<Integer> IR;
    private ArrayList<Integer> registers = new ArrayList<Integer>(32);
    private int PC;
    private Hilillo hilillo;

    public Thread(Hilillo hilillo) {
        this.hilillo = hilillo;
        this.PC = hilillo.getContext().getPCinitial();
        this.registers = hilillo.getContext().getRegisters();
    }

    public Thread() {

    }

    public void executeInstruction(ArrayList<Integer> instruction) { //despues de cada instruccion se le quita quantum
        switch (instruction.get(0)) {
            case 8: //DADDI
                DADDI();
                this.hilillo.removeQuantum();
                break;
            case 32:
                DADD();
                this.hilillo.removeQuantum();
                break;
            case 34:
                DSUB();
                this.hilillo.removeQuantum();
                break;
            case 12:
                DMUL();
                this.hilillo.removeQuantum();
                break;
            case 14:
                DDIV();
                this.hilillo.removeQuantum();
                break;
            case 4:
                BEQZ();
                this.hilillo.removeQuantum();
                break;
            case 5:
                BNEZ();
                this.hilillo.removeQuantum();
                break;
            case 3:
                JAL();
                this.hilillo.removeQuantum();
                break;
            case 2:
                JR();
                this.hilillo.removeQuantum();
                break;
            case 35:
                //LW
                this.hilillo.removeQuantum();
                break;
            case 43:
                //SW
                this.hilillo.removeQuantum();
                break;
            case 63:
                FIN();
                this.hilillo.removeQuantum();
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
        this.PC = hilillo.getContext().getPCinitial();
        this.registers = hilillo.getContext().getRegisters();
    }

    public Hilillo getHilillo() {
        return this.hilillo;
    }


    @Override
    public void run() {
        int endIR = this.hilillo.getContext().getPCfinal();
        int count = 0;
        while (this.PC <= endIR) {
            BlockInstructions blockInstructions = InstructionsMemory.instructionsMemory.getInstrucciones().get(this.PC);
            this.IR = blockInstructions.getInstructions();
            this.executeInstruction(blockInstructions.getInstructions());
            this.PC += 1;
        }

    }
}
