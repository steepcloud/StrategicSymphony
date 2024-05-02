import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private static final int PORT = 1234;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port " + PORT);
    }

    public void startServer() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println(client.getUserName() + " has disconnected.");
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private Server server;
    private BufferedReader reader;
    private String userName;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Enter your username:");
            userName = reader.readLine();
            System.out.println(userName + " has connected.");

            String clientMessage;
            do {
                clientMessage = reader.readLine();
                if (clientMessage != null && !clientMessage.equalsIgnoreCase("quit")) {
                    System.out.println("[" + userName + "]: " + clientMessage);
                }
            } while (clientMessage != null && !clientMessage.equalsIgnoreCase("quit"));

            server.removeClient(this);
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error in ClientHandler: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    String getUserName() {
        return userName;
    }
}
