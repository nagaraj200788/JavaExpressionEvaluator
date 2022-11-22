package oracle.expression.evaluator.model;

public class Token {
    TokenKind tokenKind;
    StringBuffer tokenValue;

    public Token(String currentVal, TokenKind tokenKind) {
        this.tokenValue=new StringBuffer(currentVal);
        this.tokenKind=tokenKind;
    }

    public TokenKind getTokenKind() {
        return tokenKind;
    }

    public void setTokenKind(TokenKind tokenKind) {
        this.tokenKind = tokenKind;
    }

    public StringBuffer getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(StringBuffer tokenValue) {
        this.tokenValue = tokenValue;
    }
}
