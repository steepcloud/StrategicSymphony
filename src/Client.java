import java.io.*;
import java.net.*;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            // Read the initial prompt from the server and respond with a username
            System.out.println(in.readLine());  // Should prompt for username
            String userName = stdIn.readLine();
            out.println(userName);

            System.out.println("Connected as " + userName + ". Type 'quit' to exit.");

            // Continuously read user input and send it to the server
            String userInput;
            while (true) {
                userInput = stdIn.readLine();
                if (userInput != null) {
                    out.println(userInput);
                    if ("quit".equalsIgnoreCase(userInput.trim())) {
                        break; // Exit the loop if user types 'quit'
                    }
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + HOST);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + HOST);
            System.exit(1);
        }
    }
}
