package de.bht.vs.fib.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 1337);
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String input;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		while ((input = stdIn.readLine()) != null) {
			out.println(input);
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
