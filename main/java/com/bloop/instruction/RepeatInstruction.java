package com.bloop.instruction;

import com.bloop.runtime.Environment;
import java.util.List;

public class RepeatInstruction implements Instruction {

 
    private final int count;

    
    private final List<Instruction> body;

    

    public RepeatInstruction(int count, List<Instruction> body) {
        this.count = count;
        this.body  = body;
    }

   
    @Override
    public void execute(Environment env) {
        for (int i = 0; i < count; i++) {
            // Execute every instruction in the body for this iteration.
            // Because the Environment is shared, changes made in one iteration
            // are visible in the next (e.g. incrementing a counter variable).
            for (Instruction instruction : body) {
                instruction.execute(env);
            }
        }
    }

   
    @Override
    public String toString() {
        return "Repeat(" + count + ", body=" + body.size() + " instructions)";
    }
}
