package arquitectura.mips;

import arquitectura.Simulation;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App
{

    public static Simulation simulation;

    public static void main(String[] args) throws IOException {


        simulation.executeSimulation();
        //unica cola de contextos para los dos nucleos

        //1. leer todos los archivos y meterlo a la memoria de instrucciones. Al mismo asignar el PC al contexto de
        //   cada hilillo - LISTO
        //2.

        //memoria de instrucciones
        //direccion de memoria (de instrucciones) de instruccion1
        //saca instruccion y lo pone en IR
        //hilo es el que ejecuta la instruccion del hilillo
        //en hilo se implementan los DADDI, LW, SW etc.
        //vuelve a leer el pc que apunta a la siguiente instruccion
        //cada vez q finaliza instruccion se le resta el quantum del hilillo
        //hilillo direccion tine donde esta la primera instruccion que tiene q ejecutar hasta FIN
        //cola de contextos, el primer hilillo agarra el contexto de la cola de contextos
        //leer los archivos de instrucciones -> meter a la cola de contextos
        //cuales archivos se ejecutan en cual nucleo
        //como crear los hilillos, random?
        //estructura para estados de los hilillos y el bloque si esta bloqueado o reservado
        //cada vez q hay que cambiar de ciclo -> doble barrera?
        //java.util.concurrent.Phaser
    }
}
