package com.bloop.ast;

import com.bloop.runtime.Environment;

public class VariableNode implements Expression {

   
    private final String name;


   
    public VariableNode(String name) {
        this.name = name;
    }

   
    public String getName() {
        return name;
    }

    @Override
    public Object evaluate(Environment env) {
        // Delegate to the Environment; it will throw a helpful error if undefined.
        return env.get(name);
    }

    
    @Override
    public String toString() {
        return "VariableNode(" + name + ")";
    }
}
