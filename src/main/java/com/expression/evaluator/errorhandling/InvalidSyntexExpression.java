package com.expression.evaluator.errorhandling;

public class InvalidSyntexExpression extends Exception{
    public InvalidSyntexExpression(String message)
    {
        super(message);
    }
}
