package arquitectura.mips.block;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/4/18.
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
