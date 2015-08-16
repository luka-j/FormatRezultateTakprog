package formatrezultatetakprog;

import java.util.Arrays;


/**
 *
 * @author luka
 */
public class Takmicar implements Comparable<Takmicar> {

    static final int BROJ_ZADATAKA = 3;

    private final String username;
    private char kategorija = 'B';
    private final Zadatak[] zadaci = new Zadatak[BROJ_ZADATAKA + 1];
    private int brojZadataka = 0;
    private int iterator=0;
    private int zadaciIterator=0;

    Takmicar(String username) {
        this.username = username;
    }

    public void addZadatak(String ime, String data) {
        zadaci[brojZadataka] = new Zadatak(ime, data);
        if (zadaci[brojZadataka].getKategorija() == 'A' && this.kategorija == 'B') {
            zadaci[0] = zadaci[brojZadataka];
            for (int i = 1; i <= brojZadataka; i++) {
                zadaci[i] = null;
            }
            this.kategorija = 'A';
            brojZadataka = 0;
        }
        brojZadataka++;
    }
    
    public char getKategorija() {
        return kategorija;
    }
    
    public void startReading() {
        Arrays.sort(zadaci, (Zadatak o1, Zadatak o2) -> {
            if (o1 == null && o2 == null) {
                return 0;
            }
            if (o1 == null) {
                return 1;
            }
            if (o2 == null) {
                return -1;
            }
            return o1.compareTo(o2);
        });
        iterator=0;
        zadaci[0].startReading();
        zadaci[brojZadataka-1].startReading();
    }
    
    public int countBodove() {
        int uk=0;
        for(int i=0; i<brojZadataka; i++)
            uk+=zadaci[i].countBodove();
        return uk;
    }
    
    public boolean hasNext() {
        return zadaci[brojZadataka-1].hasNext();
    }
    
    public Object next() {
        ++iterator;
        if(iterator==1)
            return username;
        if(iterator==2)
            return countBodove();
        //else
        if(zadaci[zadaciIterator].imeZadatka.getPrvaKolona()+1 >= iterator) {
            return "/";
        }
        if(zadaci[zadaciIterator].hasNext()) {
            return zadaci[zadaciIterator].next();
        }
        else {
            zadaciIterator++;
            zadaci[zadaciIterator].startReading();
            return zadaci[zadaciIterator].next();
        }
    }

    @Override
    public int compareTo(Takmicar o) {
        int thisBodovi = this.countBodove(), oBodovi = o.countBodove();
        if(thisBodovi > oBodovi)
            return -1;
        else if(thisBodovi < oBodovi)
            return 1;
        else return 0;
    }

    private static class Zadatak implements Comparable<Zadatak> {

        ZadaciOkruzno imeZadatka;
        String jezik;
        int ukupnoBodova;
        String compilationError;
        TestPrimer[] primeri;
        private int iterator=0;
        private int primerIterator=0;
        boolean stop=false;

        Zadatak(String filename, String data) {
            String[] ime = filename.split("\\.");
            imeZadatka = ZadaciOkruzno.valueOf(ime[0]);
            jezik = FormatUtils.expandLang(ime[1]);
            primeri = new TestPrimer[imeZadatka.getBrojTestPrimera()];
            String realLine = null;
            StringBuilder ce = new StringBuilder();
            int it = 0;
            String[] lines = data.split("\\n");
            if (lines[it].equals("CE: ")) {
                it++;
                while (!lines[it].equals("EC")) {
                    ce.append(lines[it]);
                    it++;
                }
                compilationError = FormatUtils.formatCE(ce.toString());
                it++;
            }
            for (int i = 0; it < lines.length; it++) {
                if (realLine == null) {
                    realLine = "";
                } else if (lines[it].contains(",")) {
                    primeri[i] = new TestPrimer(realLine, imeZadatka);
                    realLine = "";
                    i++;
                }
                realLine += lines[it] + "\4";
            }
            primeri[primeri.length-1] = new TestPrimer(realLine, imeZadatka);
        }

        char getKategorija() {
            return imeZadatka.kategorija;
        }

