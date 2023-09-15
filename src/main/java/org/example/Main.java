package org.example;

import java.util.Arrays;

public class Main {
    public final static Interpreter interpreter = new Interpreter("src\\main\\resources\\instructions.txt",
                                                                "src\\main\\resources\\machineCodeInstructions.dat");
    public static void main(String[] args) {
        interpreter.interpret();
        System.out.println(interpreter);
        interpreter.writeFile();
    }
}