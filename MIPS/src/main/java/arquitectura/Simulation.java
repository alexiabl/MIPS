package arquitectura;

import arquitectura.mips.*;
import arquitectura.mips.Thread;
import arquitectura.mips.cache.DataCache;
import arquitectura.mips.cache.InstructionCache;
import arquitectura.mips.memory.InstructionsMemory;
import arquitectura.mips.memory.MainMemory;
import arquitectura.mips.util.Util;

import java.io.IOException;
import java.util.LinkedList;
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
    private DataCache dataCache0;
    private DataCache dataCache1;
    private InstructionCache instructionCache0;
    private InstructionCache instructionCache1;
    private List<Hilillo> hilillos;
    private List<Hilillo> hillillosBackup;

    public Simulation() {
        this.core0 = new Core(true, 4); //2 hilos
        this.core1 = new Core(false, 4); //1 hilo
        this.mainMemory = new MainMemory(8);
        this.dataCache0 = core0.getDataCache();
        this.instructionCache0 = core0.getInstructionCache();
        this.dataCache1 = core1.getDataCache();
        this.instructionCache1 = core1.getInstructionCache();
        this.hilillos = new LinkedList<Hilillo>();
        //this.hilillos = hililloList;
        //this.hillillosBackup = hililloList;
        this.masterThread = new MasterThread();
        this.thread1 = new Thread();
        this.thread2 = new Thread();
    }

    public void executeSimulation() throws IOException {

        Util util = new Util();
        util.readFiles();

        this.hilillos = util.getHilillos();
        this.instructionsMemory = util.getInstructionsMemory();
        Queue<Context> contextQueue = util.getContextQueue();

        //asignar hilillo a hilo de manera random
        asignHilillo(contextQueue);

    }

    public void asignHilillo(Queue<Context> contextQueue) { //asignar hilillo a hilo de nucleo
        Random random = new Random();
        int hililloRandom = random.nextInt(this.hilillos.size());
        int hililloRandom2 = random.nextInt(this.hilillos.size());
        while (hililloRandom == hililloRandom2) {
            hililloRandom = random.nextInt(this.hilillos.size());
            hililloRandom2 = random.nextInt(this.hilillos.size());
        }

        if (!contextQueue.isEmpty() && !this.hilillos.isEmpty()) {
            Hilillo hilillo1 = this.hilillos.get(hililloRandom);
            Context context = contextQueue.poll();
            hilillo1.setContext(context);
            this.thread1.setHilillo(hilillo1);

            Hilillo hilillo2 = this.hilillos.get(hililloRandom2);
            Context context2 = contextQueue.poll();
            hilillo2.setContext(context2);
            this.thread2.setHilillo(hilillo2);

            this.hilillos.remove(hilillo2);
            this.hilillos.remove(hilillo1);

        }


    }
}
