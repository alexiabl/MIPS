package arquitectura.mips.util;

import arquitectura.mips.Hilillo;
import arquitectura.mips.bloque.BloqueInstrucciones;
import arquitectura.mips.memoria.MemoriaInstrucciones;
import arquitectura.mips.memoria.MemoriaPrincipal;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class Util {

    private MemoriaInstrucciones memoriaInstrucciones;
    private MemoriaPrincipal memoriaPrincipal;
    private List<String> archivos;
    private Queue<Hilillo> hilillos;


    public Util() {
        this.memoriaInstrucciones = new MemoriaInstrucciones();
        this.archivos = new LinkedList<String>();
        File file1 = new File("1.txt");
        String path1 = file1.getAbsolutePath();

        File file2 = new File("2.txt");
        String path2 = file2.getAbsolutePath();

        File file3 = new File("3.txt");
        String path3 = file3.getAbsolutePath();

        File file4 = new File("4.txt");
        String path4 = file4.getAbsolutePath();

        File file5 = new File("5.txt");
        String path5 = file5.getAbsolutePath();

        this.archivos.add(path1);
        this.archivos.add(path2);
        this.archivos.add(path3);
        this.archivos.add(path4);
        this.archivos.add(path5);
        this.hilillos = new LinkedList<Hilillo>();
    }

    public void leerArchivos() { //insertar en memoria principal
        //cada linea por 4 que son las instucciones
        int contadorBloque = 0;
        int posicionActual = 0;
        for (int i = 0; i < this.archivos.size(); i++) {
            try {
                FileReader fileReader = new FileReader(this.archivos.get(i));
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                int cont = contadorBloque;
                Hilillo hilillo = new Hilillo(posicionActual);
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                    String[] split = line.split(" ");
                    BloqueInstrucciones bloqueInstrucciones = new BloqueInstrucciones();
                    bloqueInstrucciones.setNumBloque(contadorBloque);
                    ArrayList<Integer> instrucciones = new ArrayList<Integer>();
                    for (int s = 0; s < split.length; s++) {
                        instrucciones.add(new Integer(split[s]));
                    }
                    bloqueInstrucciones.setInstrucciones(instrucciones);
                    this.memoriaInstrucciones.addBloqueInstruccion(bloqueInstrucciones);
                    cont++;
                    if ((cont % 4) == 0) {
                        contadorBloque++;
                    }
                    posicionActual++;
                }
                this.agregarHillillo(hilillo);
                fileReader.close();
                System.out.println(stringBuffer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void agregarHillillo(Hilillo h) {
        this.hilillos.add(h);
    }

    public MemoriaInstrucciones getMemoriaInstrucciones() {
        return this.memoriaInstrucciones;
    }

    public Queue<Hilillo> getHilillos() {
        return this.hilillos;
    }


}
