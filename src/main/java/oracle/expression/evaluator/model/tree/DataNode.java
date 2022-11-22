package oracle.expression.evaluator.model.tree;

public class DataNode extends Node {
    StringBuffer data;
    public Node left=null;
    public Node right=null;

    public DataNode(StringBuffer data)
    {
        this.data=data;
        left=right=null;
    }


    @Override
    public String toString() {
        return data.toString();
    }

    public StringBuffer getData() {
        return data;
    }

    public void setData(StringBuffer data) {
        this.data = data;
    }

    @Override
    public Node getLeft() {
        return left;
    }

    @Override
    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    @Override
    public void setRight(Node right) {
        this.right = right;
    }

    public char[] getOperator() {
        //return this.toString().charAt(0);
        return this.toString().toCharArray();
    }

}
