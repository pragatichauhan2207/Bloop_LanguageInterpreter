package com.bloop.instruction;

import com.bloop.ast.Expression;
import com.bloop.runtime.Environment;
import java.util.List;

public class ElseInstruction implements Instruction {

    // ----------------------------------------------------------------
    // Fields
    // ----------------------------------------------------------------

    /** The boolean condition that determines which branch to execute. */
    private final Expression condition;

    /** Instructions to run when the condition is {@code true}. */
    private final List<Instruction> thenBody;

    /** Instructions to run when the condition is {@code false}. */
    private final List<Instruction> elseBody;

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

    
    public ElseInstruction(Expression condition,
                           List<Instruction> thenBody,
                           List<Instruction> elseBody) {
        this.condition = condition;
        this.thenBody  = thenBody;
        this.elseBody  = elseBody;
    }

    // ----------------------------------------------------------------
    // Instruction implementation
    // ----------------------------------------------------------------

    @Override
    public void execute(Environment env) {
        Object result = condition.evaluate(env);

        if (!(result instanceof Boolean)) {
            throw new RuntimeException(
                "Condition in 'if/else' must be a comparison, but got: " + result);
        }

        if ((Boolean) result) {
            for (Instruction instr : thenBody) {
                instr.execute(env);
            }
        } else {
            for (Instruction instr : elseBody) {
                instr.execute(env);
            }
        }
    }

    // ----------------------------------------------------------------
    // Object overrides
    // ----------------------------------------------------------------

    @Override
    public String toString() {
        return "IfElse(" + condition
            + ", then=" + thenBody.size()
            + ", else=" + elseBody.size() + " instructions)";
    }
}
