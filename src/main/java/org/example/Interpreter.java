package org.example;

import java.io.*;
import java.util.*;

public class Interpreter {
    private static final int SIZE = 128;
    private final Map<InstructionString, Instruction> instructionsToCode = new HashMap<>();
    InstructionString[] instructions = new InstructionString[128];
    private int count = 0;
    private int numInstr = 0;
    private final String destinationPath;

    public Interpreter(String filePath, String destinationPath) {
        int i = 0;
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine() && numInstr != SIZE) {
                String s = scanner.nextLine();
                instructions[i] = new InstructionString(s);
//                System.out.println(s);
                i++;
                numInstr++;
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        this.destinationPath = destinationPath;
        System.out.println("numInstr: " + numInstr);
    }

    public int[] getReg(int n, int size) {
        int[] bits = new int[size];
        int y = Math.abs(n);
        for (int i = size - 1; i >= 0; i--) {
            bits[i] = y % 2;
            y = y / 2;
        }
        if(n<0){
            boolean carry = true;
            for(int i = size-1 ; i >= 0 ; i--){
                bits[i] = bits[i] == 0 ? 1 : 0;
                if(carry){
                    if(bits[i] == 0){
                        carry = false;
                    }
                    bits[i] = bits[i]==0 ? 1:0;
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
            case "slt" -> {
                return new int[]{1, 0, 1, 0, 1, 0};
            }
            default -> {
                return new int[]{};
            }
        }
    }

    public int[] getOp(String op) {
        switch (op) {
            case "add", "sub", "or", "xor", "nor", "and", "slt" -> {
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
        int[] addr;
        for (int i = 0; i <= numInstr; i++) {
//            System.out.println(instructions[i].getInstruction());
            if (address.equals(instructions[i].getInstruction())) {
                count = 0;
                break;
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

    public int[] getBranchAdd(String s) {
        int counter = 0;
        for (int i = count; i < numInstr; i++) {
            if (instructions[i].getInstruction().equals(s)) {
                break;
            } else {
                counter++;
            }
        }
        return getReg(counter - 1, 16);
    }

    public void interpret() {
        for (int i = 0; i < numInstr; i++) {
            String[] parts = instructions[i].getInstruction().split("[ ,$()]+");
//            System.out.println(Arrays.toString(parts));
            int partsLength = parts.length;
            boolean parsed = false;
//            System.out.println(parts.length);
            if (partsLength == 4) {//or, and, nor, add,// sub, xor
                //op = 000000
//                System.out.println("sunt aici");
                int[] op = getOp(parts[0]);
                if (Arrays.equals(op, new int[]{0, 0, 0, 0, 0, 0})) {
                    int rs = Integer.parseInt(parts[1]);
                    int rt = Integer.parseInt(parts[2]);
                    int rd = Integer.parseInt(parts[3]);
                    int[] shamt = new int[]{0, 0, 0, 0, 0, 0};
                    int[] regs, regt, regd;
                    regs = getReg(rs, 5);
                    regt = getReg(rt, 5);
                    regd = getReg(rd, 5);
                    int[] func = getFunc(parts[0]);
                    instructionsToCode.put(instructions[i], new RType(op, regs, regt, regd, shamt, func));
                    parsed = true;
                } else {//addi, ori, andi
                    //op != 000100
                    if (!Arrays.equals(op, new int[]{0, 0, 0, 1, 0, 0})) {
                        int[] rs = getReg(Integer.parseInt(parts[1]), 5);
                        int[] rt = getReg(Integer.parseInt(parts[2]), 5);
                        int[] imm = getReg(Integer.parseInt(parts[3]), 16);
                        instructionsToCode.put(instructions[i], new IType(op, rs, rt, imm));
                        parsed = true;
                    } else {//beq calculez nr de adrese
                        int[] rs = getReg(Integer.parseInt(parts[1]), 5);
                        int[] rt = getReg(Integer.parseInt(parts[2]), 5);
                        int[] add = getBranchAdd(parts[3]);
                        instructionsToCode.put(instructions[i], new IType(op, rs, rt, add));
                        parsed = true;
                    }
                }
                count++;
            }
            if (partsLength == 2) {//jump
                int[] op = getOp(parts[0]);
                int[] addr = getJumpAddress(parts[1]);
                instructionsToCode.put(instructions[i], new JType(op, addr));
                count++;
                parsed = true;
            }
            if (partsLength == 3) {//lw, sw
                int[] op = getOp(parts[0]);
                int[] rs = getReg(Integer.parseInt(parts[1].substring(1)), 5);
                String[] regImm = parts[2].split("[$)(]+");
                int[] rt = getReg(Integer.parseInt(regImm[1]), 5);
                int[] imm = getReg(Integer.parseInt(regImm[0]), 16);
                instructionsToCode.put(instructions[i], new IType(op, rs, rt, imm));
                count++;
                parsed = true;
            }
            if (partsLength == 1) {
                parsed = true;
            }
            if (!parsed) {
                throw new IllegalArgumentException("Invalid Instruction: " + instructions[i].getInstruction());
            }
        }
    }

    public void writeFile() {//deschid destination path
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(destinationPath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int i = 0; i < numInstr; i++) {
                if (instructions[i].getInstruction().split(" ").length != 1) {
                    System.out.println(instructions[i]);
                    bufferedWriter.write(instructionsToCode.get(instructions[i]).toString());
                    if(i != numInstr -1) {
                        bufferedWriter.write("\n");
                    }
                }
            }
            bufferedWriter.close();
            fileWriter.close();
            System.out.println("Am scris cu succes");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
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
