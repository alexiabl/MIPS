package arquitectura.mips;

/**
 * Cores can be in these states during the simulation, regarding the Caches they are accessing.
 */
public enum State {
    DATA_CACHE_FAIL, INSTRUCTION_CACHE_FAIL, RUNNING, WAITING, FINALIZED
}
