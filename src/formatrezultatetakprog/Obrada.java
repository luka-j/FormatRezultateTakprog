//package formatrezultatetakprog;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.Arrays;
//import java.util.Scanner;
//
///**
// *
// * @author luka
// */
//public class Obrada {
//    
//    private final File file;
//    private final int red;
//    private final Scanner sc;
//    public Obrada(File f, int red) throws FileNotFoundException {
//        file = f;
//        this.red = red;
//        sc = new Scanner(file);
//        sc.useDelimiter("\n");
//    }
//    
//    public void go() throws FileNotFoundException {
//        String[] name = file.getName().split("\\."); StringBuilder ime = new StringBuilder();
//        for(int i=0; i<name.length-1; i++) {
//            ime.append(name[i]).append(".");
//        }
//        FormatRezultateTakprog.podaci[0][0][red] = ime.toString();
//        String line=null;
//        while(sc.hasNextLine() && !(line=sc.nextLine()).contains(".")) ;
//        doZadatak(line);
//    }
//    
//    private void doZadatak(String zadatak) {
//        if(!sc.hasNextLine())
//            return;
//        if(zadatak.isEmpty() || zadatak.equals("\n"))
//            sc.nextLine();
//        StringBuilder tp; String line;
//        int colNumber = getColNumber(zadatak.split("\\.")[0]);
//        FormatRezultateTakprog.podaci[0][colNumber][red] = expandLang(zadatak.split("\\.")[1]);
//        colNumber++;
//        while(sc.hasNextLine() && sc.hasNext("(?!.*\\.(pas|c|cpp)$).*")) {
//            tp = new StringBuilder(); String next;
//            //while(sc.hasNextLine() && (next = sc.nextLine()).matches("\\s*")) {
//                
//            //}
//            do {
//                line = sc.next();
//                if(line.matches("\\s*")) {
//                    System.out.println("Line: " + line);
//                    break;
//                }
//                tp.append(sc.nextLine()).append(" ");
//            }
//            while(sc.hasNext("(?!.*,.*$).*") && sc.hasNextLine()); 
//            System.out.println("tp: " + tp.toString());
//            formatLine(tp.toString(), zadatak.split("\\.")[0], colNumber);
//            colNumber+=2;
//        }
//        if(sc.hasNextLine()) 
//            doZadatak(sc.nextLine());
//    }
//    
//    private void formatLine(String line, String zadatak, int kol) {
//        String[] data = line.split(",");
//        if(Integer.parseInt(data[0])==1)
//            FormatRezultateTakprog.podaci[0][kol][red] = getBrojBodova(zadatak);
//        else
//            FormatRezultateTakprog.podaci[0][kol][red] = "0";
//        try {
//            System.out.println("0: " + data[0]);
//            System.out.println("1: " + data[1]);
//            String[] vreme = data[1].split("=");
//            String[] uk = vreme[0].split(":")[1].split("\\.");
//            StringBuilder vremeOut = new StringBuilder();
//            int ukVreme = Integer.parseInt(uk[0])*100 + Integer.parseInt(uk[1]);
//            String[] user = vreme[1].split("\\+")[0].split("\\."), 
//                     system = vreme[1].split("\\+")[1].split("\\.");
//            int usVreme = Integer.parseInt(user[0])*100 + Integer.parseInt(user[1]);
//            int sysVreme = Integer.parseInt(system[0])*100 + Integer.parseInt(system[1]);
//            vremeOut.append(ukVreme).append("=").append(usVreme).append("\\+").append(sysVreme);
//            FormatRezultateTakprog.podaci[0][kol+1][red] = vremeOut.toString();
//        } catch(NumberFormatException ex) {
//            FormatRezultateTakprog.podaci[0][kol+1][red] = line;
//        }
//    }
//    
//    static int getColNumber(String zadatak) {
//        switch(zadatak) {
//            case "misterija": return 1;
//            case "igra": return 42;
//            case "retrovirus": return 63;
//            case "levi": return 104;
//            case "klasifikacija": return 125;
//            case "segfault": return 166;
//            default: System.err.println("wrong col num: "+zadatak); return 250;
//        }
//    }
//    
//    static String getBrojBodova(String zadatak) {
//        switch(zadatak) {
//            case "misterija": case "retrovirus": case "klasifikacija":
//                return "5";
//            case "igra": case "levi": case "segfault":
//                return "10";
//            default: return "wtf";
//        }
//    }
//    
//    static String expandLang(String lang) {
//        switch(lang) {
//            case "c": return "C";
//            case "cpp": return "C++";
//            case "pas": return "Pascal";
//            default: return "nepoznato";
//        }
//    }
//}
