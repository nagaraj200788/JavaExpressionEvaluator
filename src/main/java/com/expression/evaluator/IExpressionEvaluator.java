package com.expression.evaluator;

import com.expression.evaluator.model.Node;
import com.expression.evaluator.model.ResultDecorator;
import com.expression.evaluator.reader.TokenReader;

public interface IExpressionEvaluator {
    public Node constructTree(String expressionVal);
    public ResultDecorator evaluateTree (Node root);
    public Object getResult(Node root);
    public TokenReader getTokenReader();
}
