package com.bloop.instruction;

import com.bloop.ast.Expression;
import com.bloop.runtime.Environment;


public class PrintInstruction implements Instruction {

    
    private final Expression expression;

    public PrintInstruction(Expression expression) {
        this.expression = expression;
    }

    // ----------------------------------------------------------------
    // Instruction implementation
    // ----------------------------------------------------------------

    @Override
    public void execute(Environment env) {
        // Step 1: evaluate the expression to get the value
        Object value = expression.evaluate(env);

        // Step 2: format and print the value
        System.out.println(formatValue(value));
    }

    
    private String formatValue(Object value) {
        if (value instanceof Double) {
            double d = (Double) value;
            // If the number has no fractional part, print it as a long integer
            if (d == Math.floor(d) && !Double.isInfinite(d)) {
                return String.valueOf((long) d);
            }
            return String.valueOf(d);
        }
        // Strings and booleans: use their natural toString representation
        return String.valueOf(value);
    }




    @Override
    public String toString() {
        return "Print(" + expression + ")";
    }
}
