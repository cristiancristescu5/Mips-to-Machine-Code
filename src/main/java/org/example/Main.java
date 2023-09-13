package org.example;
public class Main {
    public static void main(String[] args) {
        String s = new String("Azi e bine, fac bine");
        String[] s1 = s.split("[ ,]+");
        for(String s2 : s1){
            System.out.println(s2);
        }
    }
}