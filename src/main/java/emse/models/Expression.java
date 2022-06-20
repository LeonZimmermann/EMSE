package emse.models;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Expression {
    public int id;
    public ExpressionTemplate template;
}
