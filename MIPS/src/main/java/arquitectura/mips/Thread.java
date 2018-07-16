package arquitectura.mips;

import arquitectura.mips.block.BlockData;
import arquitectura.mips.block.BlockInstructions;
import arquitectura.mips.cache.BlockCache;
import arquitectura.mips.cache.DataCache;
import arquitectura.mips.cache.InstructionCache;
import arquitectura.mips.memory.InstructionsMemory;
import arquitectura.mips.memory.MainMemory;
import java.util.ArrayList;

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
    private MainMemory copyMemory = MainMemory.getMainMemoryInstance();

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
        int res = posicion / tamBloqueMemoria;
        return (int) Math.floor(res / tamBloqueMemoria);
    }

    //Calculate the word number
    int getNumeroDePalabra(int posicion, int tamBloqueMemoria, int bytesPalabra) {
        return (posicion / tamBloqueMemoria) % bytesPalabra;
    }

    //Calculate Position in Cache
    int getPosicionCache(int numDeBloque, int tamCache) {
        return numDeBloque % tamCache;
    }


    //Perform LW operation
    public void LW(){
        int blockNumber = getNumeroDeBloque(this.registers.get(IR.get(1)) + IR.get(3), 4);
        int word = getNumeroDePalabra(this.registers.get(IR.get(1)) + IR.get(3), 4, 4);
        int cachePosition = getPosicionCache(blockNumber, this.dataCache.getSize());

        if (this.dataCache.dataCacheLock.tryAcquire()) {
            if (lookupCache(blockNumber, cachePosition)) { //found in my cache
                if (this.dataCache.getCache().get(cachePosition).getEstado() == 'C' || this.dataCache.getCache().get(cachePosition).getEstado() == 'M') {
                    int wordVal = this.dataCache.getCache().get(cachePosition).getPalabras().get(word);
                    this.registers.set(IR.get(2), wordVal);
                    this.dataCache.dataCacheLock.release(); //suelto mi cache
                    //termina el LW
                } else { //Estado = 'I'
                    attemptLoadOnRemoteCache(blockNumber, word, cachePosition);
                    this.dataCache.dataCacheLock.release();
                }
            } //no encontro en mi cache
            else {
                attemptLoadOnRemoteCache(blockNumber, word, cachePosition);
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
                        //Si el bloque victima esta 'M' hay que guardarlo en memoria
                        if (this.dataCache.getCache().get(cachePosition).getEstado() == 'M') {
                            replaceBlockInMemory(blockCache); //cambio el bloque en memoria
                        }
                        blockCache.setPalabras(blockData.getWords()); //actualizar mi cache
                        blockCache.setEstado('C'); //cambia estado
                        blockCache.setEtiqueta(blockData.getNumBloque());
                        int wordVal = blockCache.getPalabras().get(word);
                        this.registers.set(IR.get(2), wordVal);
                    } else if (this.dataCache.getRemoteCache().getCache().get(cachePosition).getEstado() == 'C') {
                        BlockCache blockCache = this.dataCache.getCache().get(cachePosition);
                        BlockCache otherBlockCache = this.dataCache.getRemoteCache().getCache().get(cachePosition);
                        //Si el bloque victima esta 'M' hay que guardarlo en memoria
                        if (this.dataCache.getCache().get(cachePosition).getEstado() == 'M') {
                            //traer de memoria
                            for (int i = 0; i < 40; i++) {
                                Clock.executeBarrier(); //dura 40 ciclos trayendo de memoria
                            }
                            replaceBlockInMemory(blockCache); //cambio el bloque en memoria
                        }
                        int wordVal = otherBlockCache.getPalabras().get(word);
                        blockCache.setPalabras(otherBlockCache.getPalabras()); //sobreescribo en mi cache
                        this.dataCache.getCache().get(cachePosition).setEstado('C');  //dejo el estado en C
                        blockCache.setEtiqueta(otherBlockCache.getEtiqueta());
                        this.registers.set(IR.get(2), wordVal);
                    } else if (this.dataCache.getRemoteCache().getCache().get(cachePosition).getEstado() == 'M') {
                        BlockCache blockCache = this.dataCache.getCache().get(cachePosition);
                        BlockCache blockOtherCache = this.dataCache.getRemoteCache().getCache().get(cachePosition);
                        //Si el bloque victima esta 'M' hay que guardarlo en memoria
                        if (this.dataCache.getCache().get(cachePosition).getEstado() == 'M') {
                            //traer de memoria
                            for (int i = 0; i < 40; i++) {
                                Clock.executeBarrier(); //dura 40 ciclos trayendo de memoria
                            }
                            replaceBlockInMemory(blockCache); //cambio el bloque en memoria
                        }
                        blockOtherCache.setEstado('C');
                        replaceBlockInMemory(blockOtherCache); //cambio el bloque en memoria
                        blockCache.setPalabras(blockOtherCache.getPalabras());
                        this.dataCache.getCache().get(cachePosition).setEstado('C');
                        blockCache.setEtiqueta(blockOtherCache.getEtiqueta());
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
                    //Si el bloque victima esta 'M' hay que guardarlo en memoria
                    if (this.dataCache.getCache().get(cachePosition).getEstado() == 'M') {
                        replaceBlockInMemory(blockCache); //cambio el bloque en memoria
                    }
                    blockCache.setPalabras(blockData.getWords()); //actualizar mi cache
                    blockCache.setEstado('C'); //cambia estado
                    blockCache.setEtiqueta(blockData.getNumBloque());
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
                break;
            }
        }
    }

    public BlockData lookupBlockInMemory(int blockNumber) {
        BlockData blockData = null;
        for (BlockData block : MainMemory.getMainMemoryInstance().getBlocksMemory()) {
            if (block.getNumBloque() == blockNumber) {
                blockData = block;
                break;
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


    //Perform SW operation
    public void SW(){
        int blockNumber = getNumeroDeBloque(this.registers.get(IR.get(1)) + IR.get(3), 4);
        int word = getNumeroDePalabra(this.registers.get(IR.get(1)) + IR.get(3), 4, 4);
        int cachePosition = getPosicionCache(blockNumber, this.dataCache.getSize());

        if (this.dataCache.dataCacheLock.tryAcquire()) {
            if (lookupCache(blockNumber, cachePosition)) { //found in my cache
                if (this.dataCache.getCache().get(cachePosition).getEstado() == 'M') {
                    int wordVal = this.registers.get(IR.get(2));
                    this.dataCache.getCache().get(cachePosition).setPalabra(word, wordVal);
                    this.dataCache.dataCacheLock.release(); //suelto mi cache
                    //termina el SW
                } else if (this.dataCache.getCache().get(cachePosition).getEstado() == 'C') {
                    if (BusData.getBusDataInsance().lock.tryAcquire()) { //intenta adquirir el bus
                        if (this.dataCache.getRemoteCache().dataCacheLock.tryAcquire()) { //intento bloquear otra cache


                            if ((lookupRemoteCache(blockNumber, cachePosition))) { //La encontre en la otra cache con estado "C"


                                if (this.dataCache.getRemoteCache().getCache().get(cachePosition).getEstado() == 'C') {
                                    BlockCache blockCache = this.dataCache.getCache().get(cachePosition);
                                    BlockCache blockOtherCache = this.dataCache.getRemoteCache().getCache().get(cachePosition);

                                    blockCache.setEstado('M'); //cambia estado
                                    blockOtherCache.setEstado('I'); //cambia estado

                                    int wordVal = this.registers.get(IR.get(2));
                                    this.dataCache.getCache().get(cachePosition).setPalabra(word, wordVal);//hace el store
                                    this.dataCache.getCache().get(cachePosition).setEtiqueta(blockNumber);
                                    this.dataCache.dataCacheLock.release(); //suelto mi cache
                                } else {
                                    int wordVal = this.registers.get(IR.get(2));
                                    this.dataCache.getCache().get(cachePosition).setPalabra(word, wordVal);
                                    this.dataCache.getCache().get(cachePosition).setEstado('M');
                                    this.dataCache.dataCacheLock.release();
                                    BusData.getBusDataInsance().lock.release(); //suelto el bus
                                }


                            } else {

                                int wordVal = this.registers.get(IR.get(2));
                                this.dataCache.getCache().get(cachePosition).setPalabra(word, wordVal);
                                this.dataCache.getCache().get(cachePosition).setEstado('M');
                                this.dataCache.dataCacheLock.release();
                                BusData.getBusDataInsance().lock.release(); //suelto el bus

                            }
                            this.dataCache.getRemoteCache().dataCacheLock.release(); //suelto otra cache
                            BusData.getBusDataInsance().lock.release(); //suelto el bus
                        } else { //No pudo bloquear otra cache
                            BusData.getBusDataInsance().lock.release(); //suelto el bus
                            this.dataCache.dataCacheLock.release();
                            this.PC--;
                        }
                    } else { //No pudo tomar el bus
                        this.dataCache.dataCacheLock.release();
                        this.PC--;
                    }
                } else { //Estado "I"
                    attemptStoreOnRemoteCache(blockNumber, word, cachePosition);
                    this.dataCache.dataCacheLock.release();
                }
            } //No encontro en mi cache
            else {
                attemptStoreOnRemoteCache(blockNumber, word, cachePosition);
                this.dataCache.dataCacheLock.release();
            }
        } else {
            this.PC--;
        }
    }


    public void attemptStoreOnRemoteCache(int blockNumber, int word, int cachePosition) {
        if (BusData.getBusDataInsance().lock.tryAcquire()) { //logre adquirir el bus
            if (this.dataCache.getRemoteCache().dataCacheLock.tryAcquire()) { //bloqueo posicion en otra cache
                if (lookupRemoteCache(blockNumber, cachePosition)) { //la encontre en la otra cache
                    if (this.dataCache.getRemoteCache().getCache().get(cachePosition).getEstado() == 'I') {
                        //traer de memoria
                        for (int i = 0; i < 40; i++) {
                            Clock.executeBarrier(); //dura 40 ciclos trayendo de memoria
                        }
                        BlockData blockData = lookupBlockInMemory(blockNumber);
                        BlockCache blockCache = this.dataCache.getCache().get(cachePosition);
                        //Si el bloque victima esta 'M' hay que guardarlo en memoria
                        if (this.dataCache.getCache().get(cachePosition).getEstado() == 'M') {
                            replaceBlockInMemory(blockCache); //cambio el bloque en memoria
                        }
                        blockCache.setPalabras(blockData.getWords()); //actualizar mi cache
                        blockCache.setEstado('M'); //cambia estado
                        blockCache.setEtiqueta(blockData.getNumBloque());
                        int wordVal = this.registers.get(IR.get(2));//guarda en variable el valor a escribir
                        this.dataCache.getCache().get(cachePosition).setPalabra(word, wordVal);//hace el store
                        this.dataCache.getCache().get(cachePosition).setEtiqueta(blockData.getNumBloque());

                    } else if (this.dataCache.getRemoteCache().getCache().get(cachePosition).getEstado() == 'C') {
                        BlockCache blockCache = this.dataCache.getCache().get(cachePosition);
                        BlockCache blockOtherCache = this.dataCache.getRemoteCache().getCache().get(cachePosition);
                        //Si el bloque victima esta 'M' hay que guardarlo en memoria
                        if (this.dataCache.getCache().get(cachePosition).getEstado() == 'M') {
                            //traer de memoria
                            for (int i = 0; i < 40; i++) {
                                Clock.executeBarrier(); //dura 40 ciclos trayendo de memoria
                            }
                            replaceBlockInMemory(blockCache); //cambio el bloque en memoria
                        }
                        blockCache.setPalabras(blockOtherCache.getPalabras()); //actualizar mi cache
                        blockCache.setEstado('M'); //cambia estado
                        blockOtherCache.setEstado('I'); //cambia estado
                        blockCache.setEtiqueta(blockOtherCache.getEtiqueta());


                        int wordVal = this.registers.get(IR.get(2));//guarda en variable el valor a escribir
                        this.dataCache.getCache().get(cachePosition).setPalabra(word, wordVal);//hace el store
                        this.dataCache.getCache().get(cachePosition).setEstado('M');  //dejo el estado en M

                    } else if (this.dataCache.getRemoteCache().getCache().get(cachePosition).getEstado() == 'M') {
                        //traer de memoria
                        for (int i = 0; i < 40; i++) {
                            Clock.executeBarrier(); //dura 40 ciclos trayendo de memoria
                        }

                        BlockCache blockCache = this.dataCache.getCache().get(cachePosition);
                        BlockCache blockOtherCache = this.dataCache.getRemoteCache().getCache().get(cachePosition);
                        //Si el bloque victima esta 'M' hay que guardarlo en memoria
                        if (this.dataCache.getCache().get(cachePosition).getEstado() == 'M') {
                            replaceBlockInMemory(blockCache); //cambio el bloque en memoria
                        }
                        blockOtherCache.setEstado('I');
                        replaceBlockInMemory(blockOtherCache); //cambio el bloque en memoria
                        blockCache.setPalabras(blockOtherCache.getPalabras()); //actualizar mi cache
                        blockCache.setEstado('M'); //cambia estado
                        blockCache.setEtiqueta(blockOtherCache.getEtiqueta());

                        int wordVal = this.registers.get(IR.get(2));//guarda en variable el valor a escribir
                        this.dataCache.getCache().get(cachePosition).setPalabra(word, wordVal);//hace el store
                    }
                    this.dataCache.getRemoteCache().dataCacheLock.release();
                } else {
                    //traer de memoria
                    for (int i = 0; i < 40; i++) {
                        Clock.executeBarrier(); //dura 40 ciclos trayendo de memoria
                    }
                    BlockData blockData = lookupBlockInMemory(blockNumber);
                    BlockCache blockCache = this.dataCache.getCache().get(cachePosition);
                    if (this.dataCache.getCache().get(cachePosition).getEstado() == 'M') {
                        replaceBlockInMemory(blockCache); //cambio el bloque en memoria
                    }
                    blockCache.setPalabras(blockData.getWords()); //actualizar mi cache
                    blockCache.setEstado('M'); //cambia estado
                    blockCache.setEtiqueta(blockData.getNumBloque());

                    int wordVal = this.registers.get(IR.get(2));//guarda en variable el valor a escribir
                    blockCache.setPalabra(word, wordVal);
                    //this.dataCache.getCache().get(cachePosition).setPalabra(word, wordVal);//hace el store
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
                SW();
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
        this.registers.set(IR.get(2), resultado);
        Clock.executeBarrier();
    }

    public void DADD() {
        int resultado = this.registers.get(IR.get(1)) + this.registers.get(IR.get(2));
        this.registers.set(IR.get(3), resultado);
        Clock.executeBarrier();
    }

    public void DSUB() {
        int resultado = registers.get(IR.get(1)) - registers.get(IR.get(2));
        registers.set(IR.get(3), resultado);
        Clock.executeBarrier();
    }

    public void DMUL() {
        int resultado = registers.get(IR.get(1)) * registers.get(IR.get(2));
        registers.set(IR.get(3), resultado);
        Clock.executeBarrier();
    }

    public void DDIV() {
        if (registers.get(IR.get(2)) != 0) {
            int resultado = registers.get(IR.get(1)) / registers.get(IR.get(2));
            registers.set(IR.get(3), resultado);
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
        this.PC = PC + (IR.get(3) / 4);
        Clock.executeBarrier();
    }

    public void JR() {
        this.PC = registers.get(IR.get(1));
        Clock.executeBarrier();
    }

    public void FIN() {
        System.out.println("Thread finished");
        this.PC = this.hilillo.getContext().getPCfinal();
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
