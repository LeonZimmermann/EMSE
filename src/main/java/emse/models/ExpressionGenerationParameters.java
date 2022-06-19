package emse.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class ExpressionGenerationParameters {

    private static final int DEFAULT_NUMBER_OF_CONJUNCTIONS = 3;
    private static final int DEFAULT_NUMBER_OF_PARAMETERS = 14;
    private static final int DEFAULT_COMPLEXITY = 10;

    private int numberOfConjunctions = DEFAULT_NUMBER_OF_CONJUNCTIONS;
    private int numberOfParameters = DEFAULT_NUMBER_OF_PARAMETERS;
    private int complexity = DEFAULT_COMPLEXITY;
    private boolean expressionResult;
    private Method method;
}
