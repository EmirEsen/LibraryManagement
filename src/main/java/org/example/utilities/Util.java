package org.example.utilities;

import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public abstract class Util {

    public static Scanner scanner = new Scanner(System.in);

    //generates and returns random id
    public static String UUIDGenerator(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String stringScanner(String input){
        System.out.print(input);
        return scanner.nextLine();
    }

    public static int intScanner(String input){
        System.out.print(input);
        int inp = scanner.nextInt();
        scanner.nextLine();
        return inp;
    }
}
