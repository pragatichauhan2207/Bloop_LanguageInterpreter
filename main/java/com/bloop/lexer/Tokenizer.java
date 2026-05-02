package com.bloop.lexer;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    // ----------------------------------------------------------------
    // State
    // ----------------------------------------------------------------

  
    private final String source;

   
    private int pos;

    /** Column offset from the start of the current line (0-based internally, reported as 1-based). */
    private int lineStart;  // pos of first char of the current line

    /** Current 1-based line number (incremented each time we see '\n'). */
    private int line;

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

    
    public Tokenizer(String source) {
        this.source = source;
        this.pos       = 0;
        this.line      = 1;
        this.lineStart = 0;
    }

  

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < source.length()) {
            char ch = source.charAt(pos);

            // ---- whitespace (spaces and tabs) — skip silently ----
            if (ch == ' ' || ch == '\t') {
                pos++;
                continue;
            }

            // ---- comments: # to end of line — skip the rest of the line ----
            if (ch == '#') {
                skipLineComment();
                continue;
            }

            // ---- newline: emit at most one NEWLINE per run of blank lines ----
            if (ch == '\n') {
                tokens.add(new Token(TokenType.NEWLINE, "\\n", line, 1));
                line++;
                pos++;
                lineStart = pos;
                // skip any additional blank lines so the parser isn't flooded
                while (pos < source.length() && source.charAt(pos) == '\n') {
                    line++;
                    pos++;
                    lineStart = pos;
                }
                continue;
            }

            // ---- carriage return (Windows line endings) — ignore the \r ----
            if (ch == '\r') {
                pos++;
                continue;
            }

            // ---- numeric literal ----
            if (Character.isDigit(ch)) {
                tokens.add(readNumber());
                continue;
            }

            // ---- string literal ----
            if (ch == '"') {
                tokens.add(readString());
                continue;
            }

            // ---- identifier or keyword ----
            if (Character.isLetter(ch)) {
                tokens.add(readIdentifierOrKeyword());
                continue;
            }

            // ---- single- or double-character operators and punctuation ----
            Token opToken = tryReadOperator();
            if (opToken != null) {
                tokens.add(opToken);
                continue;
            }

            // ---- nothing matched — report the bad character ----
            throw new RuntimeException(
                "Unexpected character '" + ch + "' at line " + line);
        }

        // Always end the stream with EOF
        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    // ----------------------------------------------------------------
    // Private helpers — each reads one logical token
    // ----------------------------------------------------------------

    /** Returns the current 1-based column number. */
    private int currentColumn() {
        return pos - lineStart + 1;
    }


    private void skipLineComment() {
        while (pos < source.length() && source.charAt(pos) != '\n') {
            pos++;
        }
       
    }

    private Token readNumber() {
        int col = currentColumn();
        int start = pos;
        boolean sawDot = false;

        while (pos < source.length()) {
            char ch = source.charAt(pos);
            if (Character.isDigit(ch)) {
                pos++;
            } else if (ch == '.' && !sawDot) {
                // allow exactly one decimal point
                sawDot = true;
                pos++;
            } else {
                break;
            }
        }

        String text = source.substring(start, pos);
        return new Token(TokenType.NUMBER, text, line, col);
    }

    
    private Token readString() {
        int col = currentColumn();
        pos++; // skip opening '"'
        int start = pos;

        while (pos < source.length() && source.charAt(pos) != '"') {
            if (source.charAt(pos) == '\n') {
                throw new RuntimeException(
                    "Unterminated string literal at line " + line);
            }
            pos++;
        }

        if (pos >= source.length()) {
            throw new RuntimeException(
                "Unterminated string literal at line " + line);
        }

        String content = source.substring(start, pos);
        pos++; // skip closing '"'
        return new Token(TokenType.STRING, content, line, col);
    }

    
    private Token readIdentifierOrKeyword() {
        int col = currentColumn();
        int start = pos;

        while (pos < source.length()
                && (Character.isLetterOrDigit(source.charAt(pos))
                    || source.charAt(pos) == '_')) {
            pos++;
        }

        String word = source.substring(start, pos);

        // Match against the exact BLOOP keyword list (case-sensitive)
        switch (word) {
            case "put":    return new Token(TokenType.PUT, word, line, col);
            case "into":   return new Token(TokenType.INTO, word, line, col);
            case "print":  return new Token(TokenType.PRINT, word, line, col);
            case "if":     return new Token(TokenType.IF, word, line, col);
            case "then":   return new Token(TokenType.THEN, word, line, col);
            case "repeat": return new Token(TokenType.REPEAT, word, line, col);
            case "times":  return new Token(TokenType.TIMES, word, line, col);
            case "else":   return new Token(TokenType.ELSE, word, line, col);
            default:       return new Token(TokenType.IDENTIFIER, word, line, col);
        }
    }

    
    private Token tryReadOperator() {
        int col = currentColumn();
        char ch = source.charAt(pos);

        switch (ch) {
            case '+': pos++; return new Token(TokenType.PLUS, "+", line, col);
            case '-': pos++; return new Token(TokenType.MINUS, "-", line, col);
            case '*': pos++; return new Token(TokenType.STAR, "*", line, col);
            case '/': pos++; return new Token(TokenType.SLASH, "/", line, col);
            case '>': pos++; return new Token(TokenType.GREATER, ">", line, col);
            case '<': pos++; return new Token(TokenType.LESS, "<", line, col);
            case ':': pos++; return new Token(TokenType.COLON, ":", line, col);

            case '=':
                // could be '==' (equality) — peek at the next character
                if (pos + 1 < source.length() && source.charAt(pos + 1) == '=') {
                    pos += 2;
                    return new Token(TokenType.EQUAL_EQUAL, "==", line, col);
                }
                // bare '=' is not a valid BLOOP token
                return null;

            default:
                return null;
        }
    }
}
