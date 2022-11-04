package com.expression.evaluator;

import com.expression.evaluator.model.Node;
import com.expression.evaluator.reader.TokenReader;
import com.expression.evaluator.model.EvaluatorConstants;
import com.expression.evaluator.model.ResultDecorator;
import com.expression.evaluator.model.Token;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.DoubleBinaryOperator;
import java.util.function.LongBinaryOperator;

public class ExpressionEvaluator implements IExpressionEvaluator{
    public TokenReader tokenReader;
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
        Node t, t1, t2,t3;

        // Prioritising the operators
       /* if("a>b"){
            "a-b"
        }else
        { "b-a"
        }*/
        while (tokenReader.pos < tokenReader.expression.length()-1) {
            Token token = tokenReader.getNextToken();
            tokenReader.allPreviousTokens.add(token);
            switch (token.getTokenKind()) {
                case OPENINGBRACKET:
                    operatorsStack.add(new Node(token.getTokenValue()));
                    break;
                case NUMBER:
                    t = new Node(token.getTokenValue());
                    operandsStack.add(t);
                    break;
                case PARAM:
                    t = new Node(token.getTokenValue());
                    operandsStack.add(t);
                    break;
                case FUNCTION:
                case OPERATORS:
                    if (tokenReader.getOperatorPriority(token.getTokenValue()) > 0) {
                        // If an operator with lower or
                        // same associativity appears
                        boolean isMathFun=tokenReader.isMathFunction(token);
                        while (!operatorsStack.isEmpty() && !operatorsStack.peek().getData().toString().equals("(")
                                && (( (!token.getTokenValue().toString().equals("^") && !isMathFun) &&
                                      tokenReader.getOperatorPriority(operatorsStack.peek().getData()) >= tokenReader.getOperatorPriority(token.getTokenValue())
                                    )
                                   || ( (token.getTokenValue().toString().equals("^") || isMathFun)
                                && tokenReader.getOperatorPriority(operatorsStack.peek().getData()) > tokenReader.getOperatorPriority(token.getTokenValue())
                                      )
                                   )
                             ) {
                            t = operatorsStack.peek();
                            switch (t.getData().toString())
                            {
                                case "sin":
                                case "cos":
                                case "tan":
                                case "log":
                                case "sqrt":
                                    // Get and remove the top element
                                    // from the tokenReader.character stack
                                    operatorsStack.pop();
                                    // Get and remove the top element
                                    // from the node stack
                                    t1 = operandsStack.peek();
                                    operandsStack.pop();
                                    // Update the tree
                                    t.singleChild = t1;
                                    operandsStack.add(t);
                                    break;
                                case "+":
                                case "-":
                                    operatorsStack.pop();
                                    t1 = operandsStack.peek();
                                    operandsStack.pop();
                                    if(!operandsStack.isEmpty())
                                    {
                                        t2 = operandsStack.peek();
                                        operandsStack.pop();
                                        t.left = t2;
                                        t.right = t1;
                                    }else {
                                        t.left = t1;
                                    //    t.setData(t.getData().append("unary"));
                                    }
                                    operandsStack.add(t);
                                    break;
                                default:
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
                                    t.left = t2;
                                    t.right = t1;
                                    // Push the node to the node stack
                                    operandsStack.add(t);
                                    break;
                            }
                        }
                        // Push tokenReader.expression[i] to tokenReader.char stack
                        //  operatorsStack.push(tokenReader.expression.tokenReader.charAt(i));
                        operatorsStack.push(new Node(token.getTokenValue()));
                    }
                    break;
                case CLOSINGBRACKET:
                        while (!operatorsStack.isEmpty() && !operatorsStack.peek().getData().toString().equals("("))
                        {
                                          t = operatorsStack.peek();
                                          switch (t.getData().toString()) {
                                                        case "sin":
                                                        case "cos":
                                                        case "tan":
                                                        case "log":
                                                        case "sqrt":
                                                            operatorsStack.pop();
                                                            t1 = operandsStack.peek();
                                                            operandsStack.pop();
                                                            t.singleChild = t1;
                                                            operandsStack.add(t);
                                                            break;
                                                        case "+":
                                                        case "-":
                                                            operatorsStack.pop();
                                                            t1 = operandsStack.peek();
                                                            operandsStack.pop();
                                                            if (!operandsStack.isEmpty()) {
                                                                t2 = operandsStack.peek();
                                                                operandsStack.pop();
                                                                t.left = t2;
                                                                t.right = t1;
                                                            } else {
                                                                t.left = t1;
                                                            }
                                                            operandsStack.add(t);
                                                            break;
                                                        default:
                                                            operatorsStack.pop();
                                                            t1 = operandsStack.peek();
                                                            operandsStack.pop();
                                                            t2 = operandsStack.peek();
                                                            operandsStack.pop();
                                                            t.left = t2;
                                                            t.right = t1;
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
    public ResultDecorator evaluateTree (Node root){
        if (root == null){
            return null;// new ResultDecorator(0, EvaluatorConstants.Arithmatic);
        }
        // Leaf node i.e, an integer
        if (root.getLeft() == null && root.getRight() == null) {
            if(root.singleChild!=null)
            {
                ResultDecorator operand=evaluateTree(root.singleChild);
                return aaplyMathFunction(root.getData().toString(),operand);
            }
            String param = root.getData().toString();
            Number paramValue = tokenReader.getParams().isEmpty() ? null : tokenReader.getParams().get(param);
            if (paramValue != null) {
                return new ResultDecorator(paramValue,EvaluatorConstants.Arithmatic);
            } else {
                return new ResultDecorator(NumberUtils.getNumber(param),EvaluatorConstants.Arithmatic);
            }
        }

        if(root.getData().toString().equals("?"))
        {
            ResultDecorator leftEval = evaluateTree(root.left);
            return applyTernaryOperator(leftEval,root);
        }else if(tokenReader.isMathFunction(root) && root.singleChild!=null)
        {
                ResultDecorator operand=evaluateTree(root.singleChild);
                return aaplyMathFunction(root.getData().toString(),operand);
        }else
        {
            // Evaluate left subtree
            ResultDecorator leftEval = evaluateTree(root.left);
            // Evaluate right subtree
            ResultDecorator rightEval = evaluateTree(root.right);
            return applyOperator(root.getOperator(), leftEval, rightEval);
        }
    }

    private ResultDecorator applyTernaryOperator(ResultDecorator leftEval, Node questionOperatorNode) {
        switch (leftEval.getTypeOfexpresion())
        {
            case EvaluatorConstants.Boolean:
                if (leftEval.getResult().longValue() == 1)
                {
                    ResultDecorator result = evaluateTree(questionOperatorNode.right.left);
                    return result;
                }else {
                    ResultDecorator result = evaluateTree(questionOperatorNode.right.right);
                    return result;
                }
            default:return null;
        }
    }

    public  ResultDecorator aaplyMathFunction(String functionName, ResultDecorator operand) {
        switch (functionName)
        {
            case "sin" : return new ResultDecorator(Math.sin(operand.getResult().doubleValue()), EvaluatorConstants.Arithmatic);
            case "cos" : return new ResultDecorator(Math.cos(operand.getResult().doubleValue()), EvaluatorConstants.Arithmatic);
            case "tan" : return new ResultDecorator(Math.tan(operand.getResult().doubleValue()), EvaluatorConstants.Arithmatic);
            case "log" : return new ResultDecorator(Math.log(operand.getResult().doubleValue()), EvaluatorConstants.Arithmatic);
            case "sqrt" : return new ResultDecorator(Math.sqrt(operand.getResult().doubleValue()), EvaluatorConstants.Arithmatic);
            default: return null;
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
            Number a, Number b, DoubleBinaryOperator dOp)
    {
        return (double)dOp.applyAsDouble(a.doubleValue(), b.doubleValue());
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
        Number a=operand1.getResult();
        Number b=null;
        boolean valuesFitIntoPrimitiveTypes = true;
        if(operand2!=null) {
            b=operand2.getResult();
            valuesFitIntoPrimitiveTypes=NumberUtils.isValuesFitIntoPrimitiveTypes(a, b);
        }
        Number result=null;
        ResultDecorator resultDecorator= new ResultDecorator();
        switch(op.length == 1? op[0]: op[0] << 16 | op[1])
        {
            case '+':
                if(operand2!=null) {
                    if (valuesFitIntoPrimitiveTypes) {
                        result = performArithmaticOperation(a, b, Double::sum, Long::sum);
                    } else {
                        result = performArithMaticOnBigValues(a, b, op[0]);
                    }
                }else {
                    result=operand1.getResult();
                }
                resultDecorator.setTypeOfexpresion(EvaluatorConstants.Arithmatic);
                break;
            case '-':
                if(operand2!=null) {
                    if (valuesFitIntoPrimitiveTypes) {
                        result = performArithmaticOperation(a, b, (d1, d2) -> d1 - d2, (l1, l2) -> l1 - l2);
                    } else {
                        result = performArithMaticOnBigValues(a, b, op[0]);
                    }
                }else {
                    result= convertToSignedNumber(operand1.getResult());
                }
                resultDecorator.setTypeOfexpresion(EvaluatorConstants.Arithmatic);
                break;
            case '*':
                if(valuesFitIntoPrimitiveTypes)
                {
                    result = performArithmaticOperation(a, b, (d1, d2) -> d1 * d2, (l1, l2) -> l1 * l2);
                }else {
                    result= performArithMaticOnBigValues(a,b,op[0]);
                }
                resultDecorator.setTypeOfexpresion(EvaluatorConstants.Arithmatic);
                break;
            case '/':
                if(valuesFitIntoPrimitiveTypes)
                {
                    result = performDivideOperation(a, b, (d1, d2) -> (double) d1 / divideByZero(d2));
                }else {
                    result= performArithMaticOnBigValues(a,b,op[0]);
                }
                resultDecorator.setTypeOfexpresion(EvaluatorConstants.Arithmatic);
                break;
            case '%':
                result = performArithmaticOperation(a, b, (d1, d2) -> d1 % divideByZero(d2), (l1, l2) -> l1 % l2);
                resultDecorator.setTypeOfexpresion(EvaluatorConstants.Arithmatic);
                break;
            case '^':
                result = performArithmaticOperation(a, b, Math::pow, (l1, l2) -> (long) Math.pow(l1, l2));
                resultDecorator.setTypeOfexpresion(EvaluatorConstants.Arithmatic);
                break;
            case '>':
                resultDecorator.setTypeOfexpresion(EvaluatorConstants.Boolean);
                result = performComparisionOperation(a, b, (d1, d2) -> d1 > d2, (l1, l2) -> l1 > l2);
                break;
            case '<':
                resultDecorator.setTypeOfexpresion(EvaluatorConstants.Boolean);
                result = performComparisionOperation(a, b, (d1, d2) -> d1 < d2, (l1, l2) -> l1 < l2);
                break;
            case '<' << 16 | '=':
                resultDecorator.setTypeOfexpresion(EvaluatorConstants.Boolean);
                result = performComparisionOperation(a, b, (d1, d2) -> d1 <= d2, (l1, l2) -> l1 <= l2);
                break;
            case '>' << 16 | '=':
                resultDecorator.setTypeOfexpresion(EvaluatorConstants.Boolean);
                result = performComparisionOperation(a, b, (d1, d2) -> d1 >= d2, (l1, l2) -> l1 >= l2);
                break;
            case '=' << 16 | '=':
                resultDecorator.setTypeOfexpresion(EvaluatorConstants.Boolean);
                result = performComparisionOperation(a, b, (d1, d2) -> d1 == d2, (l1, l2) -> l1 == l2);
                break;
            case '!' << 16 | '=':
                resultDecorator.setTypeOfexpresion(EvaluatorConstants.Boolean);
                result = performComparisionOperation(a, b, (d1, d2) -> d1 != d2, (l1, l2) -> l1 != l2);
                break;
            case '&':
            case '&' << 16 | '&':
                resultDecorator.setTypeOfexpresion(EvaluatorConstants.Boolean);
                result = (a.equals(1)?true:false) && (b.equals(1)?true:false)?1:0;
                break;
            case '|':
            case '|' << 16 | '|':
                resultDecorator.setTypeOfexpresion(EvaluatorConstants.Boolean);
                result = (a.equals(1)?true:false) || (b.equals(1)?true:false)?1:0;
                break;
            default: throw new ArithmeticException("unknown operator " + new String(op));
        }
        resultDecorator.setResult(result);
        return resultDecorator;
    }

    private Number convertToSignedNumber(Number value) {
           if (value instanceof Double )
           {
                return -1*value.doubleValue();
            } else if (value instanceof BigDecimal )
            {
             return  ((BigDecimal) value).multiply(BigDecimal.valueOf(-1));
            } else if (value instanceof BigInteger )
            {
                return  ((BigInteger) value).multiply(BigInteger.valueOf(-1));
            }else {
               return -1*value.longValue();
           }
    }
    private Number performArithMaticOnBigValues(Number a, Number b, char operator) {
        if (a instanceof Double || b instanceof Double || a instanceof BigDecimal || b instanceof BigDecimal) {
            switch (operator){
                case '+':
                    return new BigDecimal(a.toString()).add(new BigDecimal(b.toString()));
                case '-':
                    return new BigDecimal(a.toString()).subtract(new BigDecimal(b.toString()));
                case '*':
                    return new BigDecimal(a.toString()).multiply(new BigDecimal(b.toString()));
                case '/':
                    return new BigDecimal(a.toString()).divide(new BigDecimal(b.toString()));

            }
        } else {
            switch (operator){
                case '+':
                    return new BigInteger(a.toString()).add(new BigInteger(b.toString()));
                case '-':
                    return new BigInteger(a.toString()).subtract(new BigInteger(b.toString()));
                case '*':
                    return new BigInteger(a.toString()).multiply(new BigInteger(b.toString()));
                case '/':
                    return new BigInteger(a.toString()).divide(new BigInteger(b.toString()));
            }
        }
        return null;
    }

    @Override
    public Object getResult(Node root) {
        ResultDecorator resultDecorator = evaluateTree(root);
        Number result = resultDecorator.getResult();
        switch (resultDecorator.getTypeOfexpresion()) {
            case EvaluatorConstants.Arithmatic:
                /*if(result instanceof BigDecimal)
                {
                    return new BigDecimal(result.toString()).toPlainString();
                }else{*/
                    return result;
               // }
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
}
