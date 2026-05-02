package com.bloop.lexer;


public enum TokenType {

    // ----------------------------------------------------------------
    // BLOOP Keywords
    // ----------------------------------------------------------------

    /** "put" — begins an assignment statement: put <expr> into <var> */
    PUT,

    /** "into" — separator between value and variable in assignment */
    INTO,

    /** "print" — begins a print statement: print <expr> */
    PRINT,

    /** "if" — begins a conditional block: if <condition> then: */
    IF,

    /** "then" — ends the condition header in an if-block */
    THEN,

    /** "repeat" — begins a loop: repeat <n> times: */
    REPEAT,

    /** "times" — ends the count header in a repeat-block */
    TIMES,

    /** "else" — optional else-branch of an if-block (extension feature) */
    ELSE,

    // ----------------------------------------------------------------
    // Arithmetic Operators
    // ----------------------------------------------------------------

    /** "+" addition */
    PLUS,

    /** "-" subtraction */
    MINUS,

    /** "*" multiplication */
    STAR,

    /** "/" division */
    SLASH,

    // ----------------------------------------------------------------
    // Comparison Operators
    // ----------------------------------------------------------------

    /** ">" greater-than comparison */
    GREATER,

    /** "<" less-than comparison */
    LESS,

    /** "==" equality comparison */
    EQUAL_EQUAL,

    // ----------------------------------------------------------------
    // Punctuation
    // ----------------------------------------------------------------

    /** ":" colon that terminates if/repeat header lines */
    COLON,

    // ----------------------------------------------------------------
    // Literals
    // ----------------------------------------------------------------

    /** A numeric literal such as 42 or 3.14 */
    NUMBER,

    /** A quoted string literal such as "hello world" */
    STRING,

    // ----------------------------------------------------------------
    // Identifiers
    // ----------------------------------------------------------------

    /** Any sequence of letters/digits starting with a letter — a variable name */
    IDENTIFIER,

    // ----------------------------------------------------------------
    // Structural tokens
    // ----------------------------------------------------------------

    /** A line break; used to delimit instructions */
    NEWLINE,

    /** Marks the end of the token stream */
    EOF
}
