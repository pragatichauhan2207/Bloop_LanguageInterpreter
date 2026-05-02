package com.bloop.parser;

import com.bloop.ast.*;
import com.bloop.instruction.*;
import com.bloop.lexer.Token;
import com.bloop.lexer.TokenType;
import java.util.ArrayList;
import java.util.List;


public class Parser {

  
    /** The full list of tokens produced by the Tokenizer. */
    private final List<Token> tokens;

    /**
     * Index of the token we are currently looking at.
     * Starts at 0 and advances as we consume tokens.
     */
    private int current;

    public Parser(List<Token> tokens) {
        this.tokens  = tokens;
        this.current = 0;
    }


    public List<Instruction> parse() {
        List<Instruction> instructions = new ArrayList<>();

        // Keep parsing until we hit the end-of-file marker.
        while (!isAtEnd()) {
            // Skip blank lines (consecutive NEWLINEs or a NEWLINE at the start)
            skipNewlines();
            if (isAtEnd()) break;

            // Parse one complete instruction
            Instruction instruction = parseInstruction();
            if (instruction != null) {
                instructions.add(instruction);
            }
        }

        return instructions;
    }

 
    private Instruction parseInstruction() {
        Token current = peek();

        switch (current.getType()) {
            case PUT:    return parseAssignment();
            case PRINT:  return parsePrint();
            case IF:     return parseIf();
            case REPEAT: return parseRepeat();
            default:
                throw new RuntimeException(
                    "Line " + current.getLine() + ": unexpected token '"
                    + current.getValue() + "' — expected put, print, if, or repeat.");
        }
    }

 
    private Instruction parseAssignment() {
        consume(TokenType.PUT, "Expected 'put'");

        // Parse the right-hand-side expression
        Expression expr = parseExpression();

        // Expect the keyword 'into'
        consume(TokenType.INTO, "Expected 'into' after expression in 'put' statement");

        // Expect the target variable name
        Token varToken = consume(TokenType.IDENTIFIER,
            "Expected a variable name after 'into'");

        // Consume the trailing newline (if present)
        consumeNewlineIfPresent();

        return new AssignInstruction(varToken.getValue(), expr);
    }

 
    private Instruction parsePrint() {
        consume(TokenType.PRINT, "Expected 'print'");

        Expression expr = parseExpression();

        consumeNewlineIfPresent();

        return new PrintInstruction(expr);
    }

 
    private Instruction parseIf() {
        consume(TokenType.IF, "Expected 'if'");

        Expression condition = parseExpression();

        consume(TokenType.THEN, "Expected 'then' after condition in 'if' statement");
        consume(TokenType.COLON, "Expected ':' after 'then'");
        consume(TokenType.NEWLINE, "Expected a newline after 'if ... then:'");

        List<Instruction> thenBody = parseIndentedBody();

        // Check for optional else: clause (extension feature)
        skipNewlines();
        if (check(TokenType.ELSE)) {
            advance(); // consume 'else'
            consume(TokenType.COLON, "Expected ':' after 'else'");
            consume(TokenType.NEWLINE, "Expected a newline after 'else:'");
            List<Instruction> elseBody = parseIndentedBody();
            return new ElseInstruction(condition, thenBody, elseBody);
        }

        return new IfInstruction(condition, thenBody);
    }

  
    private Instruction parseRepeat() {
        consume(TokenType.REPEAT, "Expected 'repeat'");

        // The count must be a literal number
        Token countToken = consume(TokenType.NUMBER,
            "Expected a number after 'repeat'");
        int count = (int) Double.parseDouble(countToken.getValue());

        consume(TokenType.TIMES, "Expected 'times' after count in 'repeat' statement");
        consume(TokenType.COLON, "Expected ':' after 'times'");
        consume(TokenType.NEWLINE, "Expected a newline after 'repeat N times:'");

        // Parse the indented body
        List<Instruction> body = parseIndentedBody();

        return new RepeatInstruction(count, body);

    }

    
    private List<Instruction> parseIndentedBody() {
        List<Instruction> body = new ArrayList<>();

        while (!isAtEnd()) {
            skipNewlines();
            if (isAtEnd()) break;

            Token next = peek();

            // A token at column 1 means no leading whitespace — end of block.
            if (next.getColumn() <= 1) {
                break;
            }

            // Also stop if it is not a statement-starting keyword
            // (guards against any stray tokens that slipped through).
            if (!isBodyToken(next)) {
                break;
            }

            body.add(parseInstruction());
        }

        return body;
    }


    private boolean isBodyToken(Token token) {
        switch (token.getType()) {
            case PUT:
            case PRINT:
            case IF:
            case REPEAT:
                return true;
            default:
                return false;
        }
    }


    private Expression parseExpression() {
        // Parse the left-hand side (which may include * and / via parseTerm)
        Expression left = parseTerm();

        // Consume any sequence of + or - operations
        while (check(TokenType.PLUS) || check(TokenType.MINUS)) {
            String op = advance().getValue(); // "+" or "-"
            Expression right = parseTerm();
            left = new BinaryOpNode(left, op, right);
        }

        // Optional comparison at the end (>, <, ==)
        // Comparisons have lower precedence than arithmetic, so they are
        // checked after the full arithmetic expression has been assembled.
        if (check(TokenType.GREATER) || check(TokenType.LESS)
                || check(TokenType.EQUAL_EQUAL)) {
            String op = advance().getValue();
            Expression right = parseTerm();
            left = new BinaryOpNode(left, op, right);
        }

        return left;
    }

 
    private Expression parseTerm() {
        Expression left = parsePrimary();

        while (check(TokenType.STAR) || check(TokenType.SLASH)) {
            String op = advance().getValue(); // "*" or "/"
            Expression right = parsePrimary();
            left = new BinaryOpNode(left, op, right);
        }

        return left;
    }


    private Expression parsePrimary() {
        Token token = peek();

        switch (token.getType()) {
            case NUMBER:
                advance(); // consume the number token
                return new NumberNode(Double.parseDouble(token.getValue()));

            case STRING:
                advance(); // consume the string token
                return new StringNode(token.getValue());

            case IDENTIFIER:
                advance(); // consume the identifier token
                return new VariableNode(token.getValue());

            default:
                throw new RuntimeException(
                    "Line " + token.getLine() + ": expected a number, string, or "
                    + "variable name but found '" + token.getValue() + "'.");
        }
    }


    private Token peek() {
        return tokens.get(current);
    }


    private Token advance() {
        Token token = tokens.get(current);
        current++;
        return token;
    }


    private boolean check(TokenType type) {     
        return !isAtEnd() && peek().getType() == type;
    }


    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }


    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        Token found = peek();
        throw new RuntimeException(
            "Line " + found.getLine() + ": " + message
            + " — found '" + found.getValue() + "' (" + found.getType() + ") instead.");
    }

 
    private void skipNewlines() {
        while (check(TokenType.NEWLINE)) {
            advance();
        }
    }

   
    private void consumeNewlineIfPresent() {
        if (check(TokenType.NEWLINE)) {
            advance();
        }
    }
}
