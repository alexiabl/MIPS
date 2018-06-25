package arquitectura.mips;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class BusData {

    private boolean blocked;

    public BusData() {
        this.blocked = false;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
