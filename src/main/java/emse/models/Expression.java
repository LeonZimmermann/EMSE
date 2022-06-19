package emse.models;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
public class Expression {
    public final ExpressionGenerationParameters generationParameters;
    public final boolean result;
    public final String expression;

    @Override
    public String toString() {
        return result + ":" + expression;
    }
}
