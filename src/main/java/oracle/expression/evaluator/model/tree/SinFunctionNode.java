package oracle.expression.evaluator.model.tree;

public class SinFunctionNode extends FunctionNode {
    StringBuffer data;
    public Node left=null;
    public Node right=null;
    public Node singleChild=null;
    public SinFunctionNode(StringBuffer data)
    {
        this.data=data;
        left=right=null;
    }


    @Override
    public String toString() {
        return data.toString();
    }
    @Override
    public StringBuffer getData() {
        return data;
    }
    @Override
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
    @Override
    public Node getRight() {
        return right;
    }
    @Override
    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public char[] getOperator() {
        //return this.toString().charAt(0);
        return this.toString().toCharArray();
    }

    @Override
    public Node getSingleChild() {
        return singleChild;
    }

    @Override
    public void setSingleChild(Node singleChild) {
        this.singleChild = singleChild;
    }

    @Override
    public double performOperation(double doubleValue) {
        return Math.sin(doubleValue);
    }
}