        public void startReading() {
            iterator=0;
            primerIterator=0;
            primeri[0].startReading();
            primeri[primeri.length-1].startReading();
        }
        
        public int countBodove() {
            int bodova=0;
            for(TestPrimer tp : primeri) {
                if(tp==null) {
                    System.err.println(imeZadatka + " nema dovoljno test primera: " + primeri.length);
                    continue;
                }
                if(tp.ukupnoVreme < imeZadatka.timeLimit)
                    bodova+=tp.izlaz*imeZadatka.brojBodova;
            }
            return bodova;
        }
        
        public boolean hasNext() {
            return iterator<imeZadatka.getBrojKolona();
        }
        
        public Object next() {
            iterator++;
            if(stop)
                return "";
            if(iterator==1)
                return jezik;
            if(iterator==2)
                return countBodove();
            if(iterator==3 && compilationError!=null) {
                stop=true;
                return "CE: " + compilationError;
            }
            if(primeri[primerIterator].hasNext())
                return primeri[primerIterator].next();
            else {
                primerIterator++;
                primeri[primerIterator].startReading();
                return primeri[primerIterator].next();
            }
        }
        
        @Override
        public int compareTo(Zadatak z2) {
            if(this.imeZadatka.redniBroj > z2.imeZadatka.redniBroj)
                return 1;
            if(this.imeZadatka.redniBroj < z2.imeZadatka.redniBroj)
                return -1;
            else {
                System.err.println("Dva zadatka istog broja");
                return 0;
            }
        }
    }

    private static class TestPrimer {
        
        ZadaciOkruzno zadatak;
        final float izlaz;
        int ukupnoVreme, userVreme, kernelVreme;
        final String errorDescription;
        private int iterator=0;

        TestPrimer(String toParse, ZadaciOkruzno zadatak) {
            this.zadatak = zadatak;
            izlaz = Float.parseFloat(toParse.split(",")[0]);
            String vreme;
            boolean err = FormatUtils.countChars(toParse, "\4") > 1;
            if (err) {
                String errmsg;
                String[] tokens = toParse.split("\4");
                errmsg = tokens[0].split(",")[1];
                for (int i = 1; i < tokens.length - 1; i++) {
                    errmsg += "\n" + tokens[i];
                }
                errorDescription = FormatUtils.formatRTE(errmsg);
                vreme = tokens[tokens.length - 1].trim();
            } else {
                errorDescription = null;
                vreme = toParse.split(",")[1];
            }
            
            vreme = vreme.trim();
            if(vreme.isEmpty()) {
                ukupnoVreme=-1; userVreme=-1; kernelVreme=-1;
                return;
            }
            String[] tokens = vreme.split("=");
            String uk = tokens[0], u = null, s = null;
            u = tokens[1].split("\\+")[0];
            s = tokens[1].split("\\+")[1];
            try {
                float ukfp = Float.parseFloat(uk), userfp = Float.parseFloat(u), sysfp = Float.parseFloat(s);
                ukfp *= 1000;
                userfp *= 1000;
                sysfp *= 1000;
                ukupnoVreme = (int) ukfp;
                userVreme = (int) userfp; 
                kernelVreme = (int) sysfp;
            } catch (NumberFormatException ex) {
                System.err.println("Greska pri parsiranju vremena: " + vreme);
                ukupnoVreme=-1; userVreme=-1; kernelVreme=-1;
            }
        }
        
        public String formatVreme() {
            if(kernelVreme<=10)
                return ukupnoVreme+"ms";
            else
                return ukupnoVreme+"="+userVreme+"+"+kernelVreme;
        }
        
        public void startReading() {
            iterator=0;
        }
        
        public boolean hasNext() {
            return iterator<2;
        }
        
        public Object next() {
            iterator++;
            switch(iterator) {
                case 1:
                    if(errorDescription!=null)
                        return (izlaz*zadatak.brojBodova) + ", " + errorDescription;
                    else
                        return (izlaz*zadatak.brojBodova);
                case 2:
                    return formatVreme();
                default: throw new IndexOutOfBoundsException();
            }
        }
    }
}
