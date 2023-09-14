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

    @Override
    public String toString() {
       StringBuilder rInstruction = new StringBuilder();
       for(int i = 0 ; i < 6 ; i++){
           rInstruction.append(op[i]);
       }
       for(int i = 0 ; i < 5 ; i++){
           rInstruction.append(rs[i]);
       }
        for(int i = 0 ; i < 5 ; i++){
            rInstruction.append(rt[i]);
        }
        for(int i = 0 ; i < 5 ; i++){
            rInstruction.append(rd[i]);
        }
        for(int i = 0 ; i < 5 ; i++){
            rInstruction.append(shamt[i]);
        }
        for(int i = 0 ; i < 6 ; i++){
            rInstruction.append(func[i]);
        }
        return Integer.toHexString(Integer.parseInt(rInstruction.toString(),2));
    }
}
