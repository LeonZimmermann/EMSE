package emse.models;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Datapoint {
    public final int id;
    public final Method method;
    public int complexity;
    public int numberOfConjunctions;
    public int numberOfParameters;
    public boolean result;
    public final long timeInMillis;
    public final boolean correct;
}
