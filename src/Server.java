import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private static final int PORT = 1234;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private Game game;

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
    	client.sendMessage("You are now "+ (client.ready ? "":"not ") +"ready");
    	
    	if (clients.size() > 1) {
    		for (ClientHandler i :clients) {
    			if (i.ready == false) {
    				return;
    			}
    		}
    		this.startGame();
    	}
    }
    
    public void changeColor(ClientHandler client, String color) {
    	//check if color exist
    	if (ClientHandler.colors.contains(color)) {
    		
        	if (isColorAvailable(color)) {
            	client.color = color;
            	client.sendMessage("Color changed to " + color);
        		System.out.println(client.userName + " changed to " + color);
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
    
    private void assignRandomColor(ClientHandler client) {
        List<String> availableColors = new ArrayList<>();
        for (String color : ClientHandler.colors) {
            if (isColorAvailable(color)) {
                availableColors.add(color);
            }
        }
        
        if (!availableColors.isEmpty()) {
            Collections.shuffle(availableColors);
            String selectedColor = availableColors.get(0);
            changeColor(client, selectedColor);
        } else {
        	System.out.println("No available colors.");
        }
    }
    
    private boolean isColorAvailable(String color) {
        for (ClientHandler client : clients) {
            if (color.equalsIgnoreCase(client.color)) {
                return false;
            }
        }
        return true;
    }
    
    private void startGame() {
    	for (ClientHandler client : clients) {
    		if (client.color == "") {
    			assignRandomColor(client);
    		}
    	}
    	System.out.println("Game ready to start");
    	game = new Game(6, 10, clients);
    	game.startGame();
    }
    
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }
}
