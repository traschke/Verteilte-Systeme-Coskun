package de.bht.vs.fib.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        if (args[1] == null) {
            args[1] = "5678";
        }
        Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String input;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("$>");
        while ((input = stdIn.readLine()) != null) {
            if (input.equals("hilfe")) {
                System.out.println("hilfe\t\t-\tRuft die Hilfe auf.\n" +
                        "berechne\t-\tNimmt eine Zahl entgegen und gibt den Wert\n\t\t\t\tder Fibonacci Reihe an dieser Stelle aus.\n" +
                        "ende\t\t-\tBeendet das Programm.");
            }
            if (input.startsWith("berechne")) {
                input = input.split(" ")[1];
                try {
                    int inputString = Integer.valueOf(input);
                    System.out.println("Sending request: " + inputString);
                    out.println(inputString);
                } catch (NumberFormatException e) {
                    System.out.println("-1\tFehlerhafte Eingabe");
                }
                String answer;
                if ((answer = in.readLine()) != null) {
                    System.out.println("Server answer: " + answer);
                }
            }
            if (input.equals("ende")) {
                break;
            }
            System.out.print("$>");
        }
    }
}
