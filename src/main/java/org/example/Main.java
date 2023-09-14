package org.example;
public class Main {
    public final static Interpreter interpreter = new Interpreter("C:\\Users\\crist\\OneDrive\\Desktop\\mipsToMachineCode\\src\\main\\resources\\instructions.txt");
    public static void main(String[] args) {
        interpreter.interpret();
        System.out.println(interpreter.toString());
    }
}