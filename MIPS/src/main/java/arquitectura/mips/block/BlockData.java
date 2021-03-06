package arquitectura.mips.block;

import java.util.ArrayList;

/**
 * Block of data that Main memory wil hold to store the values of the words.
 */
public class BlockData extends Block {

    private ArrayList<Integer> words;

    public BlockData() {
        this.words = new ArrayList<Integer>(4);
    }

    public ArrayList<Integer> getWords() {
        return words;
    }

    public void setWords(ArrayList<Integer> words) {
        this.words = words;
    }

}
