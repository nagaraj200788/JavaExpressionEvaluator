package oracle.expression.evaluator.testcases;

import oracle.expression.evaluator.ExpressionEvaluator;
import oracle.expression.evaluator.IExpressionEvaluator;
import oracle.expression.evaluator.model.tree.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class PerformanceTestMultiThreaded {
    static IExpressionEvaluator expressionEvaluator;

    static int runNNumberOfTimes=100000;
    static {
        expressionEvaluator= new ExpressionEvaluator();
    }
    public static void main(String[] args)  {
        PerformanceTestMultiThreaded.testExpressionRunTime();
    }

    public static void testExpressionRunTime(){

        AtomicInteger counter= new AtomicInteger();
        long startTime=System.currentTimeMillis(),evalStart=0L,evalEnd=0l,evalTotal=0l;
        Node node = expressionEvaluator.constructTree("20*(1/1-20*(1/2-20*(1/3-20*(1/4-20*(1/5-20*(1/6-20*(1/7-20*(1/8-20*(1/9-20*(1/10-20*(1/11-20*(1/12-20*(1/13-20*(1/14-20/15))))))))))))))");
        List<Future<Object>> runs=new LinkedList<>();
        int corePoolSize = 3;
        int maximumPoolSize = 8;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        for (int i=0;i<runNNumberOfTimes;i++){
         runs.add(threadPoolExecutor.submit(new CallableForExpression(node)));
        }
        long taskCount = threadPoolExecutor.getTaskCount();
        runs.parallelStream().forEach(f->{
            try {
                Object o = f.get();
                if("2.07348261507646669E18".equalsIgnoreCase(o.toString())){
                    counter.getAndIncrement();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        long endTime=System.currentTimeMillis();


        System.out.println("counter="+ counter +"  "+"Total time(seconds)="+ TimeUnit.MILLISECONDS.toSeconds(endTime-startTime));
        System.out.println("counter="+ counter +"  "+"Total time(Milliseconds)="+ (endTime-startTime));
        if(threadPoolExecutor.getCompletedTaskCount()==taskCount){
         threadPoolExecutor.shutdown();
        }
    }

}
