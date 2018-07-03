package arquitectura.mips.block;

import java.util.ArrayList;

/**
 * Block of instructions that will hold the decoded instructions in our InstructionsMemory
 */
public class BlockInstructions extends Block {

    private ArrayList instructions;

    public BlockInstructions() {
        this.instructions = new ArrayList<Integer>();
    }

    public ArrayList<Integer> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList instructions) {
        this.instructions = instructions;
    }
}
