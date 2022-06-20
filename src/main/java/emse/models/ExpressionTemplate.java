package emse.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExpressionTemplate {
    public final ExpressionGenerationParameters generationParameters;
    public final boolean result;
    public final String expression;

    @Override
    public String toString() {
        return result + ":" + expression;
    }
}
