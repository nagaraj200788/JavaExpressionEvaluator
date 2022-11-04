package com.expression.evaluator.testcases;

import com.expression.evaluator.ExpressionEvaluator;
import com.expression.evaluator.model.Node;
import com.expression.evaluator.IExpressionEvaluator;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class ExpressionsTest {
    static IExpressionEvaluator expressionEvaluator;
    static {
        expressionEvaluator= new ExpressionEvaluator();
    }
    @Test
    public void test1(){
        assertEquals(Double.valueOf(48),expressionEvaluator.getResult(expressionEvaluator.constructTree("1*9+9/3+4-4+6*6")));
    }
    @Test
    public void test2(){
        assertEquals(Long.valueOf(300),expressionEvaluator.getResult(expressionEvaluator.constructTree("100+200")));
    }
    @Test
    public void test3(){
        assertEquals(Long.valueOf(-100),expressionEvaluator.getResult(expressionEvaluator.constructTree("(100-200)")));
    }

    @Test
    public void test4(){
        assertEquals(Double.valueOf(26.4),expressionEvaluator.getResult(expressionEvaluator.constructTree("(23.4+3)")));
    }
    @Test
    public void test5(){
        assertEquals(Long.valueOf(256),expressionEvaluator.getResult(expressionEvaluator.constructTree("2^2^3")));

    }
    @Test
    public void test6(){
        assertEquals(Long.valueOf(134217728),expressionEvaluator.getResult(expressionEvaluator.constructTree("8^9")));
    }
    @Test
    public void test7(){
        Node root = expressionEvaluator.constructTree("aa+bb");
        Map<String,Number> params=new HashMap<>();
        params.put("aa",Double.valueOf(23.1));
        params.put("bb",1);
        expressionEvaluator.getTokenReader().setParams(params);
        assertEquals(Double.valueOf(24.1),expressionEvaluator.getResult(root));
        params.put("bb",3);
        assertEquals(Double.valueOf(26.1),expressionEvaluator.getResult(root));
    }
    @Test
    public void test8(){
        assertEquals(Long.valueOf(4),expressionEvaluator.getResult(expressionEvaluator.constructTree(" 1+3")));
    }
    @Test
    public void test9(){
        assertEquals(Long.valueOf(80),expressionEvaluator.getResult(expressionEvaluator.constructTree("     90-       10")));
    }
    @Test
    public void test10(){
        assertEquals(Long.valueOf(-10),expressionEvaluator.getResult(expressionEvaluator.constructTree("     90-       100")));
    }
    @Test
    public void test11(){
        assertEquals(Long.valueOf(245),expressionEvaluator.getResult(expressionEvaluator.constructTree("    2 3 6 + 9")));
    }
    @Test
    public void testsin90(){
        assertEquals(Double.valueOf(0.8939966636005579),expressionEvaluator.getResult(expressionEvaluator.constructTree("sin(90)")));
    }

    @Test
    public void testsinExpression(){
        assertEquals(Double.valueOf(-0.26237485370392877),expressionEvaluator.getResult(expressionEvaluator.constructTree("sin(40+50/5)")));
    }
    @Test
    public void testsinExpressionWithAnotherexpression(){
        assertEquals(Double.valueOf(3.893996663600558),expressionEvaluator.getResult(expressionEvaluator.constructTree("1+sin(90)+2")));
    }
    @Test
    public void testcos90(){
        assertEquals(Double.valueOf(-0.4480736161291701),expressionEvaluator.getResult(expressionEvaluator.constructTree("cos(90)")));
    }
    @Test
    public void testtan90(){
        assertEquals(Double.valueOf(-1.995200412208242),expressionEvaluator.getResult(expressionEvaluator.constructTree("tan(90)")));
    }
    @Test
    public void testLessThanOperator(){
        assertEquals(Boolean.valueOf(false),expressionEvaluator.getResult(expressionEvaluator.constructTree("8*7<3*7")));
    }

    @Test
    public void testLessThanOperator2(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("1+9+8+9-2<1+9+8+9-2+1")));
    }

    @Test
    public void testGreaterThanOperator(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("8*7>3*7")));
    }
    @Test
    public void testGreaterThanOperator2(){
        assertEquals(Boolean.valueOf(false),expressionEvaluator.getResult(expressionEvaluator.constructTree("1+9+8+9-2>1+9+8+9-2+1")));
    }
    @Test
    public void testEqualOperator(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("10==10")));
    }
    @Test
    public void testLessThanEqualOperator(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("1+9+8+9-2<=1+9+8+9-2")));
    }
    @Test
    public void testLessThanEqualOperator1(){
        assertEquals(Boolean.valueOf(false),expressionEvaluator.getResult(expressionEvaluator.constructTree("1+9+8+9-2+100<=1+9+8+9-2")));
    }
    @Test
    public void testGreaterThanEqualOperator(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("1+9+8+9-2+100>=1+9+8+9-2")));
    }

    @Test
    public void testGreaterThanEqualOperator1(){
        assertEquals(Boolean.valueOf(false),expressionEvaluator.getResult(expressionEvaluator.constructTree("1+9+8+9-2+100>=1+9+8+9-2+3000")));
    }

    @Test
    public void testDoubleAndBoolean(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("13433443==13433443")));
    }
    @Test
    public void testAnd(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("3==3&2==2")));
    }
    @Test
    public void testAndAnd(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("3==3&&2==2")));
    }
    @Test
    public void testAndFalse(){
        assertEquals(Boolean.valueOf(false),expressionEvaluator.getResult(expressionEvaluator.constructTree("9==3&4==2")));
    }
    @Test
    public void testAndAndFalse(){
        assertEquals(Boolean.valueOf(false),expressionEvaluator.getResult(expressionEvaluator.constructTree("39==3&&92==2")));
    }
    @Test
    public void testEqualWIthExpression(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("1*9+9/3+4-4+6*6==1*9+9/3+4-4+6*6")));
    }
    @Test
    public void testEqualWIthExpression1(){
        assertEquals(Boolean.valueOf(false),expressionEvaluator.getResult(expressionEvaluator.constructTree("((23+1*9+9/3+4-4+6*6)==(1*9+9/3+4-4+6*6))")));
    }
    @Test
    public void testEqualWIthExpression2(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("(1*9+9/3+4-4+6*6)==(1*9+9/3+4-4+6*6)")));
    }
    @Test
    public void testANDBooleanExpression(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("1*9+9/3+4-4+6*6==1*9+9/3+4-4+6*6 && 1*9+9/3+4-4+6*6==1*9+9/3+4-4+6*6")));
    }
    @Test
    public void testORBooleanExpression(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("1*9+9/3+4-4+6*6==1*9+9/3+4-4+6*6 | 1*9+9/3+4-4+6*6==1*9+9/3+4-4+6*6")));
    }
    @Test
    public void testORBooleanExpression1(){
        assertEquals(Boolean.valueOf(false),expressionEvaluator.getResult(expressionEvaluator.constructTree("10==130 || 33==3")));
    }
    @Test
    public void testComplexArithMatic(){
        //2073482615076466700L
        assertEquals(Double.valueOf(2.07348261507646669E18),expressionEvaluator.getResult(expressionEvaluator.constructTree("20*(1/1-20*(1/2-20*(1/3-20*(1/4-20*(1/5-20*(1/6-20*(1/7-20*(1/8-20*(1/9-20*(1/10-20*(1/11-20*(1/12-20*(1/13-20*(1/14-20/15))))))))))))))")));
    }

    @Test
    public void testANDORBooleanExpression1(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("1==3 || (3==3 && 5==5)")));
    }

    @Test
    public void testANDORBooleanExpression2(){
        assertEquals(Boolean.valueOf(false),expressionEvaluator.getResult(expressionEvaluator.constructTree("1==3 || (33==3 && 53==5)")));
    }

    @Test
    public void testNotEquals(){
        assertEquals(Boolean.valueOf(false),expressionEvaluator.getResult(expressionEvaluator.constructTree("22 != 22")));
    }

    @Test
    public void testNotEquals1(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("22 == 22")));
    }
    @Test
    public void testNotEquals2(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("(1==3 || (3==3 && 5==5)) == (1==3 || (3==3 && 5==5))")));
    }

    @Test
    public void testNotEquals3(){
        assertEquals(Boolean.valueOf(false),expressionEvaluator.getResult(expressionEvaluator.constructTree("(1==3 || (3==3 && 5==5)) != (1==3 || (3==3 && 5==5))")));
    }

    @Test
    public void divideByZero(){
        try {
             expressionEvaluator.getResult(expressionEvaluator.constructTree("23/0"));
        }catch (ArithmeticException e)
        {
            assertEquals("/ by zero",e.getMessage());
        }
    }

    @Test
    public void ternaryOperator(){
        assertEquals(Long.valueOf(43),expressionEvaluator.getResult(expressionEvaluator.constructTree("1==2 ? 4 : 43")));
    }

    @Test
    public void ternaryOperator1(){
        assertEquals(Long.valueOf(4),expressionEvaluator.getResult(expressionEvaluator.constructTree("2==2 ? 4 : 43")));
    }

    @Test
    public void ternaryOperator2(){
        assertEquals(Double.valueOf(48),expressionEvaluator.getResult(expressionEvaluator.constructTree("(1*9+9/3+4-4+6*6)==(1*9+9/3+4-4+6*6) ? (1*9+9/3+4-4+6*6) : (1*9+9/3+4-4+6*6+2)")));
    }

    @Test
    public void ternaryOperator3(){
        assertEquals(Double.valueOf(50),expressionEvaluator.getResult(expressionEvaluator.constructTree("(1*9+9/3+4-4+6*6)==(1*9+9/3+4-4+6*6+1) ? (1*9+9/3+4-4+6*6) : (1*9+9/3+4-4+6*6+2)")));
    }

    @Test
    public void ternaryOperator4(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("(1*9+9/3+4-4+6*6)==(1*9+9/3+4-4+6*6) ? (33==33) : (44==44)")));
    }

    @Test
    public void ternaryOperator5(){
        assertEquals(Boolean.valueOf(false),expressionEvaluator.getResult(expressionEvaluator.constructTree("(1*9+9/3+4-4+6*6)==(1*9+9/3+4-4+6*6+3) ? (33==33) : (44!=44)")));
    }

    @Test
    public void ternaryOperator6(){
        assertEquals(Long.valueOf(78),expressionEvaluator.getResult(expressionEvaluator.constructTree("(20*(1/1-20*(1/2-20*(1/3-20*(1/4-20*(1/5-20*(1/6-20*(1/7-20*(1/8-20*(1/9-20*(1/10-20*(1/11-20*(1/12-20*(1/13-20*(1/14-20/15)))))))))))))))==(20*(1/1-20*(1/2-20*(1/3-20*(1/4-20*(1/5-20*(1/6-20*(1/7-20*(1/8-20*(1/9-20*(1/10-20*(1/11-20*(1/12-20*(1/13-20*(1/14-20/15))))))))))))))) ? 78 : 100 ")));
    }

    @Test
    public void ternaryOperator7(){
        assertEquals(Long.valueOf(100),expressionEvaluator.getResult(expressionEvaluator.constructTree("34==35 ? 78 : 100")));
    }

    @Test
    public void ternaryOperator8(){
        assertEquals(Long.valueOf(90),expressionEvaluator.getResult(expressionEvaluator.constructTree("34==35 ? 78 : 100 - 10")));
    }
    @Test
    public void ternaryOperator9(){
        assertEquals(Long.valueOf(68),expressionEvaluator.getResult(expressionEvaluator.constructTree("(34==34 ? 78 : 100) - 10")));
    }

    @Test
    public void ternaryOperator10(){
        assertEquals(Long.valueOf(69),expressionEvaluator.getResult(expressionEvaluator.constructTree("(34==34 ? 78+1 : 10) - 10")));
    }

    @Test
    public void ternaryOperator11(){
        assertEquals(Long.valueOf(79),expressionEvaluator.getResult(expressionEvaluator.constructTree("34==34 ? 78+1 : 10")));
    }

    @Test
    public void ternaryOperator12(){
        assertEquals(Long.valueOf(-7),expressionEvaluator.getResult(expressionEvaluator.constructTree("34!=34 ? 78+1 : 3-10")));
    }
    @Test
    public void ternaryOperator13(){
        Node root = expressionEvaluator.constructTree("aa+bb==bb+aa ? 24.1 : 55");
        Map<String,Number> params=new HashMap<>();
        params.put("aa",Double.valueOf(23.1));
        params.put("bb",1);
        expressionEvaluator.getTokenReader().setParams(params);
        assertEquals(Double.valueOf(24.1),expressionEvaluator.getResult(root));
        root = expressionEvaluator.constructTree("aa+bb==bb+aa+1 ? aa+bb : bb-bb");
        params.put("bb",3);
        assertEquals(Long.valueOf(0),expressionEvaluator.getResult(root));
    }

    @Test
    public void ternaryOperator14(){
        assertEquals(Boolean.valueOf(true),expressionEvaluator.getResult(expressionEvaluator.constructTree("34==34 ? 2==2 : 3==3")));
    }

    @Test
    public void ternaryOperator15(){
        assertEquals(Long.valueOf(58),expressionEvaluator.getResult(expressionEvaluator.constructTree("34!=34 ? 2==2 :( 3==3 ? 56+2 : 99)")));
    }

    @Test
    public void ternaryOperator16(){
        assertEquals(Long.valueOf(99),expressionEvaluator.getResult(expressionEvaluator.constructTree("34==34 ? 22==2 : 3==3 ? 56+2 : 99")));
    }

    @Test
    public void ternaryOperator17(){
        assertEquals(Long.valueOf(4),expressionEvaluator.getResult(expressionEvaluator.constructTree("34==34 ? 22==2 : 3==3 ? 56+2 : 99==99 ? 1+1+1+1 : 45")));
    }

    @Test
    public void ternaryOperator18(){
        assertEquals(Long.valueOf(45),expressionEvaluator.getResult(expressionEvaluator.constructTree("34==34 ? 22==2 : 3==3 ? 56+2 : 100==99 ? 1+1+1+1 : 45")));
    }
    @Test
    public void testBigInteger(){
        assertEquals(new BigInteger("922337203685477580669"),expressionEvaluator.getResult(expressionEvaluator.constructTree("922337203685477580667+2")));
    }
    @Test
    public void testBigIntegerSub(){
        assertEquals(new BigInteger("922337203685477580665"),expressionEvaluator.getResult(expressionEvaluator.constructTree("922337203685477580667-2")));
    }

    @Test
    public void testBigIntegerMultiply(){
        assertEquals(new BigInteger("1844674407370955161334"),expressionEvaluator.getResult(expressionEvaluator.constructTree("922337203685477580667*2")));
    }

    @Test
    public void testBigIntegerDivide(){
        assertEquals(new BigInteger("461168601842738790333"),expressionEvaluator.getResult(expressionEvaluator.constructTree("922337203685477580667/2")));
    }

    @Test
    public void testBigDecimal(){
        assertEquals(new BigDecimal("179769313486231570814527423731704356798070567525844996598917476803157260780028538760589558632766878171540458953514382464234321326889464182768467546703537516986049910576551282076245490090389328944075868508455133942304583236903222948165808559332123348274797826204144723168738177180919299881250404026184124858369.1"),expressionEvaluator.getResult(expressionEvaluator.constructTree("179769313486231570814527423731704356798070567525844996598917476803157260780028538760589558632766878171540458953514382464234321326889464182768467546703537516986049910576551282076245490090389328944075868508455133942304583236903222948165808559332123348274797826204144723168738177180919299881250404026184124858368.1+1")));
    }
    @Test
    public void testBigDecimalSub(){
        assertEquals(new BigDecimal("179769313486231570814527423731704356798070567525844996598917476803157260780028538760589558632766878171540458953514382464234321326889464182768467546703537516986049910576551282076245490090389328944075868508455133942304583236903222948165808559332123348274797826204144723168738177180919299881250404026184124858367.1"),expressionEvaluator.getResult(expressionEvaluator.constructTree("179769313486231570814527423731704356798070567525844996598917476803157260780028538760589558632766878171540458953514382464234321326889464182768467546703537516986049910576551282076245490090389328944075868508455133942304583236903222948165808559332123348274797826204144723168738177180919299881250404026184124858368.1-1")));
    }
    @Test
    public void singedNumberExpression(){
        assertEquals(Long.valueOf(-800),expressionEvaluator.getResult(expressionEvaluator.constructTree("-1000+200")));
    }

    @Test
    public void singedNumberExpression1(){
        assertEquals(Long.valueOf(800),expressionEvaluator.getResult(expressionEvaluator.constructTree("+1000-200")));
    }


    @Test
    public void test3_signed(){
        assertEquals(Long.valueOf(300),expressionEvaluator.getResult(expressionEvaluator.constructTree("(+100+200)")));
    }

    @Test
    public void test3_signed_1(){
        assertEquals(Long.valueOf(-900),expressionEvaluator.getResult(expressionEvaluator.constructTree("((-100)+(-800))")));
    }

    @Test
    public void test3_signed_2(){
        assertEquals(Long.valueOf(9),expressionEvaluator.getResult(expressionEvaluator.constructTree("-1*-9")));
    }
}
