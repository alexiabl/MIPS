package arquitectura.mips;

/**
 * Hilillo that will "execute" the instructions
 */
public class Hilillo {

    private Context context;
    private int quantum;

    public Hilillo(int quantum) {
        this.quantum = quantum;
    }

    public Hilillo() {
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public void removeQuantum() {
        this.quantum--;
    }
}
