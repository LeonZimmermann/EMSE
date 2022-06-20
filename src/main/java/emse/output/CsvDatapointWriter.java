package emse.output;

import emse.models.Datapoint;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvDatapointWriter implements DatapointWriter {

  private static final String OUTPUT_FILE = "results.csv";

  private int idOffset = 0;
  private int subjectOffset = 0;

  public CsvDatapointWriter() {
    final boolean fileAlreadyExists = new File(OUTPUT_FILE).exists();
    if (!fileAlreadyExists) {
      writeHeader();
    } else {
      getOffsets();
    }
  }

  private void writeHeader() {
    try (FileWriter writer = new FileWriter(OUTPUT_FILE, true)) {
      writer.append("subject").append(";").
          append(Arrays.stream(Datapoint.class.getFields()).map(Field::getName).collect(
              Collectors.joining(";"))).append("\n");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void getOffsets() {
    try {
      final List<String> lines = Files.readAllLines(Path.of(OUTPUT_FILE));
      final String lastLine = lines.get(lines.size() - 1);
      final String[] dataOfLastLine = lastLine.split(";");
      subjectOffset = Integer.parseInt(dataOfLastLine[0]);
      idOffset = Integer.parseInt(dataOfLastLine[1]);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeDatapoint(final Datapoint datapoint) {
    try (FileWriter writer = new FileWriter(OUTPUT_FILE, true)) {
      writer.append(String.valueOf(subjectOffset + 1)).append(";")
          .append(String.valueOf(idOffset + datapoint.id)).append(";")
          .append(String.valueOf(datapoint.method.ordinal())).append(";")
          .append(String.valueOf(datapoint.complexity)).append(";")
          .append(String.valueOf(datapoint.numberOfConjunctions)).append(";")
          .append(String.valueOf(datapoint.numberOfParameters)).append(";")
          .append(datapoint.result ? "1" : "0").append(";")
          .append(String.valueOf(datapoint.timeInMillis)).append(";")
          .append(datapoint.correct ? "1" : "0")
          .append("\n")
          .flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
