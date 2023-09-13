package org.example;

public class InstructionString {
    private String instruction;
    public InstructionString(String instruction){
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    @Override
    public String toString() {
        return instruction;
    }
}
