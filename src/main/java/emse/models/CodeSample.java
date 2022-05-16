package emse.models;

public class CodeSample {

    public final int id;
    public final Method method;
    public final String code;
    public final boolean printing;

    public CodeSample(int id, Method method, String code, boolean printing) {
        this.id = id;
        this.method = method;
        this.code = code;
        this.printing = printing;
    }

}
