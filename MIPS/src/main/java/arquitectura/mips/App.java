package arquitectura.mips;

import arquitectura.mips.cache.CacheDatos;
import arquitectura.mips.cache.CacheInstrucciones;
import arquitectura.mips.memoria.MemoriaInstrucciones;
import arquitectura.mips.memoria.MemoriaPrincipal;
import arquitectura.mips.util.Util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Hello world!
 *
 */
public class App
{
    public static void ejecutarSimulacion() throws IOException {

        Nucleo nucleo0 = new Nucleo(true, 4); //2 hilos
        Nucleo nucleo1 = new Nucleo(false, 4); //1 hilo
        MemoriaPrincipal memoriaPrincipal = new MemoriaPrincipal(8);
        CacheDatos cacheDatos0 = nucleo0.getCacheDatos();
        CacheInstrucciones cacheInstrucciones0 = nucleo0.getCacheInstrucciones();
        CacheDatos cacheDatos1 = nucleo1.getCacheDatos();
        CacheInstrucciones cacheInstrucciones1 = nucleo1.getCacheInstrucciones();

        Util util = new Util();
        util.leerArchivos();

        MemoriaInstrucciones memoriaInstrucciones = util.getMemoriaInstrucciones();
        Queue<Contexto> colaDeContextos = util.getColaDeContextos();


    }

    public static void main(String[] args) throws IOException {


        ejecutarSimulacion();
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
