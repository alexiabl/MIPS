package arquitectura.mips;

import arquitectura.mips.block.Block;

import java.util.UUID;

/**
 * Hilillos state to determine if a Block can be accessed.
 */
public class EstadoHilillos {

    private UUID idHilillo;
    private State state;
    private Block block;

    public EstadoHilillos(UUID id, State state, Block b) {
        this.idHilillo = id;
        this.state = state;
        this.block = b;
    }

    public EstadoHilillos() {

    }

    public UUID getIdHilillo() {
        return idHilillo;
    }

    public void setIdHilillo(UUID idHilillo) {
        this.idHilillo = idHilillo;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
