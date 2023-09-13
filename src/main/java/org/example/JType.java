package org.example;

public class JType extends Instruction {
    private int[] op = new int[6];
    private int[] add = new int[26];

    public JType(int[] op, int[] add) {
        this.op = op;
        this.add = add;
    }

    public int[] getOp() {
        return op;
    }

    public int[] getAdd() {
        return add;
    }
}
