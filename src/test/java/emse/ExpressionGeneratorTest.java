package emse;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import emse.models.Method;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

public class ExpressionGeneratorTest {

    private final ExpressionGenerator expressionGenerator = new ExpressionGenerator(1000);

    // TODO Is this testable, since it is random?
    @Test
    public void testCreateDisjuntiveNormalformExpression() {
        final int numberOfConjunctions = 3;
        final int complexity = 10;
        expressionGenerator.createDisjuntiveNormalformExpression(numberOfConjunctions, complexity, Method.BOOLEAN_WITH_PARENTHESIS);
    }

    @Test
    public void testConvertExpressionDataToString() {
        final List<List<Boolean>> expression = new ArrayList<>();
        expression.add(Arrays.asList(false, true, false));
        expression.add(Arrays.asList(true, false));
        expression.add(Arrays.asList(false, false, true, true, true));
        assertEquals("(false && true && false) || (true && false) || (false && false && true && true && true)",
                expressionGenerator.convertExpressionDataToExpressionString(expression, Method.BOOLEAN_WITH_PARENTHESIS));
        assertEquals("false && true && false || true && false || false && false && true && true && true",
                expressionGenerator.convertExpressionDataToExpressionString(expression, Method.BOOLEAN_NO_PARENTHESIS));
        assertThatThrownBy(() -> expressionGenerator.convertExpressionDataToExpressionString(expression, Method.NESTED_IF))
                .hasSameClassAs(new IllegalArgumentException())
                .hasMessage("This method should not be called for method NESTED_IF");
    }

}