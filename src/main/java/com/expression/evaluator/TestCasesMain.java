package com.expression.evaluator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleBinaryOperator;

public class TestCasesMain {

    public static void main(String[] args) {
        DoubleBinaryOperator gg;
        IExpressionEvaluator expressionEvaluator= new ExpressionEvaluator();
        Map<String,Number> params=new HashMap<>();
        params.put("aa",Double.valueOf(100));
        params.put("bb",70);
        expressionEvaluator.getTokenReader().setParams(params);

       Object anyResult;// = expressionEvaluator.getResult(expressionEvaluator.constructTree("-9+2"));
       // anyResult = expressionEvaluator.getResult(expressionEvaluator.constructTree("-1*-9"));
        anyResult = expressionEvaluator.getResult(expressionEvaluator.constructTree("-1*-9"));
        System.out.println(anyResult.toString());


    }
}
