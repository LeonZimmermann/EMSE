package emse.output;

import emse.models.Method;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import emse.models.Datapoint;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CsvDatapointWriter implements DatapointWriter {

    private static final String OUTPUT_FILE = "results.csv";

    @Override
    public void writeDatapoint(final Datapoint datapoint) {
        final boolean fileAlreadyExists = new File(OUTPUT_FILE).exists();
        try (FileWriter writer = new FileWriter(OUTPUT_FILE, true)) {
            // write header
            if (!fileAlreadyExists) {
                writer.append(
                    Arrays.stream(datapoint.getClass().getFields()).map(Field::getName).collect(
                        Collectors.joining(";"))).append("\n");
            }
            // write data
            writer.append(String.valueOf(datapoint.id)).append(";")
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
