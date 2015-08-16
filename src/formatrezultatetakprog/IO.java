package formatrezultatetakprog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author luka
 */
public class IO {

    public IO() {
        files = FormatRezultateTakprog.podaciFolder.listFiles((File dir, String name) -> name.endsWith(".rez"));
    }

    private final File[] files;
    private static int index = 0;
    private static final String[] sheetName = {"A", "B"};

    public boolean hasNext() {
        return index < files.length;
    }

    public static int getIndex() {
        return index;
    }

    public File next() throws IOException {
        index++;
        return files[index - 1];
    }

    @SuppressWarnings("empty-statement")
    public void writeXSSF() throws IOException {

        System.out.println("\n     UPISUJEM  PODATKE ");

        FormatRezultateTakprog.outFile.createNewFile();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheetA = workbook.createSheet(sheetName[0]);
        XSSFSheet sheetB = workbook.createSheet(sheetName[1]);
        firstRow(sheetA); firstRow(sheetB);
        int ja=1, jb=1;
        FormatRezultateTakprog.startReading();
        while(ja+jb<FormatRezultateTakprog.getBrojTakmicara()) {
            Takmicar tak = FormatRezultateTakprog.next();
            Row row=null;
            if(tak.getKategorija()=='A') {
                row = sheetA.createRow(ja);
                ja++;
            } else if(tak.getKategorija()=='B') {
                row = sheetB.createRow(jb);
                jb++;
            } else {
                System.exit(tak.getKategorija());
            }
                
            Object val;
            tak.startReading();
            for (int k = 0; tak.hasNext(); k++) {
                val = tak.next();
                Cell cell = row.createCell(k);
                if (val instanceof Number) {
                    cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(Double.valueOf(val.toString()));
                } else {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cell.setCellValue((String)val);
                }
            }
        }
        FileOutputStream out
                = new FileOutputStream(FormatRezultateTakprog.outFile);
        workbook.write(out);
        out.close();
    }
    
    private void firstRow(XSSFSheet sh) {
        Row r = sh.createRow(0);
        Cell c1 = r.createCell(0), c2 = r.createCell(1), c3 = r.createCell(2);
        c1.setCellValue("username");
        c2.setCellValue("UKUPNO BODOVA");
        c3.setCellValue("ZADATAK 1");
    }
}
