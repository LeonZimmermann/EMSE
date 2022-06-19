package emse.output;

import java.io.FileWriter;
import java.io.IOException;

import emse.models.Datapoint;

public class DatapointWriter {

    private static final String OUTPUT_FILE = "results.csv";

    public void writeDatapoint(final Datapoint datapoint) {
        try (FileWriter writer = new FileWriter(OUTPUT_FILE, true)) {
            writer.append(String.valueOf(datapoint.method.ordinal())).append(";")
                    .append(String.valueOf(datapoint.timeInMillis)).append(";")
                    .append(datapoint.correct ? "1" : "0")
                    .append("\n")
                    .flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
