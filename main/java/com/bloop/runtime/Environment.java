package com.bloop.runtime;

import java.util.HashMap;
import java.util.Map;


public class Environment {

    private final Map<String, Object> store = new HashMap<>();

    
    public void set(String name, Object value) {
        store.put(name, value);
    }

    
    public Object get(String name) {
        if (!store.containsKey(name)) {
            throw new RuntimeException(
                "Variable not defined: '" + name + "'. "
                + "Make sure you assign a value with 'put ... into " + name + "' before using it.");
        }
        return store.get(name);
    }

    
    public boolean isDefined(String name) {
        return store.containsKey(name);
    }

   
    @Override
    public String toString() {
        return "Environment" + store.toString();
    }
}
