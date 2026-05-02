package com.bloop.ast;

import com.bloop.runtime.Environment;


public interface Expression {

   
    Object evaluate(Environment env);

}
