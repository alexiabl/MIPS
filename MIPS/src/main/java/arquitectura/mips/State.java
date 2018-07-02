package arquitectura.mips;

/**
 * Threads can be in these states during the simulation
 */
public enum State {
    DATA_CACHE_FAIL, INSTRUCTION_CACHE_FAIL, RUNNING, WAITING, FINALIZED
}
