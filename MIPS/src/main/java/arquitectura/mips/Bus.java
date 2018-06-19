package arquitectura.mips;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class Bus {

    private boolean bloqueado;

    public Bus() {
        this.bloqueado = false;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }
}
