package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Interpreter {
    private Map<InstructionString, Instruction> instructionsToCode = new HashMap<>();
    List<InstructionString> instructionList = new ArrayList<>();
    public Interpreter(String filePath) {
        try{
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                instructionList.add(new InstructionString(scanner.nextLine()));
            }
            scanner.close();
        }catch (FileNotFoundException e){
            System.err.println(e.getMessage());
        }
    }
    public int[] getReg(int n, int size){
        int[] bits = new int[size];
        int y = n;
        boolean found = false;
        if(n == 1 && size == 5){
            return new int[]{0,0,0,0,1};
        }
        if(n == 0 && size == 16){
            return new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        }
        if(n == 1 && size == 16){
            return new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
        }
        if(n==0 && size == 5){
            return new int[]{0,0,0,0,0};
        }
        for(int i = size-1 ; i >= 0 ; i--){
            if(!found && y == 1){
                bits[i] = 1;
                found = true;
            }
            if(y!=1 && !found){
                bits[i]=y%2;
                y = y/2;
            }else {
                if(y==1 && found) {
                    bits[i] = 0;
                }
            }
        }
        return bits;
    }
    public int[] getFunc(String func){
        int[] function;
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
    public void interpret(){
        for(InstructionString i : instructionList) {
            String[] parts = i.getInstruction().split("[ ,]+");
            int partsLength = parts.length;
            switch (partsLength){
                case 6:{
                    //op = 000000
                    int[] op = new int[6];
                    for(int j = 0 ; j <= 5 ; j ++){
                        op[j] = 0;
                    }
                    int rs = Integer.parseInt(parts[1].substring(1));
                    int rt = Integer.parseInt(parts[2].substring(1));
                    int rd = Integer.parseInt(parts[3].substring(1));
                    int[] shamt = new int[5];
                    for(int j = 0 ; j <= 4 ; j++){
                        shamt[j]=0;
                    }
                    int[] regs, regt, regd = new int[5];
                    regs = getReg(rs, 5);
                    regt = getReg(rt, 5);
                    regd = getReg(rd, 5);
                    int[] func = getFunc(parts[5]);
                    instructionsToCode.put(i, new RType(op,regs,regt, regd, shamt, func));
                }
                case 2:{

                }
                case 4:{

                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder instr = new StringBuilder();
        for(InstructionString i : instructionsToCode.keySet()){
            instr.append(i.toString()).append("------").append(instructionsToCode.get(i).toString());
        }
        return instr.toString();
    }
}
