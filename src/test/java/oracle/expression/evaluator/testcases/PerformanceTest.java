package oracle.expression.evaluator.testcases;

import oracle.expression.evaluator.ExpressionEvaluator;
import oracle.expression.evaluator.IExpressionEvaluator;
import oracle.expression.evaluator.model.tree.Node;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


public class PerformanceTest {
    static IExpressionEvaluator expressionEvaluator;

    static int runNNumberOfTimes=100000;
    static {
        expressionEvaluator= new ExpressionEvaluator();
    }
    public static void main(String[] args)  {
        PerformanceTest.testExpressionRunTime();
    }

    public static void testExpressionRunTime(){

        int counter=0;
        long startTime=System.currentTimeMillis(),evalStart=0L,evalEnd=0l,evalTotal=0l;
        Node node = expressionEvaluator.constructTree("20*(1/1-20*(1/2-20*(1/3-20*(1/4-20*(1/5-20*(1/6-20*(1/7-20*(1/8-20*(1/9-20*(1/10-20*(1/11-20*(1/12-20*(1/13-20*(1/14-20/15))))))))))))))");
       long treeEndTime=System.currentTimeMillis();
      //  Node node = expressionEvaluator.constructTree("1+9");
        for (int i=0;i<runNNumberOfTimes;i++){

            evalStart=System.currentTimeMillis();
            Object result = expressionEvaluator.getResultFromEvaluatedTree(node);

            evalEnd=System.currentTimeMillis();
            evalTotal=evalTotal+(evalEnd-evalStart);
            if("2.07348261507646669E18".equalsIgnoreCase(result.toString())){
                counter++;
           }
        }
        long endTime=System.currentTimeMillis();
        System.out.println("Tree time(seconds)="+ TimeUnit.MILLISECONDS.toSeconds(treeEndTime-startTime));
        System.out.println("Tree  time(Milliseconds)="+ (treeEndTime-startTime));

        System.out.println("Eval  time(seconds)="+ TimeUnit.MILLISECONDS.toSeconds(evalTotal));
        System.out.println("Eval time(Milliseconds)="+ (evalTotal));


        System.out.println("counter="+ counter +"  "+"Total time(seconds)="+ TimeUnit.MILLISECONDS.toSeconds(endTime-startTime));
        System.out.println("counter="+ counter +"  "+"Total time(Milliseconds)="+ (endTime-startTime));

        System.out.println("applyOperator spent time(Milliseconds)="+ ExpressionEvaluator.applyOperatorTotalTime);
        System.out.println("applyOperator spent time(seconds)="+ TimeUnit.MILLISECONDS.toSeconds(ExpressionEvaluator.applyOperatorTotalTime));


        System.out.println("isValuesFitIntoPrimitiveTypes spent time(Milliseconds)="+ ExpressionEvaluator.isValuesFitIntoPrimitiveTypes);
        System.out.println("isValuesFitIntoPrimitiveTypes spent time(seconds)="+ TimeUnit.MILLISECONDS.toSeconds(ExpressionEvaluator.isValuesFitIntoPrimitiveTypes));

    }

}
