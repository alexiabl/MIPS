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
 * Class used to read Hilillos files and assign PC, create context queue and insert to instructions memory.
 */
public class Util {

    private InstructionsMemory instructionsMemory;
    private List<String> archivos;
    private Queue<Context> contextQueue;
    private List<Hilillo> hilillos;


    public Util() throws IOException {
        this.instructionsMemory = new InstructionsMemory();
        this.archivos = new LinkedList<String>();

        File file = new File("src/main/java/arquitectura/mips/util/1.txt");
        String file1 = file.getAbsolutePath();
        file = new File("src/main/java/arquitectura/mips/util/2.txt");
        String file2 = file.getAbsolutePath();
        file = new File("src/main/java/arquitectura/mips/util/3.txt");
        String file3 = file.getAbsolutePath();
        file = new File("src/main/java/arquitectura/mips/util/4.txt");
        String file4 = file.getAbsolutePath();
        file = new File("src/main/java/arquitectura/mips/util/5.txt");
        String file5 = file.getAbsolutePath();

        this.archivos.add(file1);
        this.archivos.add(file2);
        this.archivos.add(file3);
        this.archivos.add(file4);
        this.archivos.add(file5);
        this.contextQueue = new LinkedList<Context>();
        this.hilillos = new LinkedList<Hilillo>();
    }

    //Reads Hilillos files and inserts into our Instructions Memory while creating the Context queue
    public void readFiles() {
        int blockCounter = 0;
        int currentPosition = 0;
        for (int i = 0; i < this.archivos.size(); i++) {
            try {
                FileReader fileReader = new FileReader(this.archivos.get(i));
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                int cont = blockCounter;
                Context context = null;
                if (currentPosition == 0) {
                    context = new Context(currentPosition);
                } else {
                    context = new Context(currentPosition + 1);
                }
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                    String[] split = line.split(" ");
                    BlockInstructions instructionBlock = new BlockInstructions();
                    instructionBlock.setNumBloque(blockCounter);
                    ArrayList<Integer> instrucciones = new ArrayList<Integer>();
                    for (int s = 0; s < split.length; s++) {
                        instrucciones.add(new Integer(split[s]));
                    }
                    instructionBlock.setInstructions(instrucciones);
                    InstructionsMemory.getInstructionsMemoryInstance().addBloqueInstruccion(instructionBlock);
                    cont++;
                    if ((cont % 4) == 0) {
                        blockCounter++;
                    }
                    currentPosition++;
                }
                context.setPCfinal(currentPosition);
                this.addToContextQueue(context);
                Hilillo hilillo = new Hilillo(40);
                this.hilillos.add(hilillo);
                fileReader.close();
                System.out.println(stringBuffer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void addToContextQueue(Context c) {
        this.contextQueue.add(c);
    }


    public Queue<Context> getContextQueue() {
        return this.contextQueue;
    }

    public List<Hilillo> getHilillos() {
        return this.hilillos;
    }


}
