package oracle.expression.evaluator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleBinaryOperator;

public class TestCasesMain {

    public static void main(String[] args) throws Exception {
        DoubleBinaryOperator gg;
        IExpressionEvaluator expressionEvaluator= new ExpressionEvaluator();
        Map<String,Number> params=new HashMap<>();
        params.put("aa",Double.valueOf(100));
        params.put("bb",70);
        expressionEvaluator.getTokenReader().setParams(params);

       Object anyResult = expressionEvaluator.getResultFromEvaluatedTree(expressionEvaluator.constructTree("1*8+6"));
      // anyResult = expressionEvaluator.getResultFromEvaluatedTree(expressionEvaluator.constructTree("-1*-9"));
       // anyResult = expressionEvaluator.getResultFromEvaluatedTree(expressionEvaluator.constructTree("20*(1/1-20*(1/2-20*(1/3-20*(1/4-20*(1/5-20*(1/6-20*(1/7-20*(1/8-20*(1/9-20*(1/10-20*(1/11-20*(1/12-20*(1/13-20*(1/14-20/15))))))))))))))"));
        System.out.println(anyResult.toString());


    }
}
