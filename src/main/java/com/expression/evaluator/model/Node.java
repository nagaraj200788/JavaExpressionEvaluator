package com.expression.evaluator.model;

public class Node {
    StringBuffer data;
    public Node left=null;
    public Node right=null;
    public Node singleChild=null;
    public Node(StringBuffer data)
    {
        this.data=data;
        left=right=null;
    }
    public  Node(StringBuffer data,Node left,Node right)
    {
        this.data=data;
        this.left=left;
        this.right=right;
    }

    public Node(char token) {
        this.data = new StringBuffer();
        this.data.append(token);
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

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public char[] getOperator() {
      //return this.toString().charAt(0);
       return this.toString().toCharArray();
    }
}
