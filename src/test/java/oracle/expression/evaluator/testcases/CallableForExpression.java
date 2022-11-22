package oracle.expression.evaluator.testcases;

import oracle.expression.evaluator.ExpressionEvaluator;
import oracle.expression.evaluator.model.tree.Node;

import java.util.concurrent.Callable;

public class CallableForExpression implements Callable<Object> {
    Node root;
    public CallableForExpression(Node root){
        this.root=root;
    }
    @Override
    public Object call() throws Exception {
        ExpressionEvaluator expressionEvaluator= new ExpressionEvaluator();
        return expressionEvaluator.getResultFromEvaluatedTree(root);
    }
}
