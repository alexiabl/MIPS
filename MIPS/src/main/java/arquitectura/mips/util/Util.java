package arquitectura.mips.util;

import arquitectura.mips.bloque.BloqueInstrucciones;
import arquitectura.mips.memoria.MemoriaInstrucciones;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by alexiaborchgrevink on 6/11/18.
 */
public class Util {

    private MemoriaInstrucciones memoriaInstrucciones;
    private List<String> archivos;


    public Util() {
        this.memoriaInstrucciones = new MemoriaInstrucciones();
        this.archivos = new LinkedList<String>();
        String file1 = "/Users/alexiaborchgrevink/Desktop/Arquitectura/Proyecto-MIPS/MIPS/MIPS/src/main/java/arquitectura/mips/util/1.txt";
        String file2 = "/Users/alexiaborchgrevink/Desktop/Arquitectura/Proyecto-MIPS/MIPS/MIPS/src/main/java/arquitectura/mips/util/2.txt";
        String file3 = "/Users/alexiaborchgrevink/Desktop/Arquitectura/Proyecto-MIPS/MIPS/MIPS/src/main/java/arquitectura/mips/util/3.txt";
        String file4 = "/Users/alexiaborchgrevink/Desktop/Arquitectura/Proyecto-MIPS/MIPS/MIPS/src/main/java/arquitectura/mips/util/4.txt";
        String file5 = "/Users/alexiaborchgrevink/Desktop/Arquitectura/Proyecto-MIPS/MIPS/MIPS/src/main/java/arquitectura/mips/util/2.txt";
        this.archivos.add(file1);
        this.archivos.add(file2);
        this.archivos.add(file3);
        this.archivos.add(file4);
        this.archivos.add(file5);
    }

    public void leerArchivos() { //insertar en memoria principal
        //cada linea por 4 que son las instucciones
        int contadorBloque = 0;
        for (int i = 0; i < this.archivos.size(); i++) {
            try {
                File file1 = new File(this.archivos.get(i));
                FileReader fileReader = new FileReader(file1);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                int cont = contadorBloque;
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
                }
                fileReader.close();
                System.out.println(stringBuffer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public MemoriaInstrucciones getMemoriaInstrucciones() {
        return this.memoriaInstrucciones;
    }


}
