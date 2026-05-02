package com.bloop.main;

import com.bloop.interpreter.Interpreter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

   
    public static void main(String[] args) {

        // ---- Validate command-line usage --------------------------------
        if (args.length != 1) {
            System.err.println("Usage: bloop <source-file.bloop>");
            System.err.println("Example: bloop hello.bloop");
            System.exit(1);
        }

        String filePath = args[0];

        // ---- Read source file into a string ----------------------------
        String sourceCode;
        try {
            sourceCode = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.err.println("Error: cannot read file '" + filePath + "'");
            System.err.println("  " + e.getMessage());
            System.exit(1);
            return; // unreachable, but required so the compiler knows sourceCode is set
        }

        // ---- Run the interpreter ---------------------------------------
        try {
            Interpreter interpreter = new Interpreter();
            interpreter.run(sourceCode);
        } catch (RuntimeException e) {
            // Print the error message neatly instead of a raw Java stack trace
            System.err.println("BLOOP Runtime Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
