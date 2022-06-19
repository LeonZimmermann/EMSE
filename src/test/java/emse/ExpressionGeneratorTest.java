package emse;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import emse.models.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

public class ExpressionGeneratorTest {

    private static final Logger logger = Logger.getLogger(ExpressionGeneratorTest.class.getName());
    private final ExpressionGenerator expressionGenerator = new ExpressionGenerator(1000);

    @BeforeClass
    public static void setup() throws IOException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yMdHms");
        final String timestamp = dateFormat.format(Date.from(Instant.now()));
        final String path = "logs/ExpressionGeneratorTest-" + timestamp;
        final File file = new File(path);
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Cannot write logs to file");
        }
        final FileHandler fileHandler = new FileHandler(path);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
    }

    @Test
    public void testSplitComplexityAmongConjunctions() {
        for (int i = 0; i < 5; i++) {
            final int numberOfConjunctions = 2 + i;
            final int complexity = 15 + i;
            for (int j = 0; j < 10; j++) {
                final List<Integer> result = expressionGenerator.splitComplexityAmongConjunctions(numberOfConjunctions, complexity);
                final int sum = result.stream().reduce(0, Integer::sum);
                assertThat(result).doesNotContain(0);
                assertEquals(complexity, sum);
                logger.log(Level.INFO, "Given " + numberOfConjunctions + " conjunctions and a complexity of " + complexity + ", the result was: " + result);
            }
        }
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