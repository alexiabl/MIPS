package arquitectura.mips;

import arquitectura.mips.block.BlockData;
import arquitectura.mips.block.BlockInstructions;
import arquitectura.mips.cache.DataCache;
import arquitectura.mips.cache.InstructionCache;
import arquitectura.mips.memory.InstructionsMemory;
import arquitectura.mips.memory.MainMemory;
import sun.applet.Main;

import javax.xml.crypto.Data;
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
        int numeroBloque = getNumeroDeBloque(IR.get(1), 8);
        int numeroPalabra = getNumeroDePalabra(IR.get(1), 8, 4);
        int posicionCache = getPosicionCache(numeroBloque, this.dataCache.getSize());

        DataCache otherCache = dataCache.getRemoteCache(); //asi se obtiene la otra cache

        if (this.dataCache.dataCacheLock.tryAcquire()) { //siempre se bloquea la cache
            for (int i = 0; i < 40; i++) {
                Clock.executeBarrier();
            }
            if (dataCache.getCache().get(posicionCache).getEtiqueta() == numeroBloque) {
                if (dataCache.getCache().get(posicionCache).getEstado() == 'M' || dataCache.getCache().get(posicionCache).getEstado() == 'C') {
                    registers.set(IR.get(2), dataCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                    this.dataCache.dataCacheLock.release();//será??????
                } else {
                    BusData.getBusDataInsance().lock.tryAcquire();

                    if (otherCache.dataCacheLock.tryAcquire()) { //se bloquea la otra cache
                        for (int i = 0; i < 40; i++) {
                            Clock.executeBarrier();
                        }
                        if (otherCache.getCache().get(posicionCache).getEtiqueta() == numeroBloque) {

                            if (otherCache.getCache().get(posicionCache).getEstado() == 'C') {

                                registers.set(IR.get(2), otherCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                                this.dataCache.dataCacheLock.release();//será??????
                                otherCache.dataCacheLock.release();//será??????
                            } else if (otherCache.getCache().get(posicionCache).getEstado() == 'M') {

                                //MainMemory.getMainMemoryInstance().setDatosBloque(numeroBloque, otherCache.getCache().get(posicionCache).getPalabras());
                                //this.dataCache.setBloqueCache(posicionCache,otherCache.getBloqueCache(posicionCache));
                                this.dataCache.getCache().set(posicionCache, otherCache.getCache().get(posicionCache));
                                this.dataCache.getCache().get(posicionCache).setEstado('C');
                                otherCache.getCache().get(posicionCache).setEstado('C');
                                this.dataCache.dataCacheLock.release();//será??????
                                BusData.getBusDataInsance().lock.release();//creo que asi se hace

                                registers.set(IR.get(2), otherCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                                otherCache.dataCacheLock.release();//será??????
                            } else if (otherCache.getCache().get(posicionCache).getEstado() == 'I') {

                                otherCache.dataCacheLock.release();//será??????
                                //MainMemory.getMainMemoryInstance().setDatosBloque(IR.get(1), dataCache.getCache().get(posicionCache).getPalabras());
                                this.dataCache.getCache().get(posicionCache).setEstado('C');
                                BusData.getBusDataInsance().lock.release();//creo que asi se hace

                                registers.set(IR.get(2), dataCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                                this.dataCache.dataCacheLock.release();//será??????
                                //MainMemory.getMainMemoryInstance().setDatosBloque(IR.get(1), otherCache.getCache().get(posicionCache).getPalabras());
                                otherCache.getCache().get(posicionCache).setEstado('M');
                                BusData.getBusDataInsance().lock.release();//creo que asi se hace

                                registers.set(IR.get(2), otherCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                                otherCache.dataCacheLock.release();//será??????
                            }
                        } else {
                            //MainMemory.getMainMemoryInstance().setDatosBloque(numeroBloque, dataCache.getCache().get(posicionCache).getPalabras());
                            registers.set(IR.get(2), dataCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                            BusData.getBusDataInsance().lock.release();//creo que asi se hace
                            this.dataCache.dataCacheLock.release();//será??????
                        }
                    }
                }
            } else if (otherCache.getCache().get(posicionCache).getEtiqueta() == numeroBloque)
            //PARTE DE LA VICTIMA
            {
                if (otherCache.getCache().get(posicionCache).getEstado() == 'M') {
                    //MainMemory.getMainMemoryInstance().setDatosBloque(IR.get(1), otherCache.getCache().get(posicionCache).getPalabras());
                    otherCache.getCache().get(posicionCache).setEstado('I');
                    if (otherCache.dataCacheLock.tryAcquire()) {
                        for (int i = 0; i < 40; i++) {
                            Clock.executeBarrier();
                        }
                        if (otherCache.getCache().get(posicionCache).getEtiqueta() == numeroBloque) {

                            if (otherCache.getCache().get(posicionCache).getEstado() == 'C') {

                                registers.set(IR.get(2), otherCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                                this.dataCache.dataCacheLock.release();//será??????
                                otherCache.dataCacheLock.release();//será??????
                            } else if (otherCache.getCache().get(posicionCache).getEstado() == 'M') {
                                //REVISAR!!!
                                //MainMemory.getMainMemoryInstance().setDatosBloque(IR.get(1), otherCache.getCache().get(posicionCache).getPalabras());
                                //this.dataCache.setBloqueCache(posicionCache,otherCache.getBloqueCache(posicionCache));
                                this.dataCache.getCache().set(posicionCache, otherCache.getCache().get(posicionCache));
                                this.dataCache.getCache().get(posicionCache).setEstado('C');
                                otherCache.getCache().get(posicionCache).setEstado('C');
                                this.dataCache.dataCacheLock.release();//será??????
                                BusData.getBusDataInsance().lock.release();//creo que asi se hace

                                registers.set(IR.get(2), otherCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                                otherCache.dataCacheLock.release();//será??????
                            } else if (otherCache.getCache().get(posicionCache).getEstado() == 'I') {

                                this.dataCache.dataCacheLock.release();//será??????
                                //MainMemory.getMainMemoryInstance().setDatosBloque(IR.get(1), otherCache.getCache().get(posicionCache).getPalabras());
                                otherCache.getCache().get(posicionCache).setEstado('M');
                                BusData.getBusDataInsance().lock.release();//creo que asi se hace

                                registers.set(IR.get(2), otherCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                                otherCache.dataCacheLock.release();//será??????
                            }
                        } else {
                            //MainMemory.getMainMemoryInstance().setDatosBloque(IR.get(1), dataCache.getCache().get(posicionCache).getPalabras());
                            registers.set(IR.get(2), dataCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                            BusData.getBusDataInsance().lock.release();//creo que asi se hace
                            this.dataCache.dataCacheLock.release();//será??????
                        }
                    }
                }
            } else { //entra a memoria, fallo de cache y se carga el registro con el valor en memoria
                if (BusData.getBusDataInsance().lock.tryAcquire()) {
                    if (MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).getNumBloque() == numeroBloque) {
                        this.registers.set(IR.get(2), MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).getWords().get(numeroPalabra));
                        System.out.println("registro actualizado: " + this.registers.get(IR.get(2)));
                        this.dataCache.updateWord(posicionCache, numeroPalabra, this.registers.get(IR.get(2)));
                    }
                    BusData.getBusDataInsance().lock.release();
                }
            }
        } else {
            for (int i = 0; i < 40; i++) {
                Clock.executeBarrier();
            }
        }
        this.dataCache.dataCacheLock.release();
    }


    public void SW() {
        int numeroBloque = getNumeroDeBloque(IR.get(1), 8);
        int numeroPalabra = getNumeroDePalabra(IR.get(1), 8, 4);
        int posicionCache = getPosicionCache(numeroBloque, dataCache.getSize());

        DataCache otherCache = dataCache.getRemoteCache(); //asi se obtiene la otra cache

        if (this.dataCache.dataCacheLock.tryAcquire()) {
            for (int i = 0; i < 40; i++) {
                Clock.executeBarrier();
            }
            if (dataCache.getCache().get(posicionCache).getEtiqueta() == numeroBloque) {
                if (dataCache.getCache().get(posicionCache).getEstado() == 'M') {
                    dataCache.getCache().get(posicionCache).setPalabra(numeroPalabra, registers.get(IR.get(2)));
                    this.dataCache.dataCacheLock.release(); //
                    //AVANZA EL CICLO DEL RELOJ!!!
                } else if (dataCache.getCache().get(posicionCache).getEstado() == 'C') {
                    BusData.getBusDataInsance().lock.tryAcquire();
                    if (otherCache.dataCacheLock.tryAcquire()) { //se bloquea la otra cache
                        for (int i = 0; i < 40; i++) {
                            Clock.executeBarrier();
                        }
                        if (otherCache.getCache().get(posicionCache).getEtiqueta() == numeroBloque && otherCache.getCache().get(posicionCache).getEstado() == 'C') {
                            otherCache.getCache().get(posicionCache).setEstado('I');
                            this.dataCache.getCache().get(posicionCache).setEstado('M');
                            //MainMemory.getMainMemoryInstance().setDatosBloque(IR.get(1), this.dataCache.getCache().get(posicionCache).getPalabras());
                            MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).setWords(otherCache.getCache().get(posicionCache).getPalabras());
                            this.dataCache.dataCacheLock.release();//será??????
                            //AVANZA EL CICLO DEL RELOJ!!!
                        }
                        BusData.getBusDataInsance().lock.release();//creo que asi se hace
                    }
                } else if (dataCache.getCache().get(posicionCache).getEstado() == 'I') {
                    BusData.getBusDataInsance().lock.tryAcquire();
                    if (otherCache.dataCacheLock.tryAcquire()) {
                        for (int i = 0; i < 40; i++) {
                            Clock.executeBarrier();
                        }
                        if (otherCache.getCache().get(posicionCache).getEtiqueta() == numeroBloque) {
                            if (otherCache.getCache().get(posicionCache).getEstado() == 'C') {
                                otherCache.getCache().get(posicionCache).setEstado('I');
                                otherCache.dataCacheLock.release();

                                BusData.getBusDataInsance().lock.release();//creo que asi se hace
                                dataCache.setBloqueCache(posicionCache, MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).getWords());
                                this.dataCache.getCache().get(posicionCache).setEstado('C');
                                //AVANZA EL CICLO DEL RELOJ!!!

                            } else if (otherCache.getCache().get(posicionCache).getEstado() == 'M') {
                                //MainMemory.getMainMemoryInstance().setDatosBloque(IR.get(1), otherCache.getCache().get(posicionCache).getPalabras());
                                MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).setWords(otherCache.getCache().get(posicionCache).getPalabras());
                                dataCache.setBloqueCache(posicionCache, MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).getWords());
                                otherCache.getCache().get(posicionCache).setEstado('I');
                                otherCache.dataCacheLock.release();
                                BusData.getBusDataInsance().lock.release();//creo que asi se hace
                                dataCache.getCache().get(posicionCache).setPalabra(numeroPalabra, registers.get(IR.get(2)));
                                dataCache.getCache().get(posicionCache).setEstado('M');
                                this.dataCache.dataCacheLock.release();//será??????
                            }
                        }
                        //falta else para ir a traerlo de memoria
                    }
                }
            } else if (otherCache.getCache().get(posicionCache).getEtiqueta() == numeroBloque) {
                if (otherCache.getCache().get(posicionCache).getEtiqueta() == numeroBloque) {
                    if (otherCache.dataCacheLock.tryAcquire()) {
                        for (int i = 0; i < 40; i++) {
                            Clock.executeBarrier();
                        }
                        if (otherCache.getCache().get(posicionCache).getEstado() == 'M') {
                            //MainMemory.getMainMemoryInstance().setDatosBloque(IR.get(1), otherCache.getCache().get(posicionCache).getPalabras());
                            MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).setWords(otherCache.getCache().get(posicionCache).getPalabras());
                            dataCache.setBloqueCache(posicionCache, MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).getWords());
                            this.getDataCache().getCache().get(posicionCache).setEstado('I');
                            BusData.getBusDataInsance().lock.release();//creo que asi se hace
                            this.dataCache.dataCacheLock.release();//será??????

                            otherCache.getCache().get(posicionCache).setPalabra(numeroPalabra, registers.get(IR.get(2)));
                            otherCache.getCache().get(posicionCache).setEstado('M');
                            otherCache.dataCacheLock.release();
                            //AVANZA EL RELOJ!!!
                        } else if (otherCache.getCache().get(posicionCache).getEstado() == 'C') {
                            otherCache.getCache().get(posicionCache).setEstado('I');
                            MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque);
                            otherCache.dataCacheLock.release();
                            //AVANZA EL RELOJ!!!
                        } else if (otherCache.getCache().get(posicionCache).getEstado() == 'I') {
                            MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque);
                            otherCache.dataCacheLock.release();
                            //AVANZA EL RELOJ!!!
                        }
                    } else {
                        dataCache.setBloqueCache(posicionCache, MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).getWords());
                        dataCache.getCache().get(posicionCache).setEstado('C');
                        otherCache.dataCacheLock.release();
                        BusData.getBusDataInsance().lock.release();//creo que asi se hace
                        //AVANZA EL RELOJ!!!
                    }
                } else {
                    if (otherCache.getCache().get(posicionCache).getEstado() == 'M') {
                        //MainMemory.getMainMemoryInstance().setDatosBloque(IR.get(1), otherCache.getCache().get(posicionCache).getPalabras());
                        MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).setWords(otherCache.getCache().get(posicionCache).getPalabras());
                        otherCache.getCache().get(posicionCache).setEstado('I');
                        if (otherCache.dataCacheLock.tryAcquire()) {
                            for (int i = 0; i < 40; i++) {
                                Clock.executeBarrier();
                            }
                            if (otherCache.getCache().get(posicionCache).getEstado() == 'M') {
                                //MainMemory.getMainMemoryInstance().setDatosBloque(IR.get(1), otherCache.getCache().get(posicionCache).getPalabras());
                                MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).setWords(otherCache.getCache().get(posicionCache).getPalabras());
                                dataCache.setBloqueCache(posicionCache, MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).getWords());
                                this.getDataCache().getCache().get(posicionCache).setEstado('I');
                                BusData.getBusDataInsance().lock.release();//creo que asi se hace
                                this.dataCache.dataCacheLock.release();//será??????

                                otherCache.getCache().get(posicionCache).setPalabra(numeroPalabra, registers.get(IR.get(2)));
                                otherCache.getCache().get(posicionCache).setEstado('M');
                                otherCache.dataCacheLock.release();
                                //AVANZA EL RELOJ!!!
                            } else if (otherCache.getCache().get(posicionCache).getEstado() == 'C') {
                                otherCache.getCache().get(posicionCache).setEstado('I');
                                MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque); //esto no hace nada
                                otherCache.dataCacheLock.release();
                                //AVANZA EL RELOJ!!!
                            } else if (otherCache.getCache().get(posicionCache).getEstado() == 'I') {
                                MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque);
                                otherCache.dataCacheLock.release();
                                //AVANZA EL RELOJ!!!
                            }
                        } else {
                            dataCache.setBloqueCache(posicionCache, MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).getWords());
                            dataCache.getCache().get(posicionCache).setEstado('C');
                            otherCache.dataCacheLock.release();
                            BusData.getBusDataInsance().lock.release();//creo que asi se hace
                            //AVANZA EL RELOJ!!!
                        }
                    }
                }
            } else { //se va a memoria
                if (BusData.getBusDataInsance().lock.tryAcquire()) {
                    if (MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).getNumBloque() == numeroBloque) {
                        MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).getWords().set(numeroPalabra, this.registers.get(IR.get(2).intValue()));
                    }
                    this.dataCache.setBloqueCache(posicionCache, MainMemory.getMainMemoryInstance().getBlocksMemory().get(numeroBloque).getWords());
                    this.dataCache.getCache().get(posicionCache).setEstado('M');
                    this.dataCache.getCache().get(posicionCache).setEtiqueta(numeroBloque);

                    BusData.getBusDataInsance().lock.release();
                }
            }
        } else {
            //AUMENTAR CICLO DE RELOJ!!!
            for (int i = 0; i < 40; i++) {
                Clock.executeBarrier();
            }
        }
        this.dataCache.dataCacheLock.release();
        Clock.executeBarrier();
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
            PC = PC + 4 * IR.get(3);
            Clock.executeBarrier();
        }
    }

    public void BNEZ() {
        if (registers.get(IR.get(1)) != 0) {
            PC = PC + 4 * IR.get(3);
            Clock.executeBarrier();
        }
    }

    public void JAL() {
        registers.set(31, PC);
        PC = PC + IR.get(3);
        Clock.executeBarrier();
    }

    public void JR() {
        PC = registers.get(IR.get(1));
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
        int count = this.hilillo.getContext().getPCinitial();
        while (count < endIR) {
            BlockInstructions blockInstructions = InstructionsMemory.getInstructionsMemoryInstance().getBlockInstructions(count);
            this.IR = blockInstructions.getInstructions();
            this.executeInstruction(blockInstructions.getInstructions());
            count++;
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
