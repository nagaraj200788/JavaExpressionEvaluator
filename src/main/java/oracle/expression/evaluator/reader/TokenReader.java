package oracle.expression.evaluator.reader;

import oracle.expression.evaluator.errorhandling.InvalidSyntexExpression;
import oracle.expression.evaluator.model.Token;
import oracle.expression.evaluator.model.TokenKind;

import java.util.LinkedList;

public class TokenReader extends AbstractTokenReader {

    public LinkedList<Token> allPreviousTokens = new LinkedList<>();
    @Override
    public Token getNextToken () throws Exception {
        int startPos = this.pos,preViousPosition=-1;
        startPos = this.pos;
        nextChar();
        String currentVal = this.expression.substring(startPos>=0?startPos+1:0, this.pos+1);
        if(this.ch=='('){
            return new Token(currentVal, TokenKind.OPENINGBRACKET);
        }else if ((this.ch >= '0' && this.ch <= '9') || this.ch == '.'){
            currentVal=getNextNumber(startPos);
            return new Token(currentVal, TokenKind.NUMBER);
        }else if ((this.ch >= 'a' && this.ch <= 'z') || (this.ch >= 'A' && this.ch <= 'Z') ){
           currentVal= getNextParamOrFunctionName(startPos);
            if (currentVal.equals("sqrt")) return new Token(currentVal, TokenKind.FUNCTION);
            else if (currentVal.equals("sin")) return new Token(currentVal, TokenKind.FUNCTION);
            else if (currentVal.equals("cos")) return new Token(currentVal, TokenKind.FUNCTION);
            else if (currentVal.equals("tan")) return new Token(currentVal, TokenKind.FUNCTION);
            else if (currentVal.equals("log")) return new Token(currentVal, TokenKind.FUNCTION);
            return new Token(currentVal, TokenKind.PARAM);
        }else if (unaryOperator.get((char)this.ch)==null ? false:true) {
            char operator=(char)this.ch;
            int ch1 = this.ch;
            int pos1 = this.pos;
            Token nextToken = getNextToken();
            Token preViousToken=getPreviousToken();
            boolean isUnaryOperator= isUnaryOperator(nextToken,preViousToken);
            if(isUnaryOperator){
                if(operator=='-'){
                    return new Token("-"+nextToken.getTokenValue(), TokenKind.NUMBER);
                }else if(operator=='+'){
                    return nextToken;
                }
            }else{
                this.ch=ch1;
                this.pos=pos1;
                return new Token((char)this.ch+"",TokenKind.OPERATORS);
            }
        }else if (getSingleCharOperator().get((char)this.ch)==null ? false:true) {
            return new Token((char)this.ch+"",TokenKind.OPERATORS);
        }else if (getDoubleCharOperator().get((char)this.ch)==null ? false:true) {
            if(this.ch == '<'){
                preViousPosition=this.pos;
                nextChar();
                if(this.ch == '='){
                    currentVal = this.expression.substring(startPos>=0?startPos+1:0,preViousPosition+2);
                }else {
                    PreviousChar();
                    return new Token((char)this.ch+"",TokenKind.OPERATORS);
                }
            }else if(this.ch == '>'){
                preViousPosition=this.pos;
                nextChar();
                if(this.ch == '='){
                    currentVal = this.expression.substring(startPos>=0?startPos+1:0,preViousPosition+2);
                }else {
                    PreviousChar();
                    return new Token((char)this.ch+"",TokenKind.OPERATORS);
                }
            }else if(this.ch == '&'){
                preViousPosition=this.pos;
                nextChar();
                if(this.ch == '&'){
                    currentVal = this.expression.substring(startPos>=0?startPos+1:0,preViousPosition+2);
                }else {
                    PreviousChar();
                    return new Token((char)this.ch+"",TokenKind.OPERATORS);
                }
            }else if(this.ch == '|'){
                preViousPosition=this.pos;
                nextChar();
                if(this.ch == '|'){
                    currentVal = this.expression.substring(startPos>=0?startPos+1:0,preViousPosition+2);
                }else {
                    PreviousChar();
                    return new Token((char)this.ch+"",TokenKind.OPERATORS);
                }
            }else if(this.ch == '='){
                preViousPosition=this.pos;
                nextChar();
                if(this.ch == '='){
                    currentVal = this.expression.substring(startPos>=0?startPos+1:0,preViousPosition+2);
                }else {
                    PreviousChar();
                    throw new InvalidSyntexExpression("Expression is of Invalid syntex");
                }
            }else if(this.ch == '!'){
                preViousPosition=this.pos;
                nextChar();
                if(this.ch == '='){
                    currentVal = this.expression.substring(startPos>=0?startPos+1:0,preViousPosition+2);
                }else {
                    PreviousChar();
                    throw new InvalidSyntexExpression("Expression is of Invalid syntex");
                }
            }
            return new Token(currentVal, TokenKind.OPERATORS);
        }else  if(this.ch==')'){
            return new Token(currentVal, TokenKind.CLOSINGBRACKET);
        }
        return null;
    }

