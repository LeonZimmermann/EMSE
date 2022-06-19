package emse.models;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class CodeSample {
    public final int id;
    public final Method method;
    public final String code;
    public final boolean printing;
}
