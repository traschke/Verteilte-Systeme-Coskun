package de.bht.vs.fib.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("141.64.170.180", 1337);
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String input;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		while ((input = stdIn.readLine()) != null) {
			try {
				int inputString = Integer.valueOf(input);
				System.out.println("Sending request: " + inputString);
				out.println(inputString);
			} catch (NumberFormatException e) {
				out.println("Illegal input format. Please try again.");
			}
			if (input.equals("exit")) {
				break;
			}
			String answer;
			while ((answer = in.readLine()) != null) {
				System.out.println("Server answer: " + answer);
				break;
			}
		}
	}
}
