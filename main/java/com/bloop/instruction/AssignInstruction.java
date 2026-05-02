package com.bloop.instruction;

import com.bloop.ast.Expression;
import com.bloop.runtime.Environment;

public class AssignInstruction implements Instruction {

    private final String variableName;

    /** The expression whose value is assigned to the variable. */
    private final Expression expression;

   
    public AssignInstruction(String variableName, Expression expression) {
        this.variableName = variableName;
        this.expression   = expression;
    }


    @Override
    public void execute(Environment env) {
        // Step 1: compute the value of the right-hand-side expression
        Object value = expression.evaluate(env);

        // Step 2: store the result in the environment under the variable name
        env.set(variableName, value);
    }

   
    @Override
    public String toString() {
        return "Assign(" + variableName + " = " + expression + ")";
    }
}
