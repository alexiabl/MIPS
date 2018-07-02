package arquitectura;

import arquitectura.mips.*;
import arquitectura.mips.Thread;
import arquitectura.mips.cache.DataCache;
import arquitectura.mips.cache.InstructionCache;
import arquitectura.mips.memory.MainMemory;
import arquitectura.mips.util.Util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Simulation deals with the logic of our simulation. Here we initialize our processors, shared memories and the caches and run the simulation.
 */
public class Simulation {

    private Thread thread1;
    private Thread thread2;
    private Core core0;
    private Core core1;
    private DataCache dataCache0;
    private DataCache dataCache1;
    private InstructionCache instructionCache0;
    private InstructionCache instructionCache1;
    private List<Hilillo> hilillos;
    private List<Hilillo> hillillosBackup;
    private MainMemory mainMemory;

    public Simulation() {
        this.dataCache0 = new DataCache(8); //8 bloques
        this.dataCache1 = new DataCache(8); // 8 bloques
        this.dataCache0.setRemoteCache(dataCache1);
        this.dataCache1.setRemoteCache(dataCache0);

        this.instructionCache0 = new InstructionCache();
        this.instructionCache1 = new InstructionCache();
        this.instructionCache0.setRemoteInstructionCache(instructionCache1);
        this.instructionCache1.setRemoteInstructionCache(instructionCache0);

        this.mainMemory = new MainMemory(24); //24 bloques de memoria


        this.hilillos = new LinkedList<Hilillo>();

        this.core0 = new Core();
        this.core1 = new Core();

    }

    public void executeSimulation() throws IOException {

        Util util = new Util();
        util.readFiles();

        this.hilillos = util.getHilillos();

        Queue<Context> contextQueue = util.getContextQueue();

        this.thread1 = new Thread();
        this.thread2 = new Thread();
        this.thread1.setInstructionCache(this.instructionCache0);
        this.thread2.setInstructionCache(this.instructionCache1);
        this.thread1.setDataCache(this.dataCache0);
        this.thread2.setDataCache(this.dataCache1);

        core0.setThread(this.thread1);
        core1.setThread(this.thread2);

        while (!contextQueue.isEmpty()) {
            asignHilillo(contextQueue);
            //thread1.getHilillo().getContext().setRegisters(core0.getRegisters());
            this.thread1.run();
            //thread2.getHilillo().getContext().setRegisters(core1.getRegisters());
            this.thread2.run();
            for (int i = 0; i < this.thread1.getHilillo().getContext().getRegisters().size(); i++) {
                System.out.println("R[" + i + "] = " + this.thread1.getHilillo().getContext().getRegisters().get(i));
            }
            for (int i = 0; i < this.thread2.getHilillo().getContext().getRegisters().size(); i++) {
                System.out.println("R[" + i + "] = " + this.thread2.getHilillo().getContext().getRegisters().get(i));
            }

        }


        printSharedMemory();
        printDataCache();

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

    public void printSharedMemory() {
        System.out.println("Shared memory:");
        for (int i = 0; i < MainMemory.getMainMemoryInstance().getsize(); i++) {
            System.out.println("Block: " + MainMemory.getMainMemoryInstance().getBlocksMemory().get(i).getNumBloque());
            for (int j = 0; j < MainMemory.getMainMemoryInstance().getBlocksMemory().get(i).getWords().size(); j++) {
                System.out.println("    Words: " + MainMemory.getMainMemoryInstance().getBlocksMemory().get(i).getWords().get(j));
            }
        }
    }

    public void printDataCache() {
        System.out.println("Core 0 Data Cache:");
        for (int i = 0; i < this.dataCache0.getCache().size(); i++) {
            System.out.println("Position: " + i);
            System.out.println("    Block: " + this.dataCache0.getCache().get(i).getEtiqueta());
            System.out.println("    Status: " + this.dataCache0.getCache().get(i).getEstado());
            System.out.print("     Words: ");
            for (int j = 0; j < this.dataCache0.getCache().get(i).getPalabras().size(); j++) {
                System.out.print(this.dataCache0.getCache().get(i).getPalabras().get(j) + " ");
            }

        }
        System.out.println("Core 1 Data Cache:");
        for (int i = 0; i < this.dataCache1.getCache().size(); i++) {
            System.out.println("Position: " + i);
            System.out.println("    Block: " + this.dataCache1.getCache().get(i).getEtiqueta());
            System.out.println("    Status: " + this.dataCache1.getCache().get(i).getEstado());
            System.out.print("    Words: ");
            for (int j = 0; j < this.dataCache1.getCache().get(i).getPalabras().size(); j++) {
                System.out.print(this.dataCache1.getCache().get(i).getPalabras().get(j) + " ");
            }
        }
    }

    public Thread getThread1() {
        return thread1;
    }

    public void setThread1(Thread thread1) {
        this.thread1 = thread1;
    }

    public Thread getThread2() {
        return thread2;
    }

    public void setThread2(Thread thread2) {
        this.thread2 = thread2;
    }

    public Core getCore0() {
        return core0;
    }

    public void setCore0(Core core0) {
        this.core0 = core0;
    }

    public Core getCore1() {
        return core1;
    }

    public void setCore1(Core core1) {
        this.core1 = core1;
    }

    public DataCache getDataCache0() {
        return dataCache0;
    }

    public void setDataCache0(DataCache dataCache0) {
        this.dataCache0 = dataCache0;
    }

    public DataCache getDataCache1() {
        return dataCache1;
    }

    public void setDataCache1(DataCache dataCache1) {
        this.dataCache1 = dataCache1;
    }

    public InstructionCache getInstructionCache0() {
        return instructionCache0;
    }

    public void setInstructionCache0(InstructionCache instructionCache0) {
        this.instructionCache0 = instructionCache0;
    }

    public InstructionCache getInstructionCache1() {
        return instructionCache1;
    }

    public void setInstructionCache1(InstructionCache instructionCache1) {
        this.instructionCache1 = instructionCache1;
    }

    public List<Hilillo> getHilillos() {
        return hilillos;
    }

    public void setHilillos(List<Hilillo> hilillos) {
        this.hilillos = hilillos;
    }

    public List<Hilillo> getHillillosBackup() {
        return hillillosBackup;
    }

    public void setHillillosBackup(List<Hilillo> hillillosBackup) {
        this.hillillosBackup = hillillosBackup;
    }

}
