package com.epam.io;

import java.util.Scanner;

public class ConsoleIO {
    public void print(String message) {
        System.out.println(message);
    }

    public void errPrint(String message) {
        System.err.println(message);
    }

    public String scanner() {
        return new Scanner(System.in).nextLine();
    }
}
