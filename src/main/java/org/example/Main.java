package org.example;

import java.util.Arrays;

public class Main {
    public final static Interpreter interpreter = new Interpreter("C:\\Users\\crist\\OneDrive\\Desktop\\mipsToMachineCode\\src\\main\\resources\\instructions.txt",
                                                                "C:\\Users\\crist\\OneDrive\\Desktop\\mipsToMachineCode\\src\\main\\resources\\machineCodeInstructions.dat");
    public static void main(String[] args) {
        interpreter.interpret();
        System.out.println(interpreter.toString());
        interpreter.writeFile();
//        System.out.println("$223e4567".substring(1));
    }
}