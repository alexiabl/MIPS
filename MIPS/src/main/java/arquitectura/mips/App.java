package arquitectura.mips;

import arquitectura.mips.Cache.CacheDatos;
import arquitectura.mips.Cache.CacheInstrucciones;
import arquitectura.mips.Memoria.MemoriaPrincipal;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        Nucleo nucleo0 = new Nucleo(true, 4); //2 hilos
        Nucleo nucleo1 = new Nucleo(false, 4); //1 hilo

        //unica cola de contextos para los dos nucleos

        MemoriaPrincipal memoriaPrincipal = new MemoriaPrincipal(8);

        CacheDatos cacheDatos0 = nucleo0.getCacheDatos();
        CacheInstrucciones cacheInstrucciones0 = nucleo0.getCacheInstrucciones();

        CacheDatos cacheDatos1 = nucleo1.getCacheDatos();
        CacheInstrucciones cacheInstrucciones1 = nucleo1.getCacheInstrucciones();

        //1. leer todos los archivos y meterlo a la memoria de instrucciones. Al mismo asignar el PC al contexto de
        //   cada hilillo

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
    }
}
