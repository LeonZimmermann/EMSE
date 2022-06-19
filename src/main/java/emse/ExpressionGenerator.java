package emse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import emse.models.Expression;
import emse.models.Method;

/**
 * disjuntive normalform: (x1 && x2) || (x3 && !x4 && !x5) || (x6 && x7)
 * Reading the disjunctive normalform:
 * 1. Begin with first conjunction: read until the first false parameter was found. If none was found, result is true.
 * 2. Go to next conjunction
 * Create the disjuntive normalform:
 * 1. Determine the number of parameters, conjunctions and parameters that have to be read
 * 2. Determine the result (true/false)
 * 3.
 */
public class ExpressionGenerator {

    private static final float THRESHOLD_FACTOR = .4f;

    private final Random random;

    public ExpressionGenerator(final int seed) {
        random = new Random(seed);
    }

    /**
     * @param numberOfConjunctions the number of conjunctions that the expression is supposed to have
     * @param numberOfParameters   the number of parameter in the expression
     * @param complexity           the number of parameters that have to be read, before the result of the expression can be determined
     * @return the construct of a boolean expression in disjunctive normal form
     */
    public Expression createDisjuntiveNormalformExpression(
            final int numberOfConjunctions,
            final int numberOfParameters,
            final int complexity,
            final Method method
    ) {
        if (numberOfConjunctions < complexity) {
            throw new IllegalArgumentException("Number of conjunctions can't be less than complexity, as the complexity is " +
                    "the number of parameters read, and each conjunction contains at least one parameter");
        }
        if (numberOfParameters < numberOfConjunctions) {
            throw new IllegalArgumentException("There can't be less parameters, than there are conjunctions");
        }
        if (numberOfParameters < complexity) {
            throw new IllegalArgumentException("There need to be more or an equal number of parameters, than parameters that have to be read");
        }

        final boolean expressionResult = random.nextBoolean();
        final List<List<Boolean>> expressionData = new ArrayList<>();

        // TODO If expression result is true, then a conjunction needs to be true. Then conjunction needs to be determined (random value for index of resulting conjunction, then all the complexity needs to be before that)
        final List<Integer> complexitiesOfConjunctions = splitComplexityAmongConjunctions(numberOfConjunctions, complexity);
        // TODO Generate lengths for each conjunction, which are higher or equal read-length


        // TODO Implement generation

        final String expressionString = convertExpressionDataToExpressionString(expressionData, method);
        return new Expression(expressionResult, expressionString);
    }

    List<Integer> splitComplexityAmongConjunctions(final int numberOfConjunctions, final int complexity) {
        final List<Integer> result = new ArrayList<>();
        final int numberOfSplits = numberOfConjunctions - 1;
        final int averageSizeOfConjunction = complexity / numberOfConjunctions;
        final int threshold = (int) (averageSizeOfConjunction * THRESHOLD_FACTOR);
        int lastSplitPosition = 0;
        for (int i = 0; i < numberOfSplits; i++) {
            final int relativePositionOfSplit = (random.nextInt(2) - 1) * threshold;
            final int absolutePositionOfSplit = averageSizeOfConjunction * (i + 1) + relativePositionOfSplit;
            int sizeOfConjunction = absolutePositionOfSplit - lastSplitPosition;
            if (sizeOfConjunction <= 0) {
                sizeOfConjunction = 1;
            }
            result.add(sizeOfConjunction);
            lastSplitPosition += sizeOfConjunction;
        }
        result.add(complexity - lastSplitPosition);
        return result;
    }

    String convertExpressionDataToExpressionString(final List<List<Boolean>> expression,
                                                   final Method method) throws IllegalArgumentException {
        return expression.stream()
                .map(conjunction -> convertConjunctionToString(conjunction, method))
                .collect(Collectors.joining(" || "));
    }

    private String convertConjunctionToString(final List<Boolean> conjunction,
                                              final Method method) throws IllegalArgumentException {
        final Collector<CharSequence, ?, String> joiningCollector = switch (method) {
            case BOOLEAN_NO_PARENTHESIS -> Collectors.joining(" && ");
            case BOOLEAN_WITH_PARENTHESIS -> Collectors.joining(" && ", "(", ")");
            case NESTED_IF ->
                    throw new IllegalArgumentException("This method should not be called for method NESTED_IF");
        };
        return conjunction.stream()
                .map(Object::toString)
                .collect(joiningCollector);
    }


}
