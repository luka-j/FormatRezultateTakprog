package formatrezultatetakprog;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author luka
 */
public class FormatUtils {
    
    public static String expandLang(String lang) {
        switch(lang) {
            case "c": return "C";
            case "cpp": return "C++";
            case "pas": return "Pascal";
            default: return "nepoznato";
        }
    }
    
    public static int countChars(String s, String ch) {
        return s.length() - s.replace(ch, "").length();
    }
    
    final static Set<String> unparsedRTE = new HashSet<>();
    public static String formatRTE(String msg) {
        msg = msg.toLowerCase();
        if(msg.contains("failed to run"))
            return "CE";
        if(msg.contains("status 127"))
            return "CE: not found";
        if(msg.contains("status 124"))
            return "TLE (5+ sekundi)";
        //C / C++
        if(msg.contains("signal 11") || msg.contains("segmentation fault"))
            return "SIGSEGV";
        if(msg.contains("the monitored command dumped core"))
            return "SIGSEGV: core dump";
        if(msg.contains("signal 8") || msg.contains("floating point exception"))
            return "SIGFPE";
        if(msg.contains("signal 6") || msg.contains("aborted"))
            return "SIGABRT: " + msg;
        if(msg.contains("signal 7"))
            return "SIGEMT";
        if(msg.contains("signal 9") || msg.contains("killed"))
            return "SIGKILL" + msg;
        if(msg.contains("bus error"))
            return "SIGBUS";
        if(msg.contains("std::bad_alloc"))
            return "MLE";
        if(msg.contains("pause: not found"))
            return "system(\"pause\")";
        //Pascal
        if(msg.contains("status 103"))
            return "File not open ?";
        if(msg.contains("status 200"))
            return "Deljenje sa nulom";
        if(msg.contains("status 201"))
            return "Range error";
        if(msg.contains("status 207"))
            return "Invalid floating point op";
        if(msg.contains("status 216"))
            return "General protection fault";
        if(msg.contains("status 106"))
            return "Invalid numeric format";
        if(msg.contains("status 205"))
            return "Floating point overflow";
        unparsedRTE.add(msg+"\n");
        return msg;
    }
    
    final static Set<String> unparsedCE = new HashSet<>();
    public static String formatCE(String ce) {
        //C / C++
        //losi includovi:
        if(ce.contains("iostream.h")) 
            return "#include<iostream.h>";
        if(ce.contains("‘scanf’ was not declared in this scope") || 
                ce.contains("‘printf’ was not declared in this scope"))
            return "Nije includovan stdio.h (za scanf/printf funkcije)";
        if(ce.contains("‘sort’ was not declared in this scope"))
            return "Nije includovan algorithm (za sort funkciju)";
        if(ce.contains("‘abs’ was not declared in this scope"))
            return "Nije includovan stdlib.h (za abs funkciju)";
        if(ce.contains("‘system’ was not declared in this scope"))
            return "Nije includovan stdio.h za system";
        if(ce.contains("conio.h"))
            return "Includovan conio.h (DOS header)";
        if(ce.contains("iostream: No such file or directory"))
            return "Includovan iostream u C programu";
        if(ce.contains("cstdlib: No such file or directory"))
            return "Includovan cdstlib u C programu (umesto stdlib.h)";
        if(ce.contains("error: no matching function for call to ‘find"))
            return "Loš argument za find ili nije includovan algorithm";
        //nestandardne operacije
        if(ce.contains("uint64_t"))
            return "C89/C++98 ne podržavaju fixed-width integere";
        if(ce.contains("scanf_s"))
            return "C89 ne podržava scanf_s";
        if(ce.contains("error: ISO C++ forbids comparison between pointer and integer"))
            return "Upoređivanje pokazivača i celog broja";
        if(ce.contains("as array of references")) 
            return "Korišćenje niza referenci";
        if(ce.contains("relocation truncated to fit"))
            return "Data segment >2GB";
        //predefinisane promenljive:
        if(ce.contains("unix=x;"))
            return "unix je predefinisana konstanta";
        if(ce.contains("error: ‘long long int time’ redeclared as different kind of symbol"))
            return "Redefinisanje promenljive time; jedan od includovanih fajlova (time.h) koristi ime time";
        //sintaksne greske:
        if(ce.contains("as multidimensional array must have bounds for all dimensions except the first")) 
            return "Niz deklarisan bez veličine (kao niz[][])";
        if(ce.contains("undefined reference to `main'"))
            return "Fali main (loš/nedovršen fajl)";
        if(ce.contains("error: case label not within a switch statement"))
            return "case bez switch-a";
        if(ce.contains("stray ‘#’ in program"))
            return "Karakter '#' nije pod navodnicima";
        if(ce.contains("expected ‘;’"))
            return "Fali ;";
        if(ce.contains("expected ‘{’"))
            return "Fali '{'";
        if(ce.contains("error: expected primary-expression before ‘}’ token"))
            return "'}' viška";
        if(ce.contains("error: expected primary-expression before ‘)’ token"))
            return "')' viška";
        //specificne (sintaksne) greske:
        if(ce.contains("error: variable-sized object may not be initialized     "
                + "long long h[n][m], redmin[n], kolonamax[m], Nmin[n] = 0, Nmax[m] = 0;"))
            return "Dodela vrednosti nizu pri deklaraciji";
        if(ce.contains("sifra.cpp:4:1: error: expected initializer before ‘long’"))
            return "Fali ;";
        if(ce.contains("front.c:30:12: error: expected declaration specifiers or ‘...’ before string constant"))
            return "'}' viška";
        if(ce.contains("front.c:3:1: error: expected identifier or ‘(’ before ‘{’ token"))
            return "Nemam pojma šta je pisac hteo reći";
        //Pascal
        if(ce.contains("link.res contains output sections; did you forget -T?"))
            return "Pascal linking error: ignorisati"; 
        if(ce.contains("Error: Illegal qualifier"))
            return "Illegal qualifier";
        if(ce.contains("Illegal assignment to for-loop variable"))
            return "Dodeljivanje vrednosti promenljivoj po kojoj se radi iteracija (for-loop)";
        //Misc (pretpostavljam C/C++)
        if(ce.contains("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>"))
            return "Poslao si XML fajl, idiote";
        
        unparsedCE.add(ce+"\n");
        return ce;
    }
}
