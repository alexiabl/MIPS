package arquitectura.mips;

import arquitectura.mips.block.BlockInstructions;
import arquitectura.mips.cache.DataCache;
import arquitectura.mips.cache.InstructionCache;
import arquitectura.mips.memory.InstructionsMemory;
import arquitectura.mips.memory.MainMemory;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class Thread implements Runnable { //corre el hilillo

//tiempo de ejecucion de cada hilillo

    private ArrayList<Integer> IR;
    private ArrayList<Integer> registers = new ArrayList<Integer>(32);
    private int PC;
    private Hilillo hilillo;
    private DataCache dataCache;
    private InstructionCache instructionCache;

    public Thread(DataCache dataCache, InstructionCache instructionCache) {
        this.dataCache = dataCache;
        this.instructionCache = instructionCache;
    }

    public Thread() {

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

    int getNumeroDeBloque(int posicion, int tamBloqueMemoria) {
        return posicion / tamBloqueMemoria;
    }

    int getNumeroDePalabra(int posicion, int tamBloqueMemoria, int bytesPalabra) {
        return (posicion % tamBloqueMemoria) / bytesPalabra;
    }

    int getPosicionCache(int numDeBloque, int tamCache) {
        return numDeBloque % tamCache;
    }

    public void LW(){
        int numeroBloque = getNumeroDeBloque(IR.get(1), 16);
        int numeroPalabra = getNumeroDePalabra(IR.get(1), 16, 4);
        int posicionCache = getPosicionCache(numeroBloque, this.dataCache.getSize());

        if (this.dataCache.dataCacheLock.tryAcquire()) { //siempre se bloquea la cache

            if (dataCache.getCache().get(posicionCache).getEtiqueta() == numeroBloque) {
                if (dataCache.getCache().get(posicionCache).getEstado() == 'M' || dataCache.getCache().get(posicionCache).getEstado() == 'C') {
                    registers.set(IR.get(2), dataCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                    this.dataCache.dataCacheLock.release();//será??????
                } else {
                    //BLOQUEAR BUS!!!!!!!!!!!
                    DataCache otherCache = dataCache.getRemoteCache(); //asi se obtiene la otra cache
                    if (otherCache.dataCacheLock.tryAcquire()) { //se bloquea la otra cache
                        if (otherCache.getCache().get(posicionCache).getEtiqueta() == numeroBloque) {


                            if (otherCache.getCache().get(posicionCache).getEstado() == 'C') {

                                registers.set(IR.get(2), otherCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                                this.dataCache.dataCacheLock.release();//será??????
                                otherCache.dataCacheLock.release();//será??????
                            } else if (otherCache.getCache().get(posicionCache).getEstado() == 'M') {

                                MainMemory.getMainMemoryInstance().setDatosBloque(IR.get(1), otherCache.getCache().get(posicionCache).getPalabras());
                                //this.dataCache.setBloqueCache(posicionCache,otherCache.getBloqueCache(posicionCache));
                                this.dataCache.getCache().set(posicionCache, otherCache.getCache().get(posicionCache));
                                this.dataCache.getCache().get(posicionCache).setEstado('C');
                                otherCache.getCache().get(posicionCache).setEstado('C');
                                this.dataCache.dataCacheLock.release();//será??????
                                //LIBERAR BUS !!!

                                registers.set(IR.get(2), otherCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                                otherCache.dataCacheLock.release();//será??????
                            } else if (otherCache.getCache().get(posicionCache).getEstado() == 'I') {

                                otherCache.dataCacheLock.release();//será??????
                                MainMemory.getMainMemoryInstance().setDatosBloque(IR.get(1), dataCache.getCache().get(posicionCache).getPalabras());
                                this.dataCache.getCache().get(posicionCache).setEstado('C');
                                //LIBERAR BUS !!!

                                registers.set(IR.get(2), dataCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                                this.dataCache.dataCacheLock.release();//será??????
                            }
                        } else {
                            MainMemory.getMainMemoryInstance().setDatosBloque(IR.get(1), dataCache.getCache().get(posicionCache).getPalabras());
                            registers.set(IR.get(2), dataCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                            //LIBERAR BUS !!!
                            this.dataCache.dataCacheLock.release();//será??????
                        }
                    }
                }
            } else {
                //PARTE DE LA VICTIMA
            }
        } else {
            //NO SE BLOQUEO LA POSICION... AUMENTAR CICLO DE RELOJ
        }
    }

    public void LW2() {
        int numeroBloque = getNumeroDeBloque(IR.get(1), 16);
        int numeroPalabra = getNumeroDePalabra(IR.get(1), 16, 4);
        int posicionCache = getPosicionCache(numeroBloque, this.dataCache.getSize());

        if (this.dataCache.dataCacheLock.tryAcquire()) { //siempre se bloquea la cache
            if (dataCache.getCache().get(posicionCache).getEtiqueta() == numeroBloque) {
                if (dataCache.getCache().get(posicionCache).getEstado() == 'M' || dataCache.getCache().get(posicionCache).getEstado() == 'C') {
                    registers.set(IR.get(2), dataCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                    //desbloquear posicion
                } else {
                    //bloquear bus
                    BusData.getBusDataInsance().lock.tryAcquire();
                    //acceder a la otra caché
                    //otraCache(numeroBloque, numeroPalabra, posicionCache);

                    DataCache otherCache = dataCache.getRemoteCache(); //asi se obtiene la otra cache

                }
            } else {

                if (dataCache.getCache().get(posicionCache).getEtiqueta() != numeroBloque) {
                    //gets de la otra cache
                    if (dataCache.getCache().get(posicionCache).getEstado() == 'M') {
                        //copiar bloque en memoria
                        //cambiarle el estado a invalido
                        ///!!!bloquear posicion en cache
                        DataCache otherCache = dataCache.getRemoteCache(); //asi se obtiene la otra cache
                    }
                }
            }
        }
    }

/*
    public void otraCache(int numeroBloque, int numeroPalabra, int posicionCache) {
        //el if es parecido pero haciendo get de la cache del otro nucleo
        if (dataCache.getCache().get(posicionCache).getEtiqueta() == numeroBloque) {
            //el if es con la cache del otro lado
            if (dataCache.getCache().get(posicionCache).getEstado() == 'C') {
                //liberar posicion en otra cache
                //gets de la otra cache
                //subir bloque de memoria
                //liberar posicion en esta cache
            } else if (dataCache.getCache().get(posicionCache).getEstado() == 'M') {
                //guardar en memoria lo que está en caché
                //sobreescribir en la otra cache lo que esta en la cache actual
                //cambiar el estado en el otro lado a C
                //liberar posicion y bus en la otra cache

                //los gets con la cache del otro lado
                registers.set(IR.get(2), dataCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                //liberar posicion de esta cache
            } else if (dataCache.getCache().get(posicionCache).getEstado() == 'I') {
                //liberar posicion en caché
                //almacenar en el bloque de la cache el bloque corrspondiente de memoria
                //cambiar el estado a M
                //liberar bus

                //los gets con la cache del otro lado
                registers.set(IR.get(2), dataCache.getCache().get(posicionCache).getPalabras().get(numeroPalabra));
                //liberar posicion de esta cache
            }
        }
    }*/

    public void SW() {
        int numeroBloque = getNumeroDeBloque(IR.get(1), 16);
        int numeroPalabra = getNumeroDePalabra(IR.get(1), 16, 4);
        int posicionCache = getPosicionCache(numeroBloque, dataCache.getSize());
        //bloquear posicion cache!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (dataCache.getCache().get(posicionCache).getEtiqueta() == numeroBloque) {
            if (dataCache.getCache().get(posicionCache).getEstado() == 'M') {
                dataCache.getCache().get(posicionCache).setPalabra(numeroPalabra, registers.get(IR.get(2)));
                //liberar posicion en esta cache
                //avanza el reloj
            } else if (dataCache.getCache().get(posicionCache).getEstado() == 'C') {
                //se bloquea bus
                //bloquear posicion en otra cache
                //gets de otra cache
                if (this.dataCache.getRemoteCache().getCache().get(posicionCache).getEstado() == 'C') {
                    dataCache.getCache().get(posicionCache).setEstado('M');
                    this.dataCache.getRemoteCache().getCache().get(posicionCache).setEstado('I');
                }
                //se sobreescribe en memoria el bloque
                //liberar la posicion en caché
            } else if (dataCache.getCache().get(posicionCache).getEstado() == 'I') {
                //bloquea el bus
                //bloquea la posicion en la otra cache
                //revisa otra cache
                if (this.dataCache.getRemoteCache().getCache().get(posicionCache).getEtiqueta() == numeroBloque) {
                    if (this.dataCache.getRemoteCache().getCache().get(posicionCache).getEstado() == 'C') {
                        this.dataCache.getRemoteCache().getCache().get(posicionCache).setEstado('I');
                        //libera pos caché
                        //libera bus
                        //el bloque se sube de memoria y avanza un ciclo de reloj
                    } else if (this.dataCache.getRemoteCache().getCache().get(posicionCache).getEstado() == 'M') {
                        //se copia el bloque a memoria y a caché
                        //libera pos otra caché
                        //libera bus
                        dataCache.getCache().get(posicionCache).setPalabra(numeroPalabra, registers.get(IR.get(2)));
                        dataCache.getCache().get(posicionCache).setEstado('M');
                        //libera pos caché
                    }
                }
            }
        } else {
            //gets de la otra cache
            if (dataCache.getCache().get(posicionCache).getEtiqueta() != numeroBloque) {
                //gets de la otra cache
                if (dataCache.getCache().get(posicionCache).getEstado() == 'M') {
                    //copiar bloque en memoria
                    //cambiarle el estado a invalido
                    ///!!!bloquear posicion en cache, no se bloquea posicion, se bloquea la cache entera
                    if (this.dataCache.getRemoteCache().getCache().get(posicionCache).getEtiqueta() == numeroBloque) {
                        if (this.dataCache.getRemoteCache().getCache().get(posicionCache).getEstado() == 'C') {
                            //se sube a propia cache de memoria
                            //se libera la posicion de cache
                            //se avanza ciclo reloj


                        } else if (this.dataCache.getRemoteCache().getCache().get(posicionCache).getEstado() == 'M') {
                            //copia el bloque a memoria y a la otra cache
                            dataCache.getCache().get(posicionCache).setEstado('I');
                            //se libera el bus
                            //se libera la otra posicion de cache
                            this.dataCache.getRemoteCache().getCache().get(posicionCache).setPalabra(numeroPalabra, registers.get(IR.get(2)));
                            //se libera cache
                            //avanza el reloj

                        } else if (this.dataCache.getRemoteCache().getCache().get(posicionCache).getEstado() == 'I') {
                            //se libera posicion de cache
                            //Se sube el bloque a propia cache desde memoria
                            //se libera esa posicion
                            //avanza el reloj
                        }
                    } else {
                        //traerbloque de memoria
                        //guardar dato en cache propia
                        //liberar bus
                        //liberar posicion en cache
                    }
                }
            }
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
                //LW
                this.hilillo.removeQuantum();
                break;
            case 43:
                //SW
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
    }

    public void DADD() {
        int resultado = this.registers.get(IR.get(1)) + this.registers.get(IR.get(2));
        this.registers.set(IR.get(3), resultado);
        //System.out.println(registers.get(3));
    }

    public void DSUB() {
        int resultado = registers.get(IR.get(1)) - registers.get(IR.get(2));
        registers.set(IR.get(3), resultado);
    }

    public void DMUL() {
        int resultado = registers.get(IR.get(1)) * registers.get(IR.get(2));
        registers.set(IR.get(3), resultado);
    }

    public void DDIV() {
        if (registers.get(IR.get(2)) != 0) {
            int resultado = registers.get(IR.get(1)) / registers.get(IR.get(2));
            registers.set(IR.get(3), resultado);
        } else {
            System.out.println("Advertencia! Está dividiendo entre 0.");
        }
    }

    public void BEQZ() {
        if (registers.get(IR.get(1)) == 0) {
            PC = PC + 4 * IR.get(3);
        }
    }

    public void BNEZ() {
        if (registers.get(IR.get(1)) != 0) {
            PC = PC + 4 * IR.get(3);
        }
    }

    public void JAL() {
        registers.set(31, PC);
        PC = PC + IR.get(3);
    }

    public void JR() {
        PC = registers.get(IR.get(1));
    }

    public void FIN() {

    }

    @Override
    public void run() {
        int endIR = this.hilillo.getContext().getPCfinal();
        int count = this.hilillo.getContext().getPCinitial();
        while (count <= endIR) {
            BlockInstructions blockInstructions = InstructionsMemory.getInstructionsMemoryInstance().getInstrucciones().get(this.PC);
            this.IR = blockInstructions.getInstructions();
            this.executeInstruction(blockInstructions.getInstructions());
            count++;
            this.PC++;
        }

    }
}
