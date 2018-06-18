package arquitectura.mips;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class Hilo { //corre el hilillo

//tiempo de ejecucion de cada hilillo
    private ArrayList<Integer> IR;
    private ArrayList<Integer> registros = new ArrayList<Integer>();
    private int PC;
/*
    @Override
    public void run() {

    }
*/
    public Hilo() {
        for(int i=0; i<32; i++) {
            registros.add(1);//cambiar a 0

        }

    }


    public void DADDI () {
        int resultado = registros.get(IR.get(1)) + IR.get(3);
        registros.set(2, resultado);
        //System.out.println(registros.get(2));
    }


    public void DADD () {
        int resultado = registros.get(IR.get(1)) + registros.get(IR.get(2));
        registros.set(3, resultado);
        //System.out.println(registros.get(3));
    }

    public void DSUB () {
        int resultado = registros.get(IR.get(1)) - registros.get(IR.get(2));
        registros.set(3, resultado);
    }

    public void DMUL () {
        int resultado = registros.get(IR.get(1)) * registros.get(IR.get(2));
        registros.set(3, resultado);
    }

    public void DDIV () {
        if (registros.get(IR.get(2)) != 0) {
            int resultado = registros.get(IR.get(1)) / registros.get(IR.get(2));
            registros.set(3, resultado);
        }
        else {
            System.out.println("Advertencia! Está dividiendo entre 0.");
        }
    }

    public void BEQZ () {
        if (registros.get(IR.get(1)) == 0) {
            PC = PC + 4 * IR.get(3);
        }
    }

    public void BNEZ () {
            if (registros.get(IR.get(1)) != 0) {
                PC = PC + 4 * IR.get(3);
            }
        }

    public void JAL(){
                registros.set(31, PC);
                PC= PC+ IR.get(3);
            }

    public void JR(){
            PC=registros.get(IR.get(1));
        }

    public void FIN(){
            //
            //
            //
            //
        }


public void setIR(ArrayList<Integer> p){

        this.IR=p;

}






        public int getPC() {
            return PC;
        }

        public void setPC(int PC) {
            this.PC = PC;
        }


        //metodo para DADDI
    //metodo para LW
    //metodo para SW
    //....

}
