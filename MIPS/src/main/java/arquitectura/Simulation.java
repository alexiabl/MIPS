package arquitectura;

import arquitectura.mips.*;
import arquitectura.mips.Thread;
import arquitectura.mips.cache.CacheDatos;
import arquitectura.mips.cache.CacheInstrucciones;
import arquitectura.mips.memory.InstructionsMemory;
import arquitectura.mips.memory.MainMemory;
import arquitectura.mips.util.Util;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Created by alexiaborchgrevink on 6/23/18.
 */
public class Simulation {

    private Thread thread1;
    private Thread thread2;
    private MasterThread masterThread;
    private MainMemory mainMemory;
    private Core core0;
    private Core core1;
    private InstructionsMemory instructionsMemory;
    private CacheDatos cacheDatos0;
    private CacheDatos cacheDatos1;
    private CacheInstrucciones cacheInstrucciones0;
    private CacheInstrucciones cacheInstrucciones1;
    private List<Hilillo> hilillos;
    private List<Hilillo> hillillosBackup;

    public Simulation(List<Hilillo> hililloList) {
        this.core0 = new Core(true, 4); //2 hilos
        this.core1 = new Core(false, 4); //1 hilo
        this.mainMemory = new MainMemory(8);
        this.cacheDatos0 = core0.getCacheDatos();
        this.cacheInstrucciones0 = core0.getCacheInstrucciones();
        this.cacheDatos1 = core1.getCacheDatos();
        this.cacheInstrucciones1 = core1.getCacheInstrucciones();
        this.hilillos = hililloList;
        this.hillillosBackup = hililloList;
        this.masterThread = new MasterThread();
        this.thread1 = new Thread();
        this.thread2 = new Thread();
    }

    public void executeSimulation() throws IOException {

        Util util = new Util();
        util.leerArchivos();

        this.instructionsMemory = util.getInstructionsMemory();
        Queue<Context> colaDeContexts = util.getColaDeContexts();

        //asignar hilillo a hilo de manera random
        asignHilillo();

    }

    public void asignHilillo() { //asignar hilillo a hilo de nucleo
        Random random = new Random();
        int hililloRandom = random.nextInt() % this.hilillos.size();
        int hililloRandom2 = random.nextInt() % this.hilillos.size();

        Hilillo hilillo1 = this.hilillos.get(hililloRandom);
        this.thread1.setHilillo(hilillo1);
        this.hilillos.remove(hilillo1);

        Hilillo hilillo2 = this.hilillos.get(hililloRandom2);
        this.thread2.setHilillo(hilillo2);
        this.hilillos.remove(hilillo2);

    }
}
