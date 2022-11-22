package oracle.expression.evaluator;

import oracle.expression.evaluator.functionalinterfaces.BinaryDoublePredicate;
import oracle.expression.evaluator.functionalinterfaces.BinaryLongPredicate;
import oracle.expression.evaluator.functionalinterfaces.IExpressionOperation;
import oracle.expression.evaluator.model.*;
import oracle.expression.evaluator.model.tree.DataNode;
import oracle.expression.evaluator.model.tree.FunctionNode;
import oracle.expression.evaluator.model.tree.Node;
import oracle.expression.evaluator.model.tree.ParamNode;
import oracle.expression.evaluator.reader.TokenReader;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.LongBinaryOperator;

public class ExpressionEvaluator implements IExpressionEvaluator{
    public TokenReader tokenReader;
    static long a1 = 0l;
    static long a2= 0l;
    public static long applyOperatorTotalTime = 0l;
    static long a3= 0l;
    static long a4= 0l;
    public static long isValuesFitIntoPrimitiveTypes = 0l;
    public ExpressionEvaluator()
    {
        this.tokenReader=new TokenReader();
    }
    @Override
    public Node constructTree(String expressionVal) {
        try
        {
            tokenReader.expression = expressionVal.replaceAll("\\s+","");
            tokenReader.replaceDoubleSigns();
            tokenReader.pos = -1;
            tokenReader.ch=0;
            tokenReader.expression = "(" + tokenReader.expression + ")";
            //  tokenReader.char[] tokens = tokenReader.expression.toCharArray();
            // Stack to hold nodes
            Stack<Node> operandsStack = new Stack<>();
            // Stack to hold tokenReader.chars
            Stack<Node> operatorsStack = new Stack<>();
            Node t, t1, t2;

            while (tokenReader.pos < tokenReader.expression.length()-1) {
                Token token = tokenReader.getNextToken();
                tokenReader.allPreviousTokens.add(token);
                switch (token.getTokenKind()) {
                    case OPENINGBRACKET:
                        operatorsStack.add(new DataNode(token.getTokenValue()));
                        break;
                    case NUMBER:
                        t = new DataNode(token.getTokenValue());
                        operandsStack.add(t);
                        break;
                    case PARAM:
                        t = new ParamNode(token.getTokenValue());
                        operandsStack.add(t);
                        break;
                    case FUNCTION:
                    case OPERATORS:
                      FunctionNode functionNode=tokenReader.getMathFunctionInstance(token);
                        boolean isMathFunInstance=functionNode!=null ? true : false;//tokenReader.isMathFunction(token);
                        if (tokenReader.getOperatorPriority(token.getTokenValue()) > 0) {
                            // If an operator with lower or
                            // same associativity appears
                            while (!operatorsStack.isEmpty() && !operatorsStack.peek().getData().toString().equals("(")
                                    && (( (!token.getTokenValue().toString().equals("^") && !isMathFunInstance) &&
                                    tokenReader.getOperatorPriority(operatorsStack.peek().getData()) >= tokenReader.getOperatorPriority(token.getTokenValue())
                            )
                                    || ( (token.getTokenValue().toString().equals("^") || isMathFunInstance)
                                    && tokenReader.getOperatorPriority(operatorsStack.peek().getData()) > tokenReader.getOperatorPriority(token.getTokenValue())
                            )
                            )
                            ) {
                                t = operatorsStack.peek();
                               if( t instanceof FunctionNode){
                                   // Get and remove the top element
                                   // from the tokenReader.character stack
                                   operatorsStack.pop();
                                   // Get and remove the top element
                                   // from the node stack
                                   t1 = operandsStack.peek();
                                   operandsStack.pop();
                                   // Update the tree
                                   ((FunctionNode)t).setSingleChild(t1);;
                                   operandsStack.add(t);
                                   break;
                               }else {
                                   // Get and remove the top element
                                   // from the tokenReader.character stack
                                   operatorsStack.pop();
                                   // Get and remove the top element
                                   // from the node stack
                                   t1 = operandsStack.peek();
                                   operandsStack.pop();
                                   // Get and remove the currently top
                                   // element from the node stack
                                   t2 = operandsStack.peek();
                                   operandsStack.pop();
                                   // Update the tree
                                   t.setLeft(t2);
                                   t.setRight(t1);
                                   // Push the node to the node stack
                                   operandsStack.add(t);
                               }
                            }
                            // Push tokenReader.expression[i] to tokenReader.char stack
                            //  operatorsStack.push(tokenReader.expression.tokenReader.charAt(i));
                            if(isMathFunInstance){
                                operatorsStack.push(functionNode);
                            }else {
                                operatorsStack.push(new DataNode(token.getTokenValue()));
                            }
                        }
                        break;
                    case CLOSINGBRACKET:
                        while (!operatorsStack.isEmpty() && !operatorsStack.peek().getData().toString().equals("("))
                        {
                            t = operatorsStack.peek();
                            if( t instanceof FunctionNode){
                                operatorsStack.pop();
                                t1 = operandsStack.peek();
                                operandsStack.pop();
                                ((FunctionNode)t).setSingleChild(t1);;
                                operandsStack.add(t);
                            }else {
                                operatorsStack.pop();
                                t1 = operandsStack.peek();
                                operandsStack.pop();
                                t2 = operandsStack.peek();
                                operandsStack.pop();
                                t.setLeft(t2);
                                t.setRight(t1);
                                operandsStack.add(t);
                            }
                        }
                        operatorsStack.pop();
                        break;
                }
            }
            t = operandsStack.peek();
            tokenReader.allPreviousTokens.clear();
            return t;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public ResultDecorator evaluateTree (Node root) throws Exception {
        if (root == null){
            return null;
        }
        // Leaf node
        if (root.getLeft() == null && root.getRight() == null) {
            if(root instanceof DataNode)
            {
                return new ResultDecorator(NumberUtils.getNumber(root.getData().toString()),EvaluatorConstants.Arithmatic);
            }else if(root instanceof ParamNode)
            {
                String param = root.getData().toString();
                Number paramValue = tokenReader.getParams().isEmpty() ? null : tokenReader.getParams().get(param);
                if(paramValue==null){
                    throw new Exception("Parameter "+param+" not defined");
                }
                return new ResultDecorator(paramValue,EvaluatorConstants.Arithmatic);
            }
        }
        if(root.getData().toString().equals("?"))
        {
            ResultDecorator leftEval = evaluateTree(root.getLeft());
            return applyTernaryOperator(leftEval,root);
        }
        if( root instanceof FunctionNode && ((FunctionNode)root).getSingleChild()!=null)
        {
            ResultDecorator operand=evaluateTree(((FunctionNode)root).getSingleChild());
            return new ResultDecorator(((FunctionNode)root).performOperation(operand.getResult().doubleValue()), EvaluatorConstants.Arithmatic);
        }
        // Evaluate left subtree
        ResultDecorator leftEval = evaluateTree(root.getLeft());
        // Evaluate right subtree
        ResultDecorator rightEval = evaluateTree(root.getRight());
        return applyOperator(root.getOperator(), leftEval, rightEval);
    }

    private ResultDecorator applyTernaryOperator(ResultDecorator leftEval, Node questionOperatorNode) throws Exception {
        switch (leftEval.getTypeOfexpresion())
        {
            case EvaluatorConstants.Boolean:
                if (leftEval.getResult().longValue() == 1)
                {
                    ResultDecorator result = evaluateTree(questionOperatorNode.getRight().getLeft());
                    return result;
                }else {
                    ResultDecorator result = evaluateTree(questionOperatorNode.getRight().getRight());
                    return result;
                }
            default:return null;
        }
    }


    // A utility method to apply an

    // operator 'op' on operands 'a'

    // and 'b'. Return the result.

    public Number performArithmaticOperation(
            Number a, Number b, DoubleBinaryOperator dOp, LongBinaryOperator lOp)
    {
        Number res=null;
        if (a instanceof Double || b instanceof Double) {
            res = dOp.applyAsDouble(a.doubleValue(), b.doubleValue());
        } else {
            res = lOp.applyAsLong(a.longValue(), b.longValue());
        }
        return res;
    }
    public Number performDivideOperation(
            Number a, Number b, DoubleBinaryOperator dOp) {
        double v = dOp.applyAsDouble(a.doubleValue(), b.doubleValue());
        String s = String.valueOf(v);
        if (s.indexOf(".") < 0) {
            return Long.valueOf(s);
        } else {
            s = s.replaceAll("0*$", "").replaceAll("\\.$", "");
            if (s.indexOf(".") < 0) {
                return Long.valueOf(s);
            } else {
                return Double.valueOf(s);
            }
        }
    }
    public double divideByZero(double d2)
    {
        if(d2 == 0) throw new ArithmeticException("/ by zero");
        return d2;
    }
    Number performComparisionOperation(Number a, Number b, BinaryDoublePredicate dOp, BinaryLongPredicate lOp)
    {
        return (a instanceof Double || b instanceof Double?
                dOp.test(a.doubleValue(), b.doubleValue()):
                lOp.test(a.longValue(), b.longValue()))? 1: 0;
    }
    public ResultDecorator applyOperator (char[] op, ResultDecorator operand1, ResultDecorator operand2){
        a1 = System.currentTimeMillis();
        Number a=operand1.getResult();
        Number b=null;
        boolean valuesFitIntoPrimitiveTypes = true;
        if(operand2!=null) {
            b=operand2.getResult();
            a3=System.currentTimeMillis();
            valuesFitIntoPrimitiveTypes= NumberUtils.isValuesFitIntoPrimitiveTypes(a, b);
            a4=System.currentTimeMillis();
            isValuesFitIntoPrimitiveTypes = isValuesFitIntoPrimitiveTypes +(a4-a3);
        }
        Integer operator = op.length == 1 ? op[0] : (op[0] << 16 | op[1]);
        IExpressionOperation<ResultDecorator, Number, Number, Boolean> operation = operationsImpl.get(operator);
        if(operation==null){
            throw new ArithmeticException("unknown operator " + new String(op));
        }else {
            ResultDecorator perform = operation.perform(a, b, valuesFitIntoPrimitiveTypes);
            a2=System.currentTimeMillis();
            applyOperatorTotalTime = applyOperatorTotalTime +a2-a1;
            return  perform;
        }
    }

    private Number performArithMaticOnBigValues(Number a, Number b, char operator) {
        if (a instanceof Double || b instanceof Double || a instanceof BigDecimal || b instanceof BigDecimal) {
            return bigDecimalArithMaticOperations.get(operator).apply(a,b);
        } else {
            return bigIntegersArithMaticOperations.get(operator).apply(a,b);
        }
    }

    @Override
    public Object getResultFromEvaluatedTree(Node root)  {
        ResultDecorator resultDecorator = null;
        try {
            resultDecorator = evaluateTree(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Number result = resultDecorator.getResult();
        switch (resultDecorator.getTypeOfexpresion()) {
            case EvaluatorConstants.Arithmatic:
                return result;
            case EvaluatorConstants.Boolean:
                return result.longValue() == 1 ? Boolean.valueOf(true) : Boolean.valueOf(false);
        }
        return null;
    }
    public TokenReader getTokenReader() {
        return tokenReader;
    }

    public void setTokenReader(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
    }
    public Map<Integer, IExpressionOperation<ResultDecorator,Number,Number,Boolean>> operationsImpl=new HashMap<>(){{
        put((int) '+', (a, b, valuesFitIntoPrimitiveTypes) -> {
            ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Arithmatic);
            if (valuesFitIntoPrimitiveTypes) {
                resultDecorator.setResult(performArithmaticOperation(a, b, Double::sum, Long::sum));
            } else {
                resultDecorator.setResult(performArithMaticOnBigValues(a, b, '+'));
            }
            return  resultDecorator;
        });
        put((int)'-', (a, b, valuesFitIntoPrimitiveTypes) -> {
            ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Arithmatic);
            if (valuesFitIntoPrimitiveTypes) {
                resultDecorator.setResult(performArithmaticOperation(a, b, (d1, d2) -> d1 - d2, (l1, l2) -> l1 - l2));
            } else {
                resultDecorator.setResult(performArithMaticOnBigValues(a, b, '-'));
            }
            return  resultDecorator;
        });
        put((int)'*', (a, b, valuesFitIntoPrimitiveTypes) -> {
            ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Arithmatic);
            if (valuesFitIntoPrimitiveTypes) {
                resultDecorator.setResult(performArithmaticOperation(a, b, (d1, d2) -> d1 * d2, (l1, l2) -> l1 * l2));
            } else {
                resultDecorator.setResult(performArithMaticOnBigValues(a,b,'*'));
            }
            return  resultDecorator;
        });
        put((int)'/', (a, b, valuesFitIntoPrimitiveTypes) -> {
            ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Arithmatic);
            if (valuesFitIntoPrimitiveTypes) {
                resultDecorator.setResult(performDivideOperation(a, b,  (d1, d2) -> (double) d1 / divideByZero(d2)));
            } else {
                resultDecorator.setResult(performArithMaticOnBigValues(a,b,'/'));
            }
            return  resultDecorator;
        });
        put((int)'%', (a, b, valuesFitIntoPrimitiveTypes) -> {
                    ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Arithmatic);
                    resultDecorator.setResult(performArithmaticOperation(a, b, (d1, d2) -> d1 % divideByZero(d2), (l1, l2) -> l1 % l2));
                    return  resultDecorator;
                }

        );
        put((int)'^', (a, b, valuesFitIntoPrimitiveTypes) ->
                {
                    ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Arithmatic);
                    resultDecorator.setResult(performArithmaticOperation(a, b, Math::pow, (l1, l2) -> (long) Math.pow(l1, l2)));
                    return  resultDecorator;
                }
        );
        put((int)'>', (a, b, valuesFitIntoPrimitiveTypes) ->
                {
                    ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Boolean);
                    resultDecorator.setResult(performComparisionOperation(a, b, (d1, d2) -> d1 > d2, (l1, l2) -> l1 > l2));
                    return  resultDecorator;
                }
        );
        put((int)'<', (a, b, valuesFitIntoPrimitiveTypes) ->
                {
                    ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Boolean);
                    resultDecorator.setResult(performComparisionOperation(a, b, (d1, d2) -> d1 < d2, (l1, l2) -> l1 < l2));
                    return  resultDecorator;
                }
        );
        put((int)('<' << 16 | '='), (a, b, valuesFitIntoPrimitiveTypes) ->
                {
                    ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Boolean);
                    resultDecorator.setResult(performComparisionOperation(a, b, (d1, d2) -> d1 <= d2, (l1, l2) -> l1 <= l2));
                    return  resultDecorator;
                }
        );
        put((int)('>' << 16 | '='), (a, b, valuesFitIntoPrimitiveTypes) ->
                {
                    ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Boolean);
                    resultDecorator.setResult(performComparisionOperation(a, b, (d1, d2) -> d1 >= d2, (l1, l2) -> l1 >= l2));
                    return  resultDecorator;
                }
        );

        put((int)('=' << 16 | '='), (a, b, valuesFitIntoPrimitiveTypes) ->
                {
                    ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Boolean);
                    resultDecorator.setResult(performComparisionOperation(a, b, (d1, d2) -> d1 == d2, (l1, l2) -> l1 == l2));
                    return  resultDecorator;
                }
        );
        put((int)('!' << 16 | '='), (a, b, valuesFitIntoPrimitiveTypes) ->
                {
                    ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Boolean);
                    resultDecorator.setResult(performComparisionOperation(a, b, (d1, d2) -> d1 != d2, (l1, l2) -> l1 != l2));
                    return  resultDecorator;
                }
        );
        put((int)'&', (a, b, valuesFitIntoPrimitiveTypes) ->
                {
                    ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Boolean);
                    resultDecorator.setResult((a.equals(1)?true:false) && (b.equals(1)?true:false)?1:0);
                    return  resultDecorator;
                }
        );
        put((int)('&' << 16 | '&'), (a, b, valuesFitIntoPrimitiveTypes) ->
                {
                    ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Boolean);
                    resultDecorator.setResult((a.equals(1)?true:false) && (b.equals(1)?true:false)?1:0);
                    return  resultDecorator;
                }
        );
        put((int)'|', (a, b, valuesFitIntoPrimitiveTypes) ->
                {
                    ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Boolean);
                    resultDecorator.setResult((a.equals(1)?true:false) || (b.equals(1)?true:false)?1:0);
                    return  resultDecorator;
                }
        );
        put((int)('|' << 16 | '|'), (a, b, valuesFitIntoPrimitiveTypes) ->
                {
                    ResultDecorator resultDecorator=new ResultDecorator(EvaluatorConstants.Boolean);
                    resultDecorator.setResult((a.equals(1)?true:false) || (b.equals(1)?true:false)?1:0);
                    return  resultDecorator;
                }
        );
    }};
    public Map<Character, BiFunction<Number,Number,Number>> bigDecimalArithMaticOperations=new HashMap<>(){{
        put('+',(a,b)->new BigDecimal(a.toString()).add(new BigDecimal(b.toString())));
        put('-',(a,b)->new BigDecimal(a.toString()).subtract(new BigDecimal(b.toString())));
        put('*',(a,b)->new BigDecimal(a.toString()).multiply(new BigDecimal(b.toString())));
        put('/',(a,b)->new BigDecimal(a.toString()).divide(new BigDecimal(b.toString())));
    }};
    Map<Character, BiFunction<Number,Number,Number>> bigIntegersArithMaticOperations=new HashMap<>(){{
        put('+',(a,b)->new BigInteger(a.toString()).add(new BigInteger(b.toString())));
        put('-',(a,b)->new BigInteger(a.toString()).subtract(new BigInteger(b.toString())));
        put('*',(a,b)->new BigInteger(a.toString()).multiply(new BigInteger(b.toString())));
        put('/',(a,b)->new BigInteger(a.toString()).divide(new BigInteger(b.toString())));
    }};

}
