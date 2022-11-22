package oracle.expression.evaluator.reader;

import oracle.expression.evaluator.model.*;
import oracle.expression.evaluator.model.tree.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTokenReader {

    private Map<String, Number> params = null;
    public String expression;
    public int pos = -1,posTemp, ch,chtemp;

    private Map<Character,Boolean> singleCharOperator= new HashMap<>(){{
        put('+',Boolean.valueOf(true));
        put('-', Boolean.valueOf(true));
        put('/', Boolean.valueOf(true));
        put('*', Boolean.valueOf(true));
        put('%', Boolean.valueOf(true));
        put('^', Boolean.valueOf(true));
        put('?', Boolean.valueOf(true));
        put(':', Boolean.valueOf(true));
    }};

    public Map<Character,Boolean> unaryOperator= new HashMap<>(){{
        put('+',Boolean.valueOf(true));
        put('-', Boolean.valueOf(true));
    }};

    private Map<Character,Boolean> doubleCharOperator = new HashMap<>(){{
        put('>',Boolean.valueOf(true));
        put('<', Boolean.valueOf(true));
        put('=', Boolean.valueOf(true));
        put('&', Boolean.valueOf(true));
        put('|', Boolean.valueOf(true));
        put('!', Boolean.valueOf(true));

    }};
    private int priorityCounter=1,zeroPriority=0;
    private Map<String, Integer> operatorsPrioritiesMap = new HashMap<>() {{
        put("?", priorityCounter);
        put(":", ++priorityCounter);

        put("&", ++priorityCounter);
        put("&&", priorityCounter);
        put("|", priorityCounter);
        put("||", priorityCounter);

        put(">", ++priorityCounter);
        put("<", priorityCounter);
        put(">=", priorityCounter);
        put("<=", priorityCounter);
        put("==", priorityCounter);
        put("!=", priorityCounter);

        put("+", ++priorityCounter);
        put("-", priorityCounter);

        put("/", ++priorityCounter);
        put("*", priorityCounter);
        put("%", priorityCounter);

        put("^", ++priorityCounter);

        put("sin", ++priorityCounter);
        put("cos", priorityCounter);
        put("tan", priorityCounter);
        put("log", priorityCounter);
        put("sqrt", priorityCounter);

        put(")", zeroPriority);
    }};

    public Map<String, Number> getParams() {
        return params==null ? Collections.EMPTY_MAP:params;
    }

    public void setParams(Map<String, Number> params) {
        this.params = params;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getCh() {
        return ch;
    }

    public void setCh(int ch) {
        this.ch = ch;
    }

    public Map<String, Integer> getOperatorsPrioritiesMap() {
        return operatorsPrioritiesMap;
    }

    public void setOperatorsPrioritiesMap(Map<String, Integer> operatorsPrioritiesMap) {
        this.operatorsPrioritiesMap = operatorsPrioritiesMap;
    }

    public FunctionNode getMathFunctionInstance(Token token) {
        switch (token.getTokenValue().toString())
        {
            case "sin":
                return new SinFunctionNode(token.getTokenValue());
            case "cos":
                return new CosFunctionNode(token.getTokenValue());
            case "tan":
                return new TanFunctionNode(token.getTokenValue());
            case "log":
                return new LogFunctionNode(token.getTokenValue());
            case "sqrt":
                return new SqrtFunctionNode(token.getTokenValue());
            default: return null;
        }
    }

    public int getOperatorPriority(StringBuffer operator){
        return operatorsPrioritiesMap.get(operator+"");
    }


    public Map<Character, Boolean> getSingleCharOperator() {
        return singleCharOperator;
    }
    public Map<Character, Boolean> getDoubleCharOperator() {
        return doubleCharOperator;
    }

    public abstract Token getNextToken() throws Exception;

}
