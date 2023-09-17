package org.example;

import java.math.BigInteger;

public class IType extends Instruction {
    private final int[] op;
    private final int[] rs;
    private final int[] rt;
    private final int[] imm;

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
        BigInteger intVal = new BigInteger(iType.toString(), 2);
        return intVal.toString(16);
    }
}

