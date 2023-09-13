package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        String s = new String("Azi e bine, fac bine");
        String[] s1 = s.split("[ ,]+");
        for(String s2 : s1){
            System.out.println(s2);
        }
    }
}