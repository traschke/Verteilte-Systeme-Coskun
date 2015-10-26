package de.bht.vs.fib.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    private DataOutputStream out;

    /**
     * This private variable holds the inputReader.
     */
    private DataInputStream in;

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
        this.out = new DataOutputStream(this.clientSocket.getOutputStream());
        this.in = new DataInputStream(clientSocket.getInputStream());
        int inputline;
        while ((inputline = in.readInt()) > -1) {
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
    private void sendAnswer(int answer) throws IOException {
        LOGGER.info("Sending answer: " + answer);
        out.writeInt(answer);
    }

    /**
     * Sends the errorcode -1 to the client.
     */
    private void sendIllegalInput() throws IOException {
        LOGGER.info("Illegal input received. Sending -1.");
        sendAnswer(-1);
    }

    /**
     * Sends the errorcode -2 to the client.
     */
    private void sendIllegalRange() throws IOException {
        LOGGER.info("Illegal range received. Sending -2.");
        sendAnswer(-2);
    }

    /**
     * Checks the inputline, if it matches the requested format.
     *
     * @param inputline The inputline from the client.
     * @return The int from the inputline.
     * @throws IllegalArgumentException  If the syntax is incorrect.
     * @throws IndexOutOfBoundsException If the requested fibonacci is >= 100.
     */
    private int checkInput(int inputline) throws IllegalArgumentException, IndexOutOfBoundsException {
        try {
            int zahl = inputline;
            if (zahl >= 100) {
                throw new IndexOutOfBoundsException();
            }
            return zahl;
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
