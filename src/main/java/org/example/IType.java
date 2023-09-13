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

    @Override
    public String toString() {
        StringBuilder iType = new StringBuilder();
        for(int i = 0 ; i < 6 ; i++){
            iType.append(op[i]);
        }
        for(int i = 0 ; i < 5 ; i++){
            iType.append(rs[i]);
        }
        for(int i = 0 ; i < 5 ; i++){
            iType.append(rt[i]);
        }
        for(int i = 0 ; i < 16 ; i++){
            iType.append(imm[i]);
        }
        return iType.toString();
    }
}

