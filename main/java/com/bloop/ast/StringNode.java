package com.bloop.ast;

import com.bloop.runtime.Environment;

public class StringNode implements Expression {

  
    private final String value;

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

  
    public StringNode(String value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Environment env) {
        return value;
    }

    // ----------------------------------------------------------------
    // Object overrides
    // ----------------------------------------------------------------

    
    @Override
    public String toString() {
        return "StringNode(\"" + value + "\")";
    }
}
