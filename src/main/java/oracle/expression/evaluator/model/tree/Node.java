package oracle.expression.evaluator.model.tree;

public abstract class Node {

    public abstract String toString();

    public abstract StringBuffer getData() ;

    public abstract void setData(StringBuffer data);

    public abstract Node getLeft();

    public abstract void setLeft(Node left) ;

    public abstract Node getRight() ;

    public abstract void setRight(Node right);

    public abstract char[] getOperator();

}
