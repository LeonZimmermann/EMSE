package emse.output;

import emse.models.Datapoint;

public interface DatapointWriter {
    void writeDatapoint(final Datapoint datapoint);
}
