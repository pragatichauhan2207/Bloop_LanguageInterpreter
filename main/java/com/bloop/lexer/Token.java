package com.bloop.lexer;

public final class Token {

    // ----------------------------------------------------------------
    // Fields (all set once in the constructor, never changed)
    // ----------------------------------------------------------------

    private final TokenType type;   // what kind of token this is
    private final String    value;  // the raw text from source
    private final int       line;   // 1-based line number in source file
    private final int       column; // 1-based column (char offset from line start)

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

    public Token(TokenType type, String value, int line) {
        this(type, value, line, 1);
    }

    public Token(TokenType type, String value, int line, int column) {
        this.type   = type;
        this.value  = value;
        this.line   = line;
        this.column = column;
    }


    public TokenType getType() {
        return type;
    }

    
    public String getValue() {
        return value;
    }

    
    public int getLine() {
        return line;
    }

    /** Returns the 1-based column number of this token on its source line. */
    public int getColumn() {
        return column;
    }

    // ----------------------------------------------------------------
    // Object overrides
    // ----------------------------------------------------------------

    @Override
    public String toString() {
        return String.format("Token[%s, \"%s\", line=%d]", type, value, line);
    }
}
