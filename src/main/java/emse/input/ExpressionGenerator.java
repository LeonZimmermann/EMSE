package emse.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import emse.models.ExpressionTemplate;
import emse.models.ExpressionGenerationParameters;
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
    private static final float MIN_CONJUNCTIONS_READ_FACTOR = 0.25f;

    private final Random random;

    public ExpressionGenerator(final int seed) {
        random = new Random(seed);
    }

    /**
     * @param expressionGenerationParameters generation parameters
     * @return the construct of a boolean expression in disjunctive normal form
     */
    public ExpressionTemplate createDisjuntiveNormalformExpression(final ExpressionGenerationParameters expressionGenerationParameters) {

        final int complexity = expressionGenerationParameters.getComplexity();
        final int numberOfConjunctions = expressionGenerationParameters.getNumberOfConjunctions();
        final int numberOfParameters = expressionGenerationParameters.getNumberOfParameters();
        final boolean expressionResultIsTrue = expressionGenerationParameters.isExpressionResult();
        final Method method = expressionGenerationParameters.getMethod();

        if (complexity < numberOfConjunctions) {
            throw new IllegalArgumentException("Complexity can't be less than number of conjunctions, as the complexity is " + "the number of parameters read, and each conjunction contains at least one parameter");
        }
        if (numberOfParameters < numberOfConjunctions) {
            throw new IllegalArgumentException("There can't be less parameters, than there are conjunctions");
        }
        if (numberOfParameters < complexity) {
            throw new IllegalArgumentException("There need to be more or an equal number of parameters, than parameters that have to be read");
        }

        final List<List<Boolean>> expressionData = new ArrayList<>();
        final int numberOfConjunctionsToRead = determineNumberOfConjuctionsToRead(numberOfConjunctions, expressionResultIsTrue);
        generateConjunctionsThatHaveToBeRead(expressionData, complexity, expressionResultIsTrue, numberOfConjunctionsToRead);
        generateRedundancies(expressionData, complexity, numberOfConjunctions, numberOfParameters);
        final String expressionString = convertExpressionDataToExpressionString(expressionData, method);
        return new ExpressionTemplate(expressionGenerationParameters, expressionResultIsTrue, expressionString);
    }

    private int determineNumberOfConjuctionsToRead(int numberOfConjunctions, boolean expressionResultIsTrue) {
        int numberOfConjuctionsToRead = numberOfConjunctions;
        if (expressionResultIsTrue) {
            numberOfConjuctionsToRead *= MIN_CONJUNCTIONS_READ_FACTOR + random.nextFloat() * (1 - MIN_CONJUNCTIONS_READ_FACTOR);
            numberOfConjuctionsToRead = Math.max(numberOfConjuctionsToRead, 1);
        }
        return numberOfConjuctionsToRead;
    }

    private void generateConjunctionsThatHaveToBeRead(
            final List<List<Boolean>> expressionData,
            final int complexity,
            final boolean expressionResultIsTrue,
            final int numberOfConjuctionsToRead) {
        final List<Integer> complexitiesOfConjunctions = splitElementsAmongGroups(numberOfConjuctionsToRead, complexity);
        for (int i = 0; i < complexitiesOfConjunctions.size(); i++) {
            final boolean isTheLastConjunction = i >= complexitiesOfConjunctions.size() - 1;
            if (isTheLastConjunction) {
                expressionData.add(createConjunctionOfCertainComplexity(complexitiesOfConjunctions.get(i), expressionResultIsTrue));
            } else {
                expressionData.add(createConjunctionOfCertainComplexity(complexitiesOfConjunctions.get(i), false));
            }
        }
    }

    private void generateRedundancies(
            List<List<Boolean>> expressionData,
            int complexity,
            int numberOfConjunctions,
            int numberOfParameters) {
        final int numberOfRedundantParameters = numberOfParameters - complexity;
        final List<Integer> distributionOfRedundantParameters = splitElementsAmongGroups(numberOfConjunctions, numberOfRedundantParameters);
        for (int i = 0; i < distributionOfRedundantParameters.size(); i++) {
            final boolean conjunctionAtPositionExists = i < expressionData.size();
            final int numberOfRedundantParametersAtPosition = distributionOfRedundantParameters.get(i);
            if (conjunctionAtPositionExists) {
                addRedundantParametersToConjunction(expressionData.get(i), numberOfRedundantParametersAtPosition);
            } else {
                final List<Boolean> newRedundantConjunction = new ArrayList<>();
                addRedundantParametersToConjunction(newRedundantConjunction, numberOfRedundantParametersAtPosition);
                expressionData.add(newRedundantConjunction);
            }
        }
    }

    void addRedundantParametersToConjunction(final List<Boolean> conjunction, final int numberOfParameters) {
        for (int i = 0; i < numberOfParameters; i++) {
            conjunction.add(random.nextBoolean());
        }
    }

    List<Integer> splitElementsAmongGroups(final int numberOfGroups, final int numberOfElements) {
        if (numberOfGroups == 1) {
            return List.of(numberOfElements);
        }
        final List<Integer> result = new ArrayList<>();
        final int numberOfSplits = numberOfGroups - 1;
        final int averageSizeOfGroup = numberOfElements / numberOfGroups;
        final int threshold = (int) (averageSizeOfGroup * THRESHOLD_FACTOR);
        int lastSplitPosition = 0;
        for (int i = 0; i < numberOfSplits; i++) {
            final int relativePositionOfSplit = (random.nextInt(2) - 1) * threshold;
            final int absolutePositionOfSplit = averageSizeOfGroup * (i + 1) + relativePositionOfSplit;
            int sizeOfGroup = absolutePositionOfSplit - lastSplitPosition;
            if (sizeOfGroup <= 0) {
                sizeOfGroup = 1;
            }
            result.add(sizeOfGroup);
            lastSplitPosition += sizeOfGroup;
        }
        result.add(numberOfElements - lastSplitPosition);
        return result;
    }

    List<Boolean> createConjunctionOfCertainComplexity(final int complexity, final boolean conjunctionResultIsTrue) {
        final List<Boolean> result = new ArrayList<>();
        if (conjunctionResultIsTrue) {
            for (int i = 0; i < complexity; i++) {
                result.add(true);
            }
        } else {
            for (int i = 0; i < complexity - 1; i++) {
                result.add(true);
            }
            result.add(false);
        }
        return result;
    }

    String convertExpressionDataToExpressionString(final List<List<Boolean>> expression, final Method method) throws IllegalArgumentException {
        return expression.stream().map(conjunction -> convertConjunctionToString(conjunction, method)).collect(Collectors.joining(" || "));
    }

    private String convertConjunctionToString(final List<Boolean> conjunction, final Method method) throws IllegalArgumentException {
        final Collector<CharSequence, ?, String> joiningCollector = switch (method) {
            case BOOLEAN_NO_PARENTHESIS -> Collectors.joining(" && ");
            case BOOLEAN_WITH_PARENTHESIS -> Collectors.joining(" && ", "(", ")");
        };
        return conjunction.stream().map(Object::toString).collect(joiningCollector);
    }


}
