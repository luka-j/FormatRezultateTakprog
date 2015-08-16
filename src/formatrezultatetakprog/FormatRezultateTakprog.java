package formatrezultatetakprog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


/**
 *
 * @author luka
 */
public class FormatRezultateTakprog {

    /**
     * Fajl iz koga se ucitava tabela.
     */
    final static File podaciFolder = new File("/home/luka/Okruzno2015/kodovi3");
    /**
     * Fajl u koji se upisuju obradjeni podaci.
     */
    final static File outFile = new File("/home/luka/Okruzno2015/tabelanew2.xlsx");
    final static int LAST_COL_NUMBER = 127;
    final static float RELATIVE_TIME = 10;

    /**
     * Matrica sa podacima. Prvi index oznacava sheet (od 0), drugi kolonu (od
     * 0), a treci red(od 0)
     * podaci.length - broj sheetova; podaci[n].length - broj kolona u n-tom sheetu
     * podaci[n][m].length - broj redova u n-tom sheetu u m-toj koloni
     */
    private static final List<Takmicar> takmicari = new LinkedList<>();
    private static Iterator<Takmicar> iterator;
    
    public static void main(String[] args) throws IOException {
        long start, end;
        start = System.nanoTime();
        IO io = new IO();
        while(io.hasNext()) {
            scan(io.next());
        }
        end = System.nanoTime();
        System.out.println("Vreme: " + (end-start));
        start = System.nanoTime();
        io.writeXSSF();
        end = System.nanoTime();
        System.out.println("Write time: " + (end-start));
        //System.out.println("msgs:");
        //System.out.println(NObrada.unparsedmsgs);
        System.out.println("ces:");
        System.out.println(FormatUtils.unparsedCE);
    }
    
    public static void scan(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        String filename = file.getName();
        Takmicar tak = new Takmicar(filename.substring(0, filename.length()-4));
        while(sc.hasNext()) {
            String imeZadatka = sc.nextLine(), line;
            StringBuilder dataZadatka = new StringBuilder();
            while(true) {
                line = sc.nextLine();
                if(line.isEmpty() || line.equals("\n"))
                    break;
                else dataZadatka.append(line).append("\n");
            }
            tak.addZadatak(imeZadatka, dataZadatka.toString());
        }
        takmicari.add(tak);
    }
    
    public static int getBrojTakmicara() {
        return takmicari.size();
    }
    
    public static void startReading() {
        takmicari.sort(null);
        iterator = takmicari.iterator();
    }
    public static boolean hasNext() {
        return iterator.hasNext();
    }
    public static Takmicar next() {
        return iterator.next();
    }
}
