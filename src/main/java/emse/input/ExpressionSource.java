package emse.input;

import emse.models.Expression;
import emse.models.ExpressionGenerationParameters;

public interface ExpressionSource {
    Expression getNext();
}
