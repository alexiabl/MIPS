package arquitectura.mips;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class Hilo { //corre el hilillo

//tiempo de ejecucion de cada hilillo

    private ArrayList<Integer> IR;
    private ArrayList<Integer> registros = new ArrayList<Integer>(32);
    private int PC;

    public Hilo() {
        for (int i = 0; i < registros.size(); i++) {
            this.registros.add(0);//cambiar a 0
        }
    }

    public void ejecutar(ArrayList<Integer> instruccion) {
        switch (instruccion.get(0)) {
            case 8: //DADDI
                DADDI();
                break;
            case 32:
                DADD();
                break;
            case 34:
                DSUB();
                break;
            case 12:
                DMUL();
                break;
            case 14:
                DDIV();
                break;
            case 4:
                BEQZ();
                break;
            case 5:
                BNEZ();
                break;
            case 3:
                JAL();
                break;
            case 2:
                JR();
                break;
            case 35:
                //LW
                break;
            case 43:
                //SW
                break;
            case 63:
                FIN();
                break;
        }
    }


    public void DADDI() {
        int resultado = this.registros.get(IR.get(1)) + IR.get(3);
        this.registros.set(2, resultado);
    }

    public void DADD() {
        int resultado = this.registros.get(IR.get(1)) + this.registros.get(IR.get(2));
        this.registros.set(3, resultado);
        //System.out.println(registros.get(3));
    }

    public void DSUB() {
        int resultado = registros.get(IR.get(1)) - registros.get(IR.get(2));
        registros.set(3, resultado);
    }

    public void DMUL() {
        int resultado = registros.get(IR.get(1)) * registros.get(IR.get(2));
        registros.set(3, resultado);
    }

    public void DDIV() {
        if (registros.get(IR.get(2)) != 0) {
            int resultado = registros.get(IR.get(1)) / registros.get(IR.get(2));
            registros.set(3, resultado);
        } else {
            System.out.println("Advertencia! Está dividiendo entre 0.");
        }
    }

    public void BEQZ() {
        if (registros.get(IR.get(1)) == 0) {
            PC = PC + 4 * IR.get(3);
        }
    }

    public void BNEZ() {
        if (registros.get(IR.get(1)) != 0) {
            PC = PC + 4 * IR.get(3);
        }
    }

    public void JAL() {
        registros.set(31, PC);
        PC = PC + IR.get(3);
    }

    public void JR() {
        PC = registros.get(IR.get(1));
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


}
