package formatrezultatetakprog;

import java.util.Arrays;

/**
 *
 * @author luka
 */
public enum ZadaciOkruzno {
    
    front('B', 1, 10, 500),
    sifra('B', 2, 5, 1000),
    sudari('B', 3, 5, 500), 
    kodovi('A', 1, 5, 500),
    skrinja('A', 2, 5, 1000),
    presto('A', 3, 10, 1000);
    
    final static int BROJ_BODOVA_PO_ZADATKU = 100;
    final static int BROJ_KOLONA_PO_PRIMERU = 2;
    final static int BROJ_DODATNIH_KOLONA_PO_ZADATKU = 2;
    final static int BROJ_DODATNIH_KOLONA_PO_UCENIKU = 1;
    final char kategorija;
    final int redniBroj;
    final int brojBodova;
    final int timeLimit;
    
    ZadaciOkruzno(char kategorija, int redniBroj, int brojBodova, int timeLimit) {
        this.kategorija = kategorija;
        this.redniBroj = redniBroj;
        this.brojBodova = brojBodova;
        this.timeLimit = timeLimit;
    }
    
    public int getBrojTestPrimera() {
        return BROJ_BODOVA_PO_ZADATKU/brojBodova;
    }
    
    public int getBrojKolona() {
        return getBrojTestPrimera()*BROJ_KOLONA_PO_PRIMERU + BROJ_DODATNIH_KOLONA_PO_ZADATKU;
    }
    
    public int getPrvaKolona() {
        int kol=0;
        for(int i=1; i<redniBroj; i++)
            kol+=get(i, kategorija).getBrojKolona();
        return kol+BROJ_DODATNIH_KOLONA_PO_UCENIKU;
    }
    
    public ZadaciOkruzno get(int i, char kategorija) {
        for(ZadaciOkruzno z : ZadaciOkruzno.values())
            if(z.kategorija==kategorija && z.redniBroj==i)
                return z;
        return null;
    }
}