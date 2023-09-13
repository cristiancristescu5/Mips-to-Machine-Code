package org.example;

public class IType extends Instruction {
    private int[] op = new int[6];
    private int[] rs = new int[5];
    private int[] rt = new int[5];
    private int[] imm = new int[16];

    public IType(int[] op, int[] rs, int[] rt, int[] imm) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
        this.imm = imm;
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

    public int[] getImm() {
        return imm;
    }
}

