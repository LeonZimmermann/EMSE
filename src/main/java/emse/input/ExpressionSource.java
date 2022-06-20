package emse.input;

import emse.models.Expression;
import emse.models.ExpressionGenerationParameters;
import emse.models.Method;

public interface ExpressionSource {
    void setMethod(final Method method);
    Expression getNext();
}
