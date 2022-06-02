package emse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import emse.models.Expression;
import emse.models.Method;

/**
 * Disjuntive Normalform: (x1 && x2) || (x3 && !x4 && !x5) || (x6 && x7)
 */
public class ExpressionGenerator {

    private final Random random;

    public ExpressionGenerator(final int seed) {
        random = new Random(seed);
    }

    /**
     * @param numberOfConjunctions the number of conjunctions that the expression is supposed to have
     * @param complexity           the number of parameters that have to be read, before the result of the expression can be determined
     * @return the construct of a boolean expression in disjunctive normal form
     */
    public Expression createDisjuntiveNormalformExpression(final int numberOfConjunctions, final int complexity, final Method method) {
        if (numberOfConjunctions < complexity) {
            throw new IllegalArgumentException("Number of conjunctions can't be less than complexity, as the complexity is " +
                    "the number of parameters read, and each conjunction contains at least one parameter");
        }

        // TODO Generate read-lengths for each conjunction that add up to complexity
        // TODO Generate lengths for each conjunction, which are higher or equal read-length

        final boolean expressionResult = random.nextBoolean();
        final List<List<Boolean>> expressionData = new ArrayList<>();

        // TODO Implement generation

        final String expressionString = convertExpressionDataToExpressionString(expressionData, method);
        return new Expression(expressionResult, expressionString);
    }

    String convertExpressionDataToExpressionString(final List<List<Boolean>> expression, final Method method) throws IllegalArgumentException {
        return expression.stream()
                .map(conjunction -> convertConjunctionToString(conjunction, method))
                .collect(Collectors.joining(" || "));
    }

    private String convertConjunctionToString(final List<Boolean> conjunction, final Method method) throws IllegalArgumentException {
        final Collector<CharSequence, ?, String> joiningCollector = switch (method) {
            case BOOLEAN_NO_PARENTHESIS -> Collectors.joining(" && ");
            case BOOLEAN_WITH_PARENTHESIS -> Collectors.joining(" && ", "(", ")");
            case NESTED_IF -> throw new IllegalArgumentException("This method should not be called for method NESTED_IF");
        };
        return conjunction.stream()
                .map(Object::toString)
                .collect(joiningCollector);
    }


}