    private boolean isUnaryOperator(Token nextToken,Token preViousToken) {
        //if nexttoken is Number and previous token is empty
        //if nexttoken is Number and previous token is (
        //if nexttoken is Number and previous token is *,/
        String preToken = preViousToken.getTokenValue().toString();
        if(TokenKind.NUMBER.equals(nextToken.getTokenKind()) &&
                (preViousToken.getTokenKind()==null || TokenKind.OPENINGBRACKET.equals(preViousToken.getTokenKind()) ||
                        "*".equals(preToken) || "/".equals(preToken))){
            return true;
        }else{
            return false;
        }
    }

    private Token getPreviousToken() {
        return allPreviousTokens.isEmpty() ? null : allPreviousTokens.getLast();
    }

    private String getNextParamOrFunctionName(int startPos) {
        int  preViousPosition=-1;
        while ( (this.ch >= 'a' && this.ch <= 'z') || (this.ch >= 'A' && this.ch <= 'Z') ){
            preViousPosition=this.pos;
            nextChar();
        }
        PreviousChar();
        return this.expression.substring(startPos>=0?startPos+1:0,preViousPosition+1);
    }

    private String getNextNumber(int startPos) {
        int  preViousPosition=-1;
        while ((this.ch >= '0' && this.ch <= '9') || this.ch == '.'){
            preViousPosition=this.pos;
            nextChar();
        }
        PreviousChar();
        return this.expression.substring(startPos>=0?startPos+1:0,preViousPosition+1);
    }

    private boolean isCurrentTokenIsUnaryBasedOnPreviousToken() {
        LinkedList<Token> cloned = (LinkedList<Token>)allPreviousTokens.clone();
        if(cloned.isEmpty())
        {
            return true;
        }else {
            if(TokenKind.OPENINGBRACKET.equals(cloned.poll().getTokenKind())){
                   return true;
            }
        }
        return false;
    }

    private void nextCharMove() {
        this.posTemp++;
        if(this.posTemp<this.expression.length())
        {
            this.chtemp=this.expression.charAt(this.posTemp);
            while (this.chtemp==32)
            {
                this.chtemp = (++this.posTemp < this.expression.length()) ?  this.expression.charAt(this.posTemp) : -1;
            }
        }
    }

    public boolean isNextTokenIsNumber(int startPos) {
        int  preViousPosition=-1;
        this.chtemp=this.ch;
        this.posTemp=this.pos;
        while ((this.chtemp >= '0' && this.chtemp <= '9') || this.chtemp == '.'){
            preViousPosition=this.posTemp;
            nextCharMove();
        }
        PreviousCharMove();
        String isNumber = this.expression.substring(startPos >= 0 ? startPos + 1 : 0, preViousPosition + 1);
        return  isNumber(isNumber);
    }

    public boolean isNumber(String value)
    {
        return value.matches("[-+]?[0-9]*\\.?[0-9]+");
    }
    public void nextChar() {
        this.pos++;
        if(this.pos<this.expression.length())
        {
            this.ch=this.expression.charAt(this.pos);
            while (this.ch==32)
            {
                this.ch = (++this.pos < this.expression.length()) ?  this.expression.charAt(this.pos) : -1;
            }
        }
    }

    public void PreviousCharMove() {
        if(this.posTemp>0){
            this.chtemp = (--this.posTemp < this.expression.length()) ? this.expression.charAt(this.posTemp) : -1;
        }else {
            this.posTemp=-1;
            this.chtemp=0;
        }
    }
    public void PreviousChar() {
        if(this.pos>0){
            this.ch = (--this.pos < this.expression.length()) ? this.expression.charAt(this.pos) : -1;
        }else {
            this.pos=-1;
            this.ch=0;
        }
    }

    public void replaceDoubleSigns() {
        this.expression=this.expression.replaceAll("\\-\\-", "+")
                .replaceAll("\\-\\-", "+")
                .replaceAll("\\+\\+", "+")
                .replaceAll("\\+\\+", "+")
                .replaceAll("\\-\\+", "-")
                .replaceAll("\\-\\+", "-")
                .replaceAll("\\+\\-","-")
                .replaceAll("\\+\\-","-");
        replaceImplicitBrackets();
    }
    public void replaceImplicitBrackets() {
        this.expression=this.expression.replaceAll("\\)\\(", "*");
    }
}
