package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Interpreter {
    private static final int SIZE = 128;
    private final Map<InstructionString, Instruction> instructionsToCode = new HashMap<>();
    InstructionString[] instructions = new InstructionString[128];
    int count = 0;
    int numInstr = 0;

    public Interpreter(String filePath) {
        int i = 0;
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine() && numInstr != SIZE) {
                instructions[i] = new InstructionString(scanner.nextLine());
                i++;
                numInstr++;
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public int[] getReg(int n, int size) {
        int[] bits = new int[size];
        int y = n;
        boolean found = false;
        if (n == 1 && size == 5) {
            return new int[]{0, 0, 0, 0, 1};
        }
        if (n == 0 && size == 16) {
            return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        }
        if (n == 1 && size == 16) {
            return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
        }
        if (n == 0 && size == 5) {
            return new int[]{0, 0, 0, 0, 0};
        }
        for (int i = size - 1; i >= 0; i--) {
            if (!found && y == 1) {
                bits[i] = 1;
                found = true;
            }
            if (y != 1 && !found) {
                bits[i] = y % 2;
                y = y / 2;
            } else {
                if (y == 1 && found) {
                    bits[i] = 0;
                }
            }
        }
        return bits;
    }

    public int[] getFunc(String func) {
        switch (func) {
            case "add" -> {
                return new int[]{1, 0, 0, 0, 0, 0};
            }
            case "and" -> {
                return new int[]{1, 0, 0, 1, 0, 0};
            }
            case "or" -> {
                return new int[]{1, 0, 0, 1, 0, 1};
            }
            case "xor" -> {
                return new int[]{1, 0, 0, 1, 1, 0};
            }
            case "nor" -> {
                return new int[]{1, 0, 0, 1, 1, 1};
            }
            case "sub" -> {
                return new int[]{1, 0, 0, 0, 1, 0};
            }
            default -> {
                return new int[]{};
            }
        }
    }

    public int[] getOp(String op) {
        switch (op) {
            case "add", "sub", "or", "xor", "nor", "and" -> {
                return new int[]{0, 0, 0, 0, 0, 0};
            }
            case "addi" -> {
                return new int[]{0, 0, 1, 0, 0, 0};
            }
            case "andi" -> {
                return new int[]{0, 0, 1, 1, 0, 0};
            }
            case "ori" -> {
                return new int[]{0, 0, 1, 1, 0, 1};
            }
            case "beq" -> {
                return new int[]{0, 0, 0, 1, 0, 0};
            }
            case "j" -> {
                return new int[]{0, 0, 0, 0, 1, 0};
            }
            case "lw" -> {
                return new int[]{1, 0, 0, 0, 1, 1};
            }
            case "sw" -> {
                return new int[]{1, 0, 1, 0, 1, 1};
            }
            default -> {
                return new int[]{};
            }
        }
    }

    public int[] getJumpAddress(String address) {
        int[] addr = new int[26];
        boolean found = false;
        for (int i = 0; i <= numInstr; i++) {
            if (address.equals(instructions[i].getInstruction())) {
                found = true;
                count = 0;
            } else {
                count++;
            }
        }
        int jumpAddr = count * 4;
        addr = getReg(jumpAddr, 26);
        for (int i = 0; i < 2; i++) {
            for (int j = 25; j >= 1; j--) {
                addr[j] = addr[j - 1];
            }
            addr[i] = 0;
        }
        return addr;
    }
    public int[] getBranchAdd(String s){
        int counter = 0;
        for(int i = count ; i <numInstr ; i++){
            if(instructions[i].getInstruction().equals(s)){
                break;
            }else {
                counter++;
            }
        }
        return getReg(counter-1, 16);
    }
    public void interpret() {
        for (int i = 0 ; i < numInstr ; i++) {
            String[] parts = instructions[i].getInstruction().split("[ ,]+");
            int partsLength = parts.length;
            switch (partsLength) {
                case 4: {//or, and, nor, add, sub, xor
                    //op = 000000
                    int[] op = getOp(parts[0]);
                    if (Arrays.equals(op, new int[]{0, 0, 0, 0, 0, 0})) {
                        int rs = Integer.parseInt(parts[1].substring(1));
                        int rt = Integer.parseInt(parts[2].substring(1));
                        int rd = Integer.parseInt(parts[3].substring(1));
                        int[] shamt = new int[5];
                        for (int j = 0; j <= 4; j++) {
                            shamt[j] = 0;
                        }
                        int[] regs, regt, regd;
                        regs = getReg(rs, 5);
                        regt = getReg(rt, 5);
                        regd = getReg(rd, 5);
                        int[] func = getFunc(parts[3]);
                        instructionsToCode.put(instructions[i], new RType(op, regs, regt, regd, shamt, func));
                    } else {//addi, ori, andi
                        //op != 000100
                        if (!Arrays.equals(op, new int[]{0, 0, 0, 1, 0, 0})) {
                            int[] rs = getReg(Integer.parseInt(parts[1].substring(1)), 5);
                            int[] rt = getReg(Integer.parseInt(parts[2].substring(1)), 5);
                            int[] imm = getReg(Integer.parseInt(parts[3]), 16);
                            instructionsToCode.put(instructions[i], new IType(op, rs, rt, imm));
                        } else {//beq calculez nr de adrese
                            int[] rs = getReg(Integer.parseInt(parts[1].substring(1)),5);
                            int[] rt = getReg(Integer.parseInt(parts[2].substring(1)),5);
                            int[] add = getBranchAdd(parts[3]);
                            instructionsToCode.put(instructions[i], new IType(op, rs, rt, add));
                        }
                    }
                    count++;
                }
                case 2: {//jump
                    int[] op = getOp(parts[0]);
                    int[] addr = getJumpAddress(parts[1]);
                    instructionsToCode.put(instructions[i], new JType(op, addr));
                    count++;
                }
                case 3: {//lw, sw
                    int[] op = getOp(parts[0]);
                    int[] rs = getReg(Integer.parseInt(parts[1].substring(1)), 5);
                    String[] regImmm = parts[2].split("[$)(]+");
                    int[] rt = getReg(Integer.parseInt(regImmm[1]), 5);
                    int[] imm = getReg(Integer.parseInt(regImmm[0]), 16);
                    instructionsToCode.put(instructions[i], new IType(op, rs, rt, imm));
                    count++;
                }
                default:{
                    throw new IllegalArgumentException("Invalid Instruction: " + instructions[i].getInstruction());
                }
            }
        }
    }
    public void writeFile(String filePath){

    }
    @Override
    public String toString() {
        StringBuilder instr = new StringBuilder();
        for (InstructionString i : instructionsToCode.keySet()) {
            instr.append(i.toString()).append("------").append(instructionsToCode.get(i).toString());
        }
        return instr.toString();
    }
}
