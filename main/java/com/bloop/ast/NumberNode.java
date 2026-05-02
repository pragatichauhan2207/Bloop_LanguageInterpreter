package com.bloop.ast;

import com.bloop.runtime.Environment;


public class NumberNode implements Expression {

    // ----------------------------------------------------------------
    // Field
    // ----------------------------------------------------------------

    /** The numeric value parsed from the source literal. Never changes after construction. */
    private final double value;

    
    public NumberNode(double value) {
        this.value = value;
    }

   
    @Override
    public Object evaluate(Environment env) {
        return value;  // autoboxed to Double
    }

    // ----------------------------------------------------------------
    // Object overrides
    // ----------------------------------------------------------------

  
    @Override
    public String toString() {
        return "NumberNode(" + value + ")";
    }
}
