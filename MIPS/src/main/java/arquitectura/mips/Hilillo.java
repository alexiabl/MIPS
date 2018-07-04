package arquitectura.mips;

/**
 * Hilillo that will "execute" the instructions
 */
public class Hilillo {

    private Context context;
    private int quantum;
    private String name;

    public Hilillo(int quantum, String name) {
        this.quantum = quantum;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
