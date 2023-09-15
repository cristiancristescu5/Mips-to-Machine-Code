package org.example;

import java.io.*;
import java.util.*;

/**
 * Poate interepreta: add, sub, and, or ,xor, nor, slt, addi, andi, ori, lw, sw, beg, j
 */
public class Interpreter {
    private static final int SIZE = 128;
    private final Map<InstructionString, Instruction> instructionsToCode = new HashMap<>(); //asociere dintre instrcutiuni si codul masina corespondent
    private final InstructionString[] instructions = new InstructionString[128];
    private int count = 0;
    private int numInstr = 0;
    private final String destinationPath;
    private List<String> labels = new ArrayList<>();

    /**
     * @param filePath        fisierul din care citesc instructiunile
     * @param destinationPath fisierul in care scriu cod masina
     */
    public Interpreter(String filePath, String destinationPath) {
        int i = 0;
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                instructions[i] = new InstructionString(s);
                i++;
                numInstr++;
                if (numInstr >= SIZE) {
                    throw new IllegalArgumentException("Too Many Instructions");
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        this.destinationPath = destinationPath;
    }

    /**
     * @param n    un intreg
     * @param size numarul de biti de reprezentat
     * @return un vector ce reprezinta reprezentarea binara a lui n
     */
    public int[] getReg(int n, int size) {
        int[] bits = new int[size];
        int y = Math.abs(n);
        for (int i = size - 1; i >= 0; i--) {
            bits[i] = y % 2;
            y = y / 2;
        }
        if (n < 0) {
            boolean carry = true;
            for (int i = size - 1; i >= 0; i--) {
                bits[i] = bits[i] == 0 ? 1 : 0;
                if (carry) {
                    if (bits[i] == 0) {
                        carry = false;
                    }
                    bits[i] = bits[i] == 0 ? 1 : 0;
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
        int counter = 0;
        for (int i = 0; i < numInstr; i++) {
            if (instructions[i].getInstruction().equals(address)) {
                counter++;
            }
        }
        if (counter > 1 || counter == 0) {
            throw new IllegalArgumentException("This label: " + address + " does not exist or has more than one encounter.");
        } else {
            counter = 0;
        }
        for (int i = 0; i < numInstr; i++) {
            if (address.equals(instructions[i].getInstruction())) {
                break;
            } else {
                if (instructions[i].getInstruction().split(" ").length != 1) {
                    counter++;
                }
            }
        }
        int jumpAddr = (counter) * 4;
        addr = getReg(jumpAddr, 26);
        count = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 25; j >= 1; j--) {
                addr[j] = addr[j - 1];
            }
            addr[i] = 0;
        }
        return addr;
    }

    public int[] getBranchAddress(String s) {
        int counter = 0;
        for (int i = 0; i < numInstr; i++) {
            if (s.equals(instructions[i].getInstruction())) {
                counter++;
            }
        }
        if (counter > 1 || counter == 0) {
            throw new IllegalArgumentException("This label: " + s + " does not exist or has more than one encounter.");
        } else {
            counter = 0;
        }
        for (int i = count - 1; i < numInstr; i++) {
            if (instructions[i].getInstruction().equals(s)) {
                break;
            } else {
                if (instructions[i].getInstruction().split(" ").length != 1) {
                    counter++;
                }
            }
        }
        return getReg(counter - 1, 16);
    }

    public void interpret() {
        for (int i = 0; i < numInstr; i++) {
            String[] parts = instructions[i].getInstruction().split("[ ,$()]+");
            int partsLength = parts.length;
            boolean parsed = false;
            count++;
            if (partsLength == 4) {//or, and, nor, add,// sub, xor
                int[] op = getOp(parts[0]);
                if (Arrays.equals(op, new int[]{0, 0, 0, 0, 0, 0})) {
                    int[] shamt = new int[]{0, 0, 0, 0, 0, 0};
                    int[] regs, regt, regd;
                    regs = getReg(Integer.parseInt(parts[2]), 5);
                    regt = getReg(Integer.parseInt(parts[3]), 5);
                    regd = getReg(Integer.parseInt(parts[1]), 5);
                    int[] func = getFunc(parts[0]);
                    instructionsToCode.put(instructions[i], new RType(op, regs, regt, regd, shamt, func));
                    parsed = true;
                } else {//addi, ori, andi
                    //op != 000100
                    if (!Arrays.equals(op, new int[]{0, 0, 0, 1, 0, 0})) {
                        if (!Arrays.equals(op, new int[]{1, 0, 0, 0, 1, 1}) && !Arrays.equals(op, new int[]{1, 0, 1, 0, 1, 1})) {
                            int[] rs = getReg(Integer.parseInt(parts[2]), 5);
                            int[] rt = getReg(Integer.parseInt(parts[1]), 5);
                            int[] imm = getReg(Integer.parseInt(parts[3]), 16);
                            instructionsToCode.put(instructions[i], new IType(op, rs, rt, imm));
                            parsed = true;
                        } else {//lw, sw
                            int[] rs = getReg(Integer.parseInt(parts[3]), 5);
                            int[] rt = getReg(Integer.parseInt(parts[1]), 5);
                            int[] imm = getReg(Integer.parseInt(parts[2]), 16);
                            instructionsToCode.put(instructions[i], new IType(op, rs, rt, imm));
                            parsed = true;
                        }
                    } else {//beq
                        int[] rs = getReg(Integer.parseInt(parts[1]), 5);
                        int[] rt = getReg(Integer.parseInt(parts[2]), 5);
                        int[] add = getBranchAddress(parts[3]);//calculez nr de instructiuni pana la label
                        instructionsToCode.put(instructions[i], new IType(op, rs, rt, add));
                        if(!labels.contains(parts[3])){
                            labels.add(parts[3]);
                        }
                        parsed = true;
                    }
                }
            }
            if (partsLength == 2) {//jump
                int[] op = getOp(parts[0]);
                int[] addr = getJumpAddress(parts[1]);//calculez adresa de jump
                instructionsToCode.put(instructions[i], new JType(op, addr));
                count++;
                if(!labels.contains(parts[1])){
                    labels.add(parts[1]);
                }
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

    public boolean isUnusedLabels(){
        for(int i = 0 ; i < numInstr ; i++){
            if(instructions[i].getInstruction().split(" ").length==1){
                if(!labels.contains(instructions[i].getInstruction())){
                    return true;
                }
            }
        }
        return false;
    }
    public void writeFile() {
        if(!isUnusedLabels()) {
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(destinationPath);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                for (int i = 0; i < numInstr; i++) {
                    if (instructions[i].getInstruction().split(" ").length != 1) {
                        System.out.println(instructions[i].getInstruction());
                        bufferedWriter.write(instructionsToCode.get(instructions[i]).toString());
                        if (i != numInstr - 1) {
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
        }else {
            throw new IllegalArgumentException("There are unused labels.");
        }
    }
    @Override
    public String toString() {
        StringBuilder instr = new StringBuilder();
        for (int i = 0; i < numInstr; i++) {
            if (instructions[i].getInstruction().split(" ").length != 1) {
                instr.append(instructions[i].getInstruction()).append("------").append(instructionsToCode.get(instructions[i]).toString()).append("\n");
            }
        }
        return instr.toString();
    }
}
