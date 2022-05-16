package emse.models;

public class Datapoint {

    public final Method method;
    public final int codeSample;
    public final long timeInMillis;
    public final boolean correct;

    public Datapoint(Method method, int codeSample, long timeInMillis, boolean correct) {
        this.method = method;
        this.codeSample = codeSample;
        this.timeInMillis = timeInMillis;
        this.correct = correct;
    }
}
