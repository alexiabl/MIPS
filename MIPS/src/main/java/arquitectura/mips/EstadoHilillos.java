package arquitectura.mips;

import arquitectura.mips.bloque.Bloque;

import java.util.UUID;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class EstadoHilillos {

    private UUID idHilillo;
    private Estado estado;
    private Bloque bloque;

    public EstadoHilillos(UUID id, Estado estado, Bloque b) {
        this.idHilillo = id;
        this.estado = estado;
        this.bloque = b;
    }

    public EstadoHilillos() {

    }

    public UUID getIdHilillo() {
        return idHilillo;
    }

    public void setIdHilillo(UUID idHilillo) {
        this.idHilillo = idHilillo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Bloque getBloque() {
        return bloque;
    }

    public void setBloque(Bloque bloque) {
        this.bloque = bloque;
    }
}
