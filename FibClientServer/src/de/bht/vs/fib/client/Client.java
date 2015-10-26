package de.bht.vs.fib.client;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        if (args[1] == null) {
            args[1] = "5678";
        }
        Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());
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
                System.out.println(input);
                try {
                    int inputStringAsInt = Integer.valueOf(input);
                    System.out.println("Sending request: " + inputStringAsInt);
                    out.writeInt(inputStringAsInt);
                } catch (NumberFormatException e) {
                    System.out.println("-1\tFehlerhafte Eingabe");
                }
                int answer;
                if ((answer = in.readInt()) > -3) {
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
