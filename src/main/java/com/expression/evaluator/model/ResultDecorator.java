package com.expression.evaluator.model;

public class ResultDecorator {
    String typeOfexpresion;
    Number result;

    public ResultDecorator()
    {
        this.result=null;
        this.typeOfexpresion=null;
    }
    public ResultDecorator(Number value,String typeOfexpresion)
    {
        this.result=value;
        this.typeOfexpresion=typeOfexpresion;
    }

    public ResultDecorator(Number value)
    {
        this.result=value;
    }

    public String getTypeOfexpresion() {
        return typeOfexpresion;
    }

    public void setTypeOfexpresion(String typeOfexpresion) {
        this.typeOfexpresion = typeOfexpresion;
    }

    public Number getResult() {
        return result;
    }

    public void setResult(Number result) {
        this.result = result;
    }
}
