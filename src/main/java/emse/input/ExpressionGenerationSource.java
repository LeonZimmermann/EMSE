package emse.input;

import java.util.Random;

import emse.models.Expression;
import emse.models.ExpressionGenerationParameters;
import emse.models.ExpressionTemplate;
import emse.models.Method;

public class ExpressionGenerationSource implements ExpressionSource {

    private static final int SEED = 1000;

    private int index = 0;
    private Method method = Method.BOOLEAN_NO_PARENTHESIS;
    private final ExpressionGenerator expressionGenerator = new ExpressionGenerator(SEED);
    private final Random random = new Random(SEED);

    @Override
    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public Expression getNext() {
        final int numberOfConjunctions = 2 + random.nextInt(2);
        final int complexity = 8 + random.nextInt(5);
        final int numberOfParameters = complexity + 3 + random.nextInt(4);
        final ExpressionGenerationParameters parameters = new ExpressionGenerationParameters().toBuilder()
                .numberOfConjunctions(numberOfConjunctions)
                .complexity(complexity)
                .numberOfParameters(numberOfParameters)
                .expressionResult(random.nextBoolean())
                .method(method)
                .build();
        final ExpressionTemplate template = expressionGenerator.createDisjuntiveNormalformExpression(parameters);
        return new Expression(index++, template);
    }
}
