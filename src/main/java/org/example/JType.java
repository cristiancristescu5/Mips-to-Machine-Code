package org.example;

import java.math.BigInteger;

public class JType extends Instruction {
    private final int[] op;
    private final int[] add;

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
        BigInteger intVal =  new BigInteger(j.toString(), 2);
        return intVal.toString(16);
    }

}