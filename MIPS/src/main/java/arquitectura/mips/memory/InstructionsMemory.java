package arquitectura.mips.memory;

import arquitectura.mips.block.BlockInstructions;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Instructions memory of our system. Uses singleton pattern so that the same InstructionsMemory isntance will be used on the entire program
 */
public class InstructionsMemory {

    private ArrayList<BlockInstructions> instructions;
    public static InstructionsMemory instructionsMemory;
    private Semaphore lock;


    public InstructionsMemory() {
        this.lock = new Semaphore(1);
        instructions = new ArrayList<BlockInstructions>();
        instructionsMemory = this;
    }

    public ArrayList<BlockInstructions> getInstructions() {
        return instructions;
    }

    public BlockInstructions getBlockInstructions(int pos) {
        return this.instructions.get(pos);
    }

    public void setInstructions(ArrayList<BlockInstructions> instructions) {
        this.instructions = instructions;
    }

    //Adds an instruction Block to our instructions memory
    public void addBloqueInstruccion(BlockInstructions bloqueinstructions) {
        this.instructions.add(bloqueinstructions);
    }

    //Creates new instance if null
    public static InstructionsMemory getInstructionsMemoryInstance() {
        if (instructionsMemory == null) {
            instructionsMemory = new InstructionsMemory();
        }
        return instructionsMemory;
    }

    public static void setInstructionsMemory(InstructionsMemory instructionsMemory1) {
        instructionsMemory = instructionsMemory1;
    }

    public Semaphore getLock() {
        return lock;
    }

}
