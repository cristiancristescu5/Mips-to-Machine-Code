package org.example;

import java.util.Arrays;

public class Main {
    public final static Interpreter interpreter = new Interpreter("C:\\Users\\crist\\OneDrive\\Desktop\\mipsToMachineCode\\src\\main\\resources\\instructions.txt");
    public static void main(String[] args) {
//        interpreter.interpret();
//        System.out.println(interpreter.toString());
        String s = new String("80($90)");
        String[]s1 = s.split("[()$]+");
        System.out.println(Arrays.toString(s1));
    }
}