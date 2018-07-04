package arquitectura.mips;

import arquitectura.Simulation;

import java.io.IOException;


public class App
{


    /**
     * Main class that calls Simulation to execute
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        Simulation simulation = new Simulation();
        simulation.executeSimulation();

    }
}
