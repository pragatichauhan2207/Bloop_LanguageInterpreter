package com.bloop.ast;

import com.bloop.runtime.Environment;

public class BinaryOpNode implements Expression {

    private final Expression left;

    private final String operator;

    private final Expression right;


    public BinaryOpNode(Expression left, String operator, Expression right) {
        this.left     = left;
        this.operator = operator;
        this.right    = right;
    }


    @Override
    public Object evaluate(Environment env) {

        // Step 1: evaluate both sides
        Object leftVal  = left.evaluate(env);
        Object rightVal = right.evaluate(env);

        // Step 2: dispatch on the operator
        switch (operator) {

            // ---- Arithmetic (both operands must be numbers) ----

            case "+":
                // Special case: allow string concatenation with +
                if (leftVal instanceof String || rightVal instanceof String) {
                    return formatValue(leftVal) + formatValue(rightVal);
                }
                return toDouble(leftVal, "+") + toDouble(rightVal, "+");

            case "-":
                return toDouble(leftVal, "-") - toDouble(rightVal, "-");

            case "*":
                return toDouble(leftVal, "*") * toDouble(rightVal, "*");

            case "/": {
                double divisor = toDouble(rightVal, "/");
                if (divisor == 0.0) {
                    throw new RuntimeException("Division by zero");
                }
                return toDouble(leftVal, "/") / divisor;
            }

            // ---- Comparisons (both operands must be numbers) ----

            case ">":
                return toDouble(leftVal, ">") > toDouble(rightVal, ">");

            case "<":
                return toDouble(leftVal, "<") < toDouble(rightVal, "<");

            case "==":
                // Equality works for both numbers and strings
                if (leftVal instanceof Double && rightVal instanceof Double) {
                    return leftVal.equals(rightVal);
                }
                return String.valueOf(leftVal).equals(String.valueOf(rightVal));

            default:
                throw new RuntimeException("Unknown operator: " + operator);
        }
    }

    // ----------------------------------------------------------------
    // Private helpers
    // ----------------------------------------------------------------

    
    private double toDouble(Object value, String operator) {
        if (value instanceof Double) {
            return (Double) value;
        }
        throw new RuntimeException(
            "Operator '" + operator + "' requires numeric operands, but got: "
            + value + " (" + value.getClass().getSimpleName() + ")");
    }

 
    private String formatValue(Object value) {
        if (value instanceof Double) {
            double d = (Double) value;
            if (d == Math.floor(d) && !Double.isInfinite(d)) {
                return String.valueOf((long) d);
            }
            return String.valueOf(d);
        }
        return String.valueOf(value);
    }

    // ----------------------------------------------------------------
    // Object overrides
    // ----------------------------------------------------------------

    /** Returns a debug-friendly representation of this node. */
    @Override
    public String toString() {
        return "BinaryOpNode(" + left + " " + operator + " " + right + ")";
    }
}
