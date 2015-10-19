package de.bht.vs.fib.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FibServer {

    private final static Logger LOGGER = Logger.getLogger(FibServer.class.getName());

    /**
     * This static variable holds the standard port.
     */
    private static int STANDARD_PORT = 5678;

    /**
     * This private variable holds the ServerSocket instance.
     */
    private ServerSocket serverSocket;

    /**
     * This private variable holds the ClientSocket instance.
     */
	private Socket clientSocket;

    /**
     * This private variable holds the outStream.
     */
	private PrintWriter out;

    /**
     * This private variable holds the inputReader.
     */
	private BufferedReader in;

    /**
     * Default Constructor.
     *
     * @param port The port the Server should run on.
     * @throws IOException
     */
    public FibServer(int port) throws IOException {
        LOGGER.setLevel(Level.ALL);
        this.serverSocket = new ServerSocket(port);
    }

	public static void main(String[] args) throws IOException {
        int port;
        try {
            port = Integer.valueOf(args[0]);

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            port = STANDARD_PORT;
        }
        FibServer fibServer = new FibServer(port);
        fibServer.run();
    }

    /**
     * This method runs the server and listens for a {@link de.bht.vs.fib.client.Client} connecting to it.
     * @throws IOException
     */
	public void run() throws IOException {
        LOGGER.info("Waiting for clients on " + Inet4Address.getLocalHost().getHostAddress() + ":" + this.serverSocket.getLocalPort());
        this.clientSocket = this.serverSocket.accept();
        LOGGER.info("Client " + clientSocket.getInetAddress().toString() + " connected!");
        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String inputline;
		while ((inputline = in.readLine()) != null) {
            LOGGER.info("New command received: " + inputline);
            try {
                int zahl = checkInput(inputline);
                int fibo = fib(zahl);
                sendAnswer(fibo);
            } catch (IllegalArgumentException e) {
                sendIllegalInput();
            } catch (IndexOutOfBoundsException e) {
                sendIllegalRange();
            }

        }
    }

    /**
     * Sends an answer to the client.
     *
     * @param answer The answer.
     */
    private void sendAnswer(String answer) {
        LOGGER.info("Sending answer: " + answer);
        out.println(answer);
    }

    /**
     * Sends an answer to the client.
     *
     * @param answer The answer.
     */
    private void sendAnswer(int answer) {
        sendAnswer(String.valueOf(answer));
    }

    /**
     * Sends the errorcode -1 to the client.
     */
    private void sendIllegalInput() {
        LOGGER.info("Illegal input received. Sending -1.");
        sendAnswer("-1");
    }

    /**
     * Sends the errorcode -2 to the client.
     */
    private void sendIllegalRange() {
        LOGGER.info("Illegal range received. Sending -2.");
        sendAnswer("-2");
    }

    /**
     * Checks the inputline, if it matches the requested format.
     *
     * @param inputline The inputline from the client.
     * @return The int from the inputline.
     * @throws IllegalArgumentException  If the syntax is incorrect.
     * @throws IndexOutOfBoundsException If the requested fibonacci is >= 100.
     */
    private int checkInput(String inputline) throws IllegalArgumentException, IndexOutOfBoundsException {
        try {
            if (inputline.toLowerCase().startsWith("berechne")) {
                String parts[] = inputline.split(" ");
                int zahl = Integer.valueOf(parts[1]);
                if (zahl >= 100) {
                    throw new IndexOutOfBoundsException();
                }
                return zahl;
            } else {
                throw new IllegalArgumentException();
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * This methods stops the server.
     * @throws IOException
     */
	public void stop() throws IOException {
        LOGGER.info("FibServer shutting down...");
        serverSocket.close();
        clientSocket.close();
    }

    /**
     * This method calculates the Fibonacci-Number for at the given input.
     * @param i The position at which we want the Fibonacci-Number.
     * @return The Fibonacci-Number at the position i.
     */
	public int fib (int i) {
		if (i <= 0)
			return 0;
		else if(i==1)
			return 1;
		else
			return fib(i - 2) + fib(i - 1);
	}
}
