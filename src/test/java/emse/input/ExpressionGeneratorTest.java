package emse.input;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import emse.models.Expression;
import emse.models.ExpressionGenerationParameters;
import emse.models.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

public class ExpressionGeneratorTest {

    private static final Logger logger = Logger.getLogger(ExpressionGeneratorTest.class.getName());
    private final ExpressionGenerator expressionGenerator = new ExpressionGenerator(1000);

    @BeforeClass
    public static void setup() throws IOException {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s %n");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yMdHms");
        final String timestamp = dateFormat.format(Date.from(Instant.now()));
        final String path = "logs/ExpressionGeneratorTest-" + timestamp;
        final File file = new File(path);
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Cannot write logs to file");
        }
        final FileHandler fileHandler = new FileHandler(path);
        logger.addHandler(fileHandler);
    }

    @Test
    public void testCreateDisjuntiveNormalformExpression() {
        for (int i = 0; i < 5; i++) {
            final Expression expression = expressionGenerator.createDisjuntiveNormalformExpression(new ExpressionGenerationParameters().toBuilder()
                    .expressionResult(false)
                    .method(Method.BOOLEAN_WITH_PARENTHESIS)
                    .build());
            logger.info("Generated Expression: " + expression);
        }
        for (int i = 0; i < 5; i++) {
            final Expression expression = expressionGenerator.createDisjuntiveNormalformExpression(new ExpressionGenerationParameters().toBuilder()
                    .expressionResult(true)
                    .method(Method.BOOLEAN_WITH_PARENTHESIS)
                    .build());
            logger.info("Generated Expression: " + expression);
        }
        for (int i = 0; i < 5; i++) {
            final Expression expression = expressionGenerator.createDisjuntiveNormalformExpression(new ExpressionGenerationParameters().toBuilder()
                    .expressionResult(false)
                    .method(Method.BOOLEAN_NO_PARENTHESIS)
                    .build());
            logger.info("Generated Expression: " + expression);
        }
        for (int i = 0; i < 5; i++) {
            final Expression expression = expressionGenerator.createDisjuntiveNormalformExpression(new ExpressionGenerationParameters().toBuilder()
                    .expressionResult(true)
                    .method(Method.BOOLEAN_NO_PARENTHESIS)
                    .build());
            logger.info("Generated Expression: " + expression);
        }
    }

    @Test
    public void testSplitElementsAmongGroups() {
        for (int i = 0; i < 5; i++) {
            final int numberOfConjunctions = 2 + i;
            final int complexity = 15 + i;
            for (int j = 0; j < 10; j++) {
                final List<Integer> result = expressionGenerator.splitElementsAmongGroups(numberOfConjunctions, complexity);
                final int sum = result.stream().reduce(0, Integer::sum);
                assertThat(result).doesNotContain(0);
                assertEquals(complexity, sum);
                logger.info("Given " + numberOfConjunctions + " conjunctions and a complexity of " + complexity + ", the result was: " + result);
            }
        }
    }

    @Test
    public void testCreateConjunctionOfCertainComplexity() {
        assertEquals(List.of(true), expressionGenerator.createConjunctionOfCertainComplexity(1, true));
        assertEquals(List.of(false), expressionGenerator.createConjunctionOfCertainComplexity(1, false));
        assertEquals(List.of(true, true, true), expressionGenerator.createConjunctionOfCertainComplexity(3, true));
        assertEquals(List.of(true, true, false), expressionGenerator.createConjunctionOfCertainComplexity(3, false));
        assertEquals(List.of(true, true, true, true), expressionGenerator.createConjunctionOfCertainComplexity(4, true));
        assertEquals(List.of(true, true, true, false), expressionGenerator.createConjunctionOfCertainComplexity(4, false));
        assertEquals(List.of(true, true, true, true, true), expressionGenerator.createConjunctionOfCertainComplexity(5, true));
        assertEquals(List.of(true, true, true, true, false), expressionGenerator.createConjunctionOfCertainComplexity(5, false));
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