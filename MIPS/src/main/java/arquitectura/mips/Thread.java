package arquitectura.mips;

import arquitectura.mips.block.Block;
import arquitectura.mips.block.BlockData;
import arquitectura.mips.block.BlockInstructions;
import arquitectura.mips.cache.BlockCache;
import arquitectura.mips.cache.DataCache;
import arquitectura.mips.cache.InstructionCache;
import arquitectura.mips.memory.InstructionsMemory;
import arquitectura.mips.memory.MainMemory;
import sun.applet.Main;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class can be thought of as the Core that will execute instructions. The Core class executes the assigned Thread.
 */
public class Thread extends java.lang.Thread {

    private ArrayList<Integer> IR;
    private ArrayList<Integer> registers = new ArrayList<Integer>(32);
    private int PC;
    private Hilillo hilillo;
    private DataCache dataCache;
    private InstructionCache instructionCache;
    private int cycles;

    public Thread(DataCache dataCache, InstructionCache instructionCache) {
        this.dataCache = dataCache;
        this.instructionCache = instructionCache;
        this.cycles = 0;
    }

    public Thread() {
        this.cycles = 0;
    }

    public void setIR(ArrayList<Integer> p) {
        this.IR = p;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public void setHilillo(Hilillo hilillo) {
        this.hilillo = hilillo;
        this.PC = hilillo.getContext().getPCinitial();
        this.registers = hilillo.getContext().getRegisters();
    }


    //Calculate the Block number
    int getNumeroDeBloque(int posicion, int tamBloqueMemoria) {
        return posicion / tamBloqueMemoria;
    }

    //Calculate the word number
    int getNumeroDePalabra(int posicion, int tamBloqueMemoria, int bytesPalabra) {
        return (posicion % tamBloqueMemoria) / bytesPalabra;
    }

    //Calculate Position in Cache
    int getPosicionCache(int numDeBloque, int tamCache) {
        return numDeBloque % tamCache;
    }


    //Perform LW operation
    public void LW(){
        int blockNumber = getNumeroDeBloque(IR.get(1), 4);
        int word = getNumeroDePalabra(IR.get(1), 4, 4);
        int cachePosition = getPosicionCache(blockNumber, this.dataCache.getSize());

        if (this.dataCache.dataCacheLock.tryAcquire()) {
            System.out.println("Acquired local cache with - " + this.hilillo.getName());
            if (lookupCache(blockNumber, cachePosition)) { //found in my cache
                if (this.dataCache.getCache().get(cachePosition).getEstado() == 'C' || this.dataCache.getCache().get(cachePosition).getEstado() == 'M') {
                    int wordVal = this.dataCache.getCache().get(cachePosition).getPalabras().get(word);
                    this.registers.set(IR.get(2), wordVal);
                    System.out.println("Released local cache with - " + this.hilillo.getName());
                    this.dataCache.dataCacheLock.release(); //suelto mi cache
                    //termina el LW
                } else { //Estado = 'I'
                    attemptLoadOnRemoteCache(blockNumber, word, cachePosition);
                    System.out.println("Released local cache with - " + this.hilillo.getName());
                    this.dataCache.dataCacheLock.release();
                }
            } //no encontro en mi cache
            else {
                attemptLoadOnRemoteCache(blockNumber, word, cachePosition);
                System.out.println("Released local cache with - " + this.hilillo.getName());
                this.dataCache.dataCacheLock.release();
            }
        } else {
            this.PC--;
        }

    }

    public void attemptLoadOnRemoteCache(int blockNumber, int word, int cachePosition) {
        if (BusData.getBusDataInsance().lock.tryAcquire()) { //logre adquirir el bus
            if (this.dataCache.getRemoteCache().dataCacheLock.tryAcquire()) {
                if (lookupRemoteCache(blockNumber, cachePosition)) { //la encontre en la otra cache
                    //int wordVal = this.dataCache.getRemoteCache().getCache().get(cachePosition).getPalabras().get(word);
                    //this.registers.set(IR.get(2),wordVal); //la asigno al registro
                    if (this.dataCache.getRemoteCache().getCache().get(cachePosition).getEstado() == 'I') {
                        //traer de memoria
                        for (int i = 0; i < 40; i++) {
                            Clock.executeBarrier(); //dura 40 ciclos trayendo de memoria
                        }
                        BlockData blockData = lookupBlockInMemory(blockNumber);
                        BlockCache blockCache = this.dataCache.getCache().get(cachePosition);
                        blockCache.setPalabras(blockData.getWords()); //actualizar mi cache
                        blockCache.setEstado('M'); //cambia estado
                        int wordVal = blockCache.getPalabras().get(word);
                        this.registers.set(IR.get(2), wordVal);
                    } else if (this.dataCache.getRemoteCache().getCache().get(cachePosition).getEstado() == 'C') {
                        int wordVal = this.dataCache.getRemoteCache().getCache().get(cachePosition).getPalabras().get(word);
                        this.dataCache.getCache().get(cachePosition).getPalabras().set(word, wordVal); //sobreescribo en mi cache
                        this.dataCache.getCache().get(cachePosition).setEstado('C');  //dejo el estado en C
                        this.registers.set(IR.get(2), wordVal);
                    } else if (this.dataCache.getRemoteCache().getCache().get(cachePosition).getEstado() == 'M') {
                        BlockCache blockCache = this.dataCache.getRemoteCache().getCache().get(cachePosition);
                        blockCache.setEstado('C');
                        replaceBlockInMemory(blockCache); //cambio el bloque en memoria
                        this.dataCache.getCache().set(cachePosition, blockCache); //reemplazo en mi cache
                        this.dataCache.getCache().get(cachePosition).setEstado('C');
                        int wordVal = this.dataCache.getCache().get(cachePosition).getPalabras().get(word);
                        this.registers.set(IR.get(2), wordVal);
                    }
                    this.dataCache.getRemoteCache().dataCacheLock.release();
                } else {
                    //traer de memoria
                    for (int i = 0; i < 40; i++) {
                        Clock.executeBarrier(); //dura 40 ciclos trayendo de memoria
                    }
                    BlockData blockData = lookupBlockInMemory(blockNumber);
                    BlockCache blockCache = this.dataCache.getCache().get(cachePosition);
                    blockCache.setPalabras(blockData.getWords()); //actualizar mi cache
                    blockCache.setEstado('M'); //cambia estado
                    int wordVal = blockCache.getPalabras().get(word);
                    this.registers.set(IR.get(2), wordVal);
                    this.dataCache.getRemoteCache().dataCacheLock.release();
                }
            } else {
                this.PC--;
            }
            BusData.getBusDataInsance().lock.release(); //suelto el bus
        } else {
            this.PC--;
        }
    }

    public void replaceBlockInMemory(BlockCache blockCache) {
        for (BlockData block : MainMemory.getMainMemoryInstance().getBlocksMemory()) {
            if (block.getNumBloque() == blockCache.getEtiqueta()) {
                MainMemory.getMainMemoryInstance().setBlock(block.getNumBloque(), blockCache.getPalabras());
            }
        }
    }

    public BlockData lookupBlockInMemory(int blockNumber) {
        BlockData blockData = null;
        for (BlockData block : MainMemory.getMainMemoryInstance().getBlocksMemory()) {
            if (block.getNumBloque() == blockNumber) {
                blockData = block;
            }
        }
        return blockData;
    }

    public boolean lookupCache(int block, int cachePosition) {
        boolean found = false;
        BlockCache blockCache = this.dataCache.getCache().get(cachePosition);
        if (blockCache.getEtiqueta() == block) {
            found = true;
        }
        return found;
    }

    public boolean lookupRemoteCache(int block, int cachePosition) {
        boolean found = false;
        BlockCache blockCache = this.dataCache.getRemoteCache().getCache().get(cachePosition);
        if (blockCache.getEtiqueta() == block) {
            found = true;
        }
        return found;
    }


    public void executeInstruction(ArrayList<Integer> instruction) { //despues de cada instruccion se le quita quantum
        switch (instruction.get(0)) {
            case 8: //DADDI
                DADDI();
                this.hilillo.removeQuantum();
                break;
            case 32:
                DADD();
                this.hilillo.removeQuantum();
                break;
            case 34:
                DSUB();
                this.hilillo.removeQuantum();
                break;
            case 12:
                DMUL();
                this.hilillo.removeQuantum();
                break;
            case 14:
                DDIV();
                this.hilillo.removeQuantum();
                break;
            case 4:
                BEQZ();
                this.hilillo.removeQuantum();
                break;
            case 5:
                BNEZ();
                this.hilillo.removeQuantum();
                break;
            case 3:
                JAL();
                this.hilillo.removeQuantum();
                break;
            case 2:
                JR();
                this.hilillo.removeQuantum();
                break;
            case 35:
                LW();
                this.hilillo.removeQuantum();
                break;
            case 43:
                //SW();
                this.hilillo.removeQuantum();
                break;
            case 63:
                FIN();
                this.hilillo.removeQuantum();
                break;
        }
    }


    public void DADDI() {
        int resultado = this.registers.get(IR.get(1)) + IR.get(3);
        System.out.println(this.hilillo.getName() + "-" + " resultado=" + resultado);
        this.registers.set(IR.get(2), resultado);
        System.out.println("Registro[" + IR.get(2) + "]:" + this.registers.get(IR.get(2)));
        Clock.executeBarrier();
    }

    public void DADD() {
        int resultado = this.registers.get(IR.get(1)) + this.registers.get(IR.get(2));
        System.out.println(this.hilillo.getName() + "-" + " resultado=" + resultado);
        this.registers.set(IR.get(3), resultado);
        System.out.println("Registro[" + IR.get(3) + "]" + this.registers.get(IR.get(3)));
        Clock.executeBarrier();
    }

    public void DSUB() {
        int resultado = registers.get(IR.get(1)) - registers.get(IR.get(2));
        System.out.println(this.hilillo.getName() + "-" + " resultado=" + resultado);
        registers.set(IR.get(3), resultado);
        System.out.println("Registro:" + IR.get(3) + "=" + this.registers.get(IR.get(3)));
        Clock.executeBarrier();
    }

    public void DMUL() {
        int resultado = registers.get(IR.get(1)) * registers.get(IR.get(2));
        System.out.println(this.hilillo.getName() + "-" + " resultado=" + resultado);
        registers.set(IR.get(3), resultado);
        System.out.println("Registro:" + IR.get(3) + "=" + this.registers.get(IR.get(3)));
        Clock.executeBarrier();
    }

    public void DDIV() {
        if (registers.get(IR.get(2)) != 0) {
            int resultado = registers.get(IR.get(1)) / registers.get(IR.get(2));
            System.out.println(this.hilillo.getName() + "-" + " resultado=" + resultado);
            registers.set(IR.get(3), resultado);
            System.out.println("Registro:" + IR.get(3) + "=" + this.registers.get(IR.get(3)));
        } else {
            System.out.println("Advertencia! Está dividiendo entre 0.");
        }
        Clock.executeBarrier();

    }

    public void BEQZ() {
        if (registers.get(IR.get(1)) == 0) {
            this.PC = PC + IR.get(3);
            Clock.executeBarrier();
        }
    }

    public void BNEZ() {
        if (registers.get(IR.get(1)) != 0) {
            this.PC = PC + IR.get(3);
            Clock.executeBarrier();
        }
    }

    public void JAL() {
        registers.set(31, PC);
        this.PC = PC + IR.get(3);
        Clock.executeBarrier();
    }

    public void JR() {
        this.PC = registers.get(IR.get(1));
        Clock.executeBarrier();
    }

    public void FIN() {
        System.out.println("Thread finished");
        printRegisters();
    }

    //Thread method that executes the instructions
    @Override
    public void run() {
        System.out.println(this.getName() + " running hilillo: " + this.hilillo.getName());
        int endIR = this.hilillo.getContext().getPCfinal();
        while (this.PC < endIR) {
            BlockInstructions blockInstructions = InstructionsMemory.getInstructionsMemoryInstance().getBlockInstructions(this.PC);
            this.IR = blockInstructions.getInstructions();
            this.executeInstruction(blockInstructions.getInstructions());
            this.PC++;
        }
        System.out.println(this.getName() + " finalized");
        printRegisters();
        Clock.reduceCoreCount();
        Clock.executeBarrier();
        System.out.println("Cycles = " + Clock.getCycle());
    }

    //Print the current thread's registers
    public void printRegisters() {
        for (int i = 0; i < this.registers.size(); i++) {
            System.out.println("R[" + i + "] = " + registers.get(i));
        }
    }

    public Hilillo getHilillo() {
        return this.hilillo;
    }

    public DataCache getDataCache() {
        return this.dataCache;
    }

    public void setDataCache(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    public InstructionCache getInstructionCache() {
        return instructionCache;
    }

    public void setInstructionCache(InstructionCache instructionCache) {
        this.instructionCache = instructionCache;
    }
}
