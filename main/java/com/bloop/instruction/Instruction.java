package com.bloop.instruction;

import com.bloop.runtime.Environment;


public interface Instruction {

   
    void execute(Environment env);
}
