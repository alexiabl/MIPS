package arquitectura.mips.block;

import java.util.ArrayList;

/**
 * Created by alexiaborchgrevink on 6/4/18.
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
