package arquitectura.mips;

import arquitectura.mips.memory.InstructionsMemory;
import arquitectura.mips.memory.MainMemory;

import java.util.LinkedList;
import java.util.List;

/**
 * Este es el hilo maestro que va a controlar el estado de los hilos.
 */
public class MasterThread implements Runnable {

    public List<EstadoHilillos> estadoHilillos;
    public InstructionsMemory instructionsMemory;
    public MainMemory mainMemory;
    public Thread thread1;
    public Thread thread2;

    public MasterThread() {
        instructionsMemory = InstructionsMemory.getInstructionsMemoryInstance();
        mainMemory = MainMemory.getMainMemoryInstance();
        estadoHilillos = new LinkedList<EstadoHilillos>();
    }

    @Override
    public void run() { //toda la logica va aqui
        this.thread1.run();
        this.thread2.run();
    }

    public List<EstadoHilillos> getEstadoHilillos() {
        return estadoHilillos;
    }

    public void setEstadoHilillos(List<EstadoHilillos> estadoHilillos) {
        this.estadoHilillos = estadoHilillos;
    }

    public void setThread1(Thread thread1) {
        this.thread1 = thread1;
    }

    public void setThread2(Thread thread2) {
        this.thread2 = thread2;
    }
}
