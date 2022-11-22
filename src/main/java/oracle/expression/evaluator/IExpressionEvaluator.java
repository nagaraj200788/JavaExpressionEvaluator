package oracle.expression.evaluator;

import oracle.expression.evaluator.model.ResultDecorator;
import oracle.expression.evaluator.model.tree.Node;
import oracle.expression.evaluator.reader.TokenReader;

public interface IExpressionEvaluator {
    Node constructTree(String expressionVal);
    ResultDecorator evaluateTree (Node root) throws  Exception;
    Object getResultFromEvaluatedTree(Node root) ;
    TokenReader getTokenReader();

}
