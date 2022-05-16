package emse.models;

public class Datapoint {

    public final Method method;
    public final long timeInMillis;
    public final boolean correct;

    public Datapoint(Method method, long timeInMillis, boolean correct) {
        this.method = method;
        this.timeInMillis = timeInMillis;
        this.correct = correct;
    }
}
