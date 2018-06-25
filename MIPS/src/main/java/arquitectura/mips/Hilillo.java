package arquitectura.mips;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class Hilillo {

    private Context context;
    private int quantum;

    public Hilillo(int quantum, int pc) {
        this.quantum = quantum;
        this.context = new Context(pc);
    }

    public Hilillo(int pci) {
        this.context = new Context(pci);
    }

    public void execute() {

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
