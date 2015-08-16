//package formatrezultatetakprog;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.HashSet;
//import java.util.Scanner;
//import java.util.Set;
//
///**
// *
// * @author luka
// */
//public class NObrada {
//    
//    private final File file;
//    private final int red;
//    private final Scanner sc;
//    public NObrada(File f, int red) throws FileNotFoundException {
//        file = f;
//        this.red = red;
//        sc = new Scanner(file);
//        sc.useDelimiter("\n");
//    }
//    
//    public void go() {
//        String filename = file.getName();
//        FormatRezultateTakprog.podaci[0][0][red] = filename.substring(0, filename.length()-4);
//        int ukBodova=0;
//        while(sc.hasNext()) {
//            String imeZadatka = sc.nextLine(), line;
//            FormatRezultateTakprog.podaci[0][getColNumberOkr(imeZadatka)][red] = imeZadatka;
//            StringBuilder dataZadatka = new StringBuilder();
//            while(true) {
//                line = sc.nextLine();
//                if(line.isEmpty() || line.equals("\n"))
//                    break;
//                else dataZadatka.append(line).append("\n");
//            }
//            ukBodova+=parseZadatak(imeZadatka, dataZadatka.toString());
//        }
//        FormatRezultateTakprog.podaci[0][FormatRezultateTakprog.LAST_COL_NUMBER][red] = String.valueOf(ukBodova);
//    }
//    
//    private int parseZadatak(String ime, String data) {
//        String realLine=null, parsedCE=null;
//        StringBuilder ce = new StringBuilder();
//        int kol=getColNumberOkr(ime)+1, ukBodova=0, it=0;
//        String[] lines = data.split("\\n");
//        if(lines[it].equals("CE: ")) {
//            it++;
//            while(!lines[it].equals("EC")) {
//                ce.append(lines[it]);
//                it++;
//            }
//            parsedCE=parseCE(ce.toString());
//            it++;
//        }
//        for(;it<lines.length; it++) {
//            if(lines[it].contains(",") && realLine != null) {
//                ukBodova+=parseLine(realLine, ime, parsedCE, kol);
//                kol+=2;
//                realLine = "";
//            }
//            if(realLine==null) realLine="";
//            realLine+=lines[it] + "\4";
//        }
//        ukBodova+=parseLine(realLine, ime, parsedCE, kol);
//        FormatRezultateTakprog.podaci[0][getLastColNumOkr(ime)][red] = String.valueOf(ukBodova);
//        return ukBodova;
//    }
//    
//    private int parseLine(String line, String zad, String ce, int kol) {
//        StringBuilder bodova = new StringBuilder(String.valueOf(calcBodove(line.split(",")[0], zad)));
//        int brBodova, ukTime, tl = getTLOkr(zad)*(int)FormatRezultateTakprog.RELATIVE_TIME;
//        String errDesc=null;
//        String time;
//        boolean err = countChars(line, "\4")>1;
//        if(err) {
//            String errmsg;
//            String[] tokens = line.split("\4");
//            errmsg = tokens[0].split(",")[1];
//            for(int i=1; i<tokens.length-1; i++) {
//                errmsg+="\n" + tokens[i];
//            }
//            errDesc = parseErrorMsg(errmsg);
//            time = tokens[tokens.length-1].trim();
//        } else {
//            time = line.split(",")[1];
//        }
//        time = parseTime(time);
//        ukTime = getUkTime(time);
//        if(ukTime > tl) {
//            brBodova=0;
//            bodova.append(", TLE");
//        } else {
//            brBodova = Integer.parseInt(bodova.toString());
//            if(errDesc != null) {
//                bodova.append(", ").append(errDesc);
//                if(errDesc.startsWith("CE"))
//                    bodova.append(": ").append(ce);
//            }
//        }
//        FormatRezultateTakprog.podaci[0][kol][red] = bodova.toString();
//        FormatRezultateTakprog.podaci[0][kol+1][red] = time;
//        return brBodova;
//    }
//    
//    private int calcBodove(String rel, String zad) {
//        float relBodova = Float.parseFloat(rel);
//        return (int)relBodova * getBrojBodovaOkr(zad);
//    }
//    
//    static Set<String> unparsedmsgs = new HashSet<>();
//    private String parseErrorMsg(String msg) {
//        msg = msg.toLowerCase();
//        if(msg.contains("failed to run"))
//            return "CE";
//        if(msg.contains("status 127"))
//            return "CE: not found";
//        if(msg.contains("status 124"))
//            return "TLE (5+ sekundi)";
//        //C / C++
//        if(msg.contains("signal 6") || msg.contains("aborted"))
//            return "SIGABRT: " + msg;
//        if(msg.contains("signal 7"))
//            return "SIGEMT";
//        if(msg.contains("signal 8") || msg.contains("floating point exception"))
//            return "SIGFPE";
//        if(msg.contains("signal 9") || msg.contains("killed"))
//            return "SIGKILL" + msg;
//        if(msg.contains("bus error"))
//            return "SIGBUS";
//        if(msg.contains("signal 11") || msg.contains("segmentation fault"))
//            return "SIGSEGV";
//        if(msg.contains("the monitored command dumped core"))
//            return "SIGSEGV: core dump";
//        if(msg.contains("std::bad_alloc"))
//            return "MLE";
//        if(msg.contains("pause: not found"))
//            return "system(\"pause\")";
//        //Pascal
//        if(msg.contains("status 103"))
//            return "File not open ?";
//        if(msg.contains("status 200"))
//            return "Deljenje sa nulom";
//        if(msg.contains("status 201"))
//            return "Range error";
//        if(msg.contains("status 207"))
//            return "Invalid floating point op";
//        if(msg.contains("status 216"))
//            return "General protection fault";
//        if(msg.contains("status 106"))
//            return "Invalid numeric format";
//        if(msg.contains("status 205"))
//            return "Floating point overflow";
//        unparsedmsgs.add(msg+"\n");
//        return msg;
//    }
//    
//    static Set<String> unparsedces = new HashSet<>();
//    private String parseCE(String ce) {
//        if(ce.contains("iostream.h")) 
//            return "#include<iostream.h>";
//        if(ce.contains("‘scanf’ was not declared in this scope") || 
//                ce.contains("‘printf’ was not declared in this scope"))
//            return "Nije includovan stdio.h (za scanf/printf funkcije)";
//        if(ce.contains("‘sort’ was not declared in this scope"))
//            return "Nije includovan algorithm (za sort funkciju)";
//        if(ce.contains("‘abs’ was not declared in this scope"))
//            return "Nije includovan stdlib.h (za abs funkciju)";
//        if(ce.contains("‘system’ was not declared in this scope"))
//            return "Nije includovan stdio.h za system\nA i pise da ne treba koristiti pause";
//        if(ce.contains("conio.h"))
//            return "Includovan conio.h (DOS header)";
//        if(ce.contains("iostream: No such file or directory"))
//            return "Includovan iostream u C programu";
//        if(ce.contains("uint64_t"))
//            return "C89/C++98 ne podrzavaju fixed-width integere";
//        if(ce.contains("scanf_s"))
//            return "C89 ne podrzava scanf_s";
//        if(ce.contains("link.res contains output sections; did you forget -T?"))
//            return ""; 
//        if(ce.contains("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>"))
//            return "Poslao si XML fajl, idiote";
//        unparsedces.add(ce+"\n");
//        return ce;
//    }
//    
//    private String parseTime(String time) {
//        time=time.trim();
//        String[] tokens = time.split("=");
//        String uk = tokens[0], u = null,s = null;
//        u = tokens[1].split("\\+")[0]; s=tokens[1].split("\\+")[1];
//        uk=uk.substring(2);
//        try{
//        float ukfp = Float.parseFloat(uk), userfp = Float.parseFloat(u), sysfp=Float.parseFloat(s);
//        ukfp*=1000; userfp*=1000; sysfp*=1000;
//        int ukint = (int)ukfp, userint = (int)userfp, sysint = (int)sysfp;
//        if(sysint<=10 || ukint == 10000)
//            return String.valueOf(ukint) + "ms";
//        else
//            return String.valueOf(ukint) + "=" + String.valueOf(userint) + "+" + String.valueOf(sysint) + "ms";
//        } catch(NumberFormatException ex) {
//            System.err.println("Greska pri parsiranju vremena: " + time);
//            return "ERROR";
//        }
//    }
//    
//    private int getUkTime(String parsedTime) {
//        if(parsedTime.equals("ERROR")) return -1;
//        String uk = parsedTime.substring(0, parsedTime.length()-2).split("=")[0];
//        return Integer.parseInt(uk);
//    }
//    
//    static int countChars(String s, String ch) {
//        return s.length() - s.replace(ch, "").length();
//    }
//    
//    static int getBrojBodovaOkr(String zadatak) {
//        zadatak = zadatak.split("\\.")[0];
//        switch(zadatak) {
//            case "kodovi": case "skrinja": case "sifra": case "sudari":
//                return 5;
//            case "presto": case "front":
//                return 10;
//            default: return -1;
//        }
//    }
//    
//    static int getColNumberOkr(String zadatak) {
//        zadatak = zadatak.split("\\.")[0];
//        switch(zadatak) {
//            case "front": case "kodovi": return 1;
//            case "sifra": case "skrinja": return 43;
//            case "sudari": case "presto": return 85;
//            default: System.err.println("los zadatak/TL: " + zadatak); return 150;
//        }
//    }
//    
//    static int getLastColNumOkr(String zadatak) {
//        zadatak = zadatak.split("\\.")[0];
//        switch(zadatak) {
//            case "front": case "kodovi": return 42;
//            case "sifra": case "skrinja": return 84;
//            case "sudari": case "presto": return 126;
//            default: System.err.println("los zadatak/TL: " + zadatak); return 200;
//        }
//    }
//    
//    static int getTLOkr(String zadatak) {
//        zadatak = zadatak.split("\\.")[0];
//        switch(zadatak) {
//            case "front": case "sudari": case "kodovi":
//                return 500;
//            case "sifra": case "skrinja": case "presto":
//                return 1000;
//            default: System.err.println("los zadatak/TL: " + zadatak); return 0;
//        }
//    }
//    
//    static char getKategorijaOkr(String zadatak) {
//        zadatak = zadatak.split("\\.")[0];
//        switch(zadatak) {
//            case "front": case "sudari": case "sifra":
//                return 'A';
//            case "kodovi": case "skrinja": case "presto":
//                return 'B';
//            default: System.err.println("los zadatak/kat: " + zadatak); 
//                        return 'N';
//        }
//    }
//}
