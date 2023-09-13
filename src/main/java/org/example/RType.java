package org.example;

public class RType extends Instruction {
    private int[] op = new int[6];
    private int[] rs = new int[5];
    private int[] rt = new int[5];
    private int[] rd = new int[5];
    private int[] shamt = new int[5];
    private int[] func = new int[6];

    public RType(int[] op, int[] rs, int[] rt, int[] rd, int[] shamt, int[] func) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
        this.rd = rd;
        this.shamt = shamt;
        this.func = func;
    }

    public int[] getOp() {
        return op;
    }

    public int[] getRs() {
        return rs;
    }

    public int[] getRt() {
        return rt;
    }

    public int[] getRd() {
        return rd;
    }

    public int[] getShamt() {
        return shamt;
    }

    public int[] getFunc() {
        return func;
    }
}
