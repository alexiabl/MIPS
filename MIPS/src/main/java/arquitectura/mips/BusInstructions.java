package arquitectura.mips;

/**
 * Created by alexiaborchgrevink on 6/25/18.
 */
public class BusInstructions {

    private boolean blocked;

    public BusInstructions() {
        this.blocked = false;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
