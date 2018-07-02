package arquitectura.mips.block;

/**
 * Abstract class to define Blocks in memory
 */
public abstract class Block {

    private int numBloque;

    public int getNumBloque() {
        return numBloque;
    }

    public void setNumBloque(int numBloque) {
        this.numBloque = numBloque;
    }

}
