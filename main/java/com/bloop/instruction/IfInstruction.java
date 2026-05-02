package com.bloop.instruction;

import com.bloop.ast.Expression;
import com.bloop.runtime.Environment;
import java.util.List;


public class IfInstruction implements Instruction {

    // ----------------------------------------------------------------
    // Fields
    // ----------------------------------------------------------------

    /** The boolean condition that controls whether the body runs. */
    private final Expression condition;

    
    private final List<Instruction> body;

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

    /**
     * Constructs an IfInstruction.
     *
     * @param condition the condition expression (must evaluate to Boolean)
     * @param body      the instructions to run when condition is true
     */
    public IfInstruction(Expression condition, List<Instruction> body) {
        this.condition = condition;
        this.body      = body;
    }

    // ----------------------------------------------------------------
    // Instruction implementation
    // ----------------------------------------------------------------

    
    @Override
    public void execute(Environment env) {
        // Step 1: evaluate the condition expression
        Object conditionValue = condition.evaluate(env);

        // Step 2: verify it is actually a boolean (catches programmer mistakes
        //         like "if 42 then:" which makes no sense in BLOOP)
        if (!(conditionValue instanceof Boolean)) {
            throw new RuntimeException(
                "Condition in 'if' statement must be a comparison (e.g. score > 50), "
                + "but got: " + conditionValue);
        }

        boolean conditionIsTrue = (Boolean) conditionValue;

        // Step 3: run the body only when the condition holds
        if (conditionIsTrue) {
            for (Instruction instruction : body) {
                instruction.execute(env);
            }
        }
        // If condition is false, the body is silently skipped.
    }

    // ----------------------------------------------------------------
    // Object overrides
    // ----------------------------------------------------------------

    /** Returns a debug-friendly description of this if-instruction. */
    @Override
    public String toString() {
        return "If(" + condition + ", body=" + body.size() + " instructions)";
    }
}
