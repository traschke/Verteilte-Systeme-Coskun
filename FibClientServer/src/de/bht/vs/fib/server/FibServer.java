package de.bht.vs.fib.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

public class FibServer {
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	public static void main(String[] args) throws IOException {
		FibServer fibServer = new FibServer(1337);
		fibServer.run();
	}
	
	public FibServer(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
	}
	
	public void run() throws IOException {
		System.out.println("Waiting for clients on " + Inet4Address.getLocalHost().getHostAddress() + ":" + this.serverSocket.getLocalPort());
		this.clientSocket = this.serverSocket.accept();
		System.out.println("Client " + clientSocket.getInetAddress().toString() + " connected!");
		this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
		this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String inputline;
		while ((inputline = in.readLine()) != null) {
			System.out.println("New command received: " + inputline);
			if (inputline.equals("exit")) {
				stop();
				break;
			}
			int input = Integer.valueOf(inputline);
			int fibo = fib(input);
			System.out.println("Sending answer: " + fibo);
			out.println(fibo);
		}
	}
	
	public void stop() throws IOException {
		System.out.println("FibServer shutting down...");
		serverSocket.close();
		clientSocket.close();
	}
	
	public int fib (int i) {
		if (i <= 0)
			return 0;
		else if(i==1)
			return 1;
		else
			return fib(i - 2) + fib(i - 1);
	}
}
