package arquitectura.mips;

import java.util.LinkedList;
import java.util.List;

/**
 * Este es el hilo maestro que va a controlar el estado de los hilos.
 */
public class MasterThread implements Runnable {

    public List<EstadoHilillos> estadoHilillos;

    @Override
    public void run() {
        estadoHilillos = new LinkedList<EstadoHilillos>();
    }
}
