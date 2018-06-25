package arquitectura.mips.util;

import arquitectura.mips.Context;
import arquitectura.mips.Hilillo;
import arquitectura.mips.block.BlockInstructions;
import arquitectura.mips.memory.InstructionsMemory;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class Util {

    private InstructionsMemory instructionsMemory;
    private List<String> archivos;
    private Queue<Context> colaDeContexts;
    private Hilillo hilillo1;
    private Hilillo hilillo2;
    private Hilillo hilillo3;
    private Hilillo hilillo4;
    private Hilillo hilillo5;
    private List<Hilillo> hilillos;


    public Util() throws IOException {
        this.instructionsMemory = new InstructionsMemory();
        this.archivos = new LinkedList<String>();
        String file1 = "/Users/alexiaborchgrevink/Desktop/Arquitectura/Proyecto-MIPS/MIPS/MIPS/src/main/java/arquitectura/mips/util/1.txt";
        String file2 = "/Users/alexiaborchgrevink/Desktop/Arquitectura/Proyecto-MIPS/MIPS/MIPS/src/main/java/arquitectura/mips/util/2.txt";
        String file3 = "/Users/alexiaborchgrevink/Desktop/Arquitectura/Proyecto-MIPS/MIPS/MIPS/src/main/java/arquitectura/mips/util/3.txt";
        String file4 = "/Users/alexiaborchgrevink/Desktop/Arquitectura/Proyecto-MIPS/MIPS/MIPS/src/main/java/arquitectura/mips/util/4.txt";
        String file5 = "/Users/alexiaborchgrevink/Desktop/Arquitectura/Proyecto-MIPS/MIPS/MIPS/src/main/java/arquitectura/mips/util/2.txt";
        this.archivos.add(file1);
        this.archivos.add(file2);
        this.archivos.add(file3);
        this.archivos.add(file4);
        this.archivos.add(file5);
        this.colaDeContexts = new LinkedList<Context>();
        this.hilillos = new LinkedList<Hilillo>();
    }

    public void leerArchivos() { //insertar en memoria principal
        //cada linea por 4 que son las instucciones
        int contadorBloque = 0;
        int posicionActual = 0;
        for (int i = 0; i < this.archivos.size(); i++) {
            try {
                FileReader fileReader = new FileReader(this.archivos.get(i));
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                int cont = contadorBloque;
                Context context = new Context(posicionActual);
                Hilillo hilillo = new Hilillo(posicionActual);
                this.hilillos.add(hilillo);
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                    String[] split = line.split(" ");
                    BlockInstructions bloqueInstrucciones = new BlockInstructions();
                    bloqueInstrucciones.setNumBloque(contadorBloque);
                    ArrayList<Integer> instrucciones = new ArrayList<Integer>();
                    for (int s = 0; s < split.length; s++) {
                        instrucciones.add(new Integer(split[s]));
                    }
                    bloqueInstrucciones.setInstrucciones(instrucciones);
                    this.instructionsMemory.addBloqueInstruccion(bloqueInstrucciones);
                    cont++;
                    if ((cont % 4) == 0) {
                        contadorBloque++;
                    }
                    posicionActual++;
                }
                this.agregarAColaDeContextos(context);
                fileReader.close();
                System.out.println(stringBuffer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void agregarAColaDeContextos(Context c) {
        this.colaDeContexts.add(c);
    }

    public InstructionsMemory getInstructionsMemory() {
        return this.instructionsMemory;
    }

    public Queue<Context> getColaDeContexts() {
        return this.colaDeContexts;
    }

    public List<Hilillo> getHilillos() {
        return this.hilillos;
    }


}
