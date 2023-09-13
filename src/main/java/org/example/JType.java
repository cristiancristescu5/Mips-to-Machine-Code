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

    @Override
    public String toString() {
        StringBuilder j = new StringBuilder();
        for(int i = 0 ; i < 6 ; i++){
            j.append(op[i]);
        }
        for(int i = 0 ; i < 26 ; i++){
            j.append(add[i]);
        }
        return j.toString();
    }
}
