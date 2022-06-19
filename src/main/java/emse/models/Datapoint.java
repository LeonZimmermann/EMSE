package emse.models;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Datapoint {
    public final Method method;
    public final int codeSample;
    public final long timeInMillis;
    public final boolean correct;
}
