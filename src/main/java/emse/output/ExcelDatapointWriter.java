package emse.output;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

import emse.models.Datapoint;

public class ExcelDatapointWriter implements DatapointWriter {
   private static final String OUTPUT_FILE = "results.xlsx";
   private static final String SHEET_NAME = "Data";

   @Override
   public void writeDatapoint(final Datapoint datapoint) {
      try {
         final FileInputStream file = new FileInputStream(OUTPUT_FILE);
         final Workbook workbook = new XSSFWorkbook(file);

         Sheet sheet = workbook.getSheet(SHEET_NAME);
         if (sheet == null) {
            sheet = workbook.createSheet(SHEET_NAME);
         }

         // TODO Finish Implementation
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
}
