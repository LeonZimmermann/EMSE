package emse.input;

import java.util.Random;

import emse.models.Expression;
import emse.models.ExpressionGenerationParameters;
import emse.models.ExpressionTemplate;
import emse.models.Method;

public class ExpressionGenerationSource implements ExpressionSource {

    private static final int SEED = 1000;

    private int index = 0;
    private final ExpressionGenerator expressionGenerator = new ExpressionGenerator(SEED);
    private final Random random = new Random(SEED);

    @Override
    public Expression getNext() {
        final ExpressionGenerationParameters parameters = new ExpressionGenerationParameters().toBuilder()
                .method(Method.values()[random.nextInt(Method.values().length)])
                .build();

        final ExpressionTemplate template = expressionGenerator.createDisjuntiveNormalformExpression(parameters);
        return new Expression(index++, template);
    }
}
