package emse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import emse.models.CodeSample;
import emse.models.Method;

import static emse.models.Method.BOOLEAN_NO_PARENTHESIS;
import static emse.models.Method.BOOLEAN_WITH_PARENTHESIS;
import static emse.models.Method.NESTED_IF;

public class CodeLoader {

    private final Random random = new Random();
    private int methodIterator = 0;

    private final List<CodeSample> nestedIfs = loadCode("/code/nestedIfs.txt", NESTED_IF);
    private final List<CodeSample> booleanNoParenthesis = loadCode("/code/booleanNoParenthesis.txt", BOOLEAN_NO_PARENTHESIS);
    private final List<CodeSample> booleanWithParanthesis = loadCode("/code/booleanWithParenthesis.txt", BOOLEAN_WITH_PARENTHESIS);

    private List<CodeSample> loadCode(final String url, final Method method) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(url)))) {
            var arrayOfCodeSamples = Arrays.stream(reader.lines()
                    .collect(Collectors.joining("\n"))
                    .split("#"))
                    .collect(Collectors.toList());
            List<CodeSample> result = new ArrayList<>();
            for (int i = 0; i < arrayOfCodeSamples.size(); i++) {
                final var resultCodePair = arrayOfCodeSamples.get(i).split(":");
                final boolean printing = Boolean.parseBoolean(resultCodePair[0].trim());
                final String code = resultCodePair[1];
                result.add(new CodeSample(i, method, code, printing));
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CodeSample getCodeSample() {
        final int currentMethod = methodIterator % Method.values().length;
        methodIterator++;
        return switch (Method.values()[currentMethod]) {
            case NESTED_IF -> nestedIfs.get(random.nextInt(nestedIfs.size()));
            case BOOLEAN_NO_PARENTHESIS -> booleanNoParenthesis.get(random.nextInt(nestedIfs.size()));
            case BOOLEAN_WITH_PARENTHESIS -> booleanWithParanthesis.get(random.nextInt(nestedIfs.size()));
        };
    }
}
