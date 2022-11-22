package oracle.expression.evaluator.model.tree;

public abstract class FunctionNode extends Node {
    public abstract double performOperation(double doubleValue);

    public abstract Node getSingleChild();

    public abstract void setSingleChild(Node singleChild);
}
