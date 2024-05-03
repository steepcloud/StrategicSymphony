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
        System.out.println(client.userName + " has disconnected.");
    }
    
    public void changeReadyStatus(ClientHandler client) {
    	client.ready = !client.ready;
    	//TODO send ready status to all other players
    }
    
    public void changeColor(ClientHandler client, String color) {
    	//check if color exist
    	if (ClientHandler.colors.contains(color)) {

        	//check if color is available
        	Boolean available = true;

        	for (ClientHandler i :clients) {
        		if (i.color.equalsIgnoreCase(color)) {
        			available = false;
        		}
        	}
        	if (available) {
            	client.color = color;
            	client.sendMessage("Color changed to "+color);
        	}
        	else {
        		client.sendMessage("Color is already taken");
        	}
        	//TODO send ready status to all other players
    	}
    	else {
    		client.sendMessage(color + " is not a valid color");
    	}
    	
    }
    
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }
}

class ClientHandler implements Runnable {
	
	public static List<String> colors = Arrays.asList("r","g","b","y","p") ;
	
    private Socket socket;
    private Server server;
    private BufferedReader reader;
    private PrintWriter writer;
    public String userName;
    public Boolean ready = false;
    public String color = "";

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error setting up streams: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
        	
            sendMessage("Enter your username:");
            userName = reader.readLine();
            System.out.println(userName + " has connected.");

            String clientMessage;
            String lowerMessage;
            do {
                clientMessage = reader.readLine();
                lowerMessage = clientMessage.toLowerCase();
                
                //start checking for commands
                
                if (clientMessage.startsWith("color ")) {
                	String arg = lowerMessage.substring(6).trim();
                	server.changeColor(this, arg);
                }
                
            } while (clientMessage != null && !clientMessage.equalsIgnoreCase("quit"));

            server.removeClient(this);
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error in ClientHandler: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    public void sendMessage(String message) {
        writer.println(message);
    }

}
