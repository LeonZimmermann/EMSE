package emse.models;

public class CodeSample {

    public final Method method;
    public final String code;
    public final boolean printing;

    public CodeSample(Method method, String code, boolean printing) {
        this.method = method;
        this.code = code;
        this.printing = printing;
    }

}
