package application;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private static final int PORT = 1234;
    private static final int MAX_CLIENTS = 5;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private Game game;
    private int nextClientId = 1;

    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port " + PORT);
    }

    public void startServer() {
        try {
            while (true) {
            	if (clients.size() < MAX_CLIENTS) {
	                Socket socket = serverSocket.accept();
	                ClientHandler clientHandler = new ClientHandler(socket, this, nextClientId);
	                nextClientId++;
	                clients.add(clientHandler);
	                new Thread(clientHandler).start();
            	}else {

            		System.out.println("Limita maxima de clienti a fost atinsa.");

            		break;

            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

            for (ClientHandler client : clients) {
                client.stop();
            }
        } catch (IOException ex) {
            System.err.println("Error stopping the server: " + ex.getMessage());
        }
    }
	
    public int getNumClients() {
        return clients.size();
    }

    public void addClient(ClientHandler client) {
        clients.add(client);
    }
	
    public List<ClientHandler> getClients() {
        return clients;
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println(client.getUserName() + " has disconnected.");
    }
    
    public void changeReadyStatus(ClientHandler client) {
    	client.setReady(!client.isReady());
    	client.sendMessage("You are now "+ (client.isReady() ? "" : "not ") +"ready");
    	
    	if (clients.size() > 1) {
    		for (ClientHandler i : clients) {
    			if (i.isReady() == false) {
    				return;
    			}
    		}
    		this.startGame();
    	}
    }
    
    public void putPiece(int x, int y, ClientHandler client) {
    	if (game.isValidMove(x, y, client.getId())) {
	    	if (game.putPiece(x, y, client.getId())) {
	    		if(client.getFreeDomJoker() == false) {
	    			client.sendMessage("You don't have freedom joker");
	    		}
	    		if(client.getReplaceJoker() == false) {
	    			client.sendMessage("You don't have replace joker");
	    		}
	    		if(client.getDoubleMoveJoker() == false) {
	    			client.sendMessage("You don't have double move joker");
	    		}
	    		client.sendMessage("Piece was placed.");
	    		System.out.println("Piece was placed.");
	    	}
	    	else {
	    		client.sendMessage("Not your turn");
	    		System.out.println("Not your turn.");
	    	}
    	} else {
    		client.sendMessage("Invalid move. You can only place a piece in an empty cell with at least one neighboring cell of the same color.");
            System.out.println("Invalid move. Player " + client.getUserName() + " tried to place piece at (" + x + ", " + y + ").");
    	}
    }
    
    public void toggleFreedomJoker(ClientHandler client) {
    	if (game.checkTurn(client.getId())) {
    		game.setFreedomJoker(!game.getFreedomJoker());
    		client.sendMessage("Freedom joker is now " + (game.getFreedomJoker() ? "on." : "off."));
    	} else {
    		client.sendMessage("Not your turn.");
    	}
    }
    
    public void toggleReplaceJoker(ClientHandler client) {
    	if (game.checkTurn(client.getId())) {
    		game.setReplaceJoker(!game.getReplaceJoker());
    		client.sendMessage("Replace joker is now " + (game.getReplaceJoker() ? "on." : "off."));
    	} else {
    		client.sendMessage("Not your turn.");
    	}
    }
    
    public void getPlayerColors(ClientHandler clientx) {
    	String message = "Colors:";
    	 for (ClientHandler client : clients) {
             message += client.getId() + "||" + client.getColor()+",";
             
             
         }
    	 if (!message.isEmpty()) {
    		    message = message.substring(0, message.length() - 1);
    	 }
    	 clientx.sendMessage(message);
    }
    
    public void toggleDoubleMoveJoker(ClientHandler client) {
    	if (game.checkTurn(client.getId())) {
    		game.setDoubleMoveJoker(!game.getDoubleMoveJoker());
    		client.sendMessage("Double move joker is now " + (game.getDoubleMoveJoker() ? "on." : "off."));
    	} else {
    		client.sendMessage("Not your turn.");
    	}
    }
    
    public void changeColor(ClientHandler client, String color) {
    	if (client.isGameStarted()) {
    		client.sendMessage("Game has already started. You cannot change color.");
    	} else {
	    	//check if color exists
	    	if (client.getColors().contains(color)) {
	    		
	        	if (isColorAvailable(color)) {
	            	client.setColor(color);
	            	client.sendMessage("Color changed to " + color);
	        		System.out.println(client.getUserName() + " changed to " + color);
	        		
	        		sendReadyStatusToOtherPlayers(client);
	        	}
	        	else {
	        		client.sendMessage("Color is already taken");
	        	}
	    	}
	    	else {
	    		client.sendMessage(color + " is not a valid color");
	    	}
    	}
    }
    
    private void sendReadyStatusToOtherPlayers(ClientHandler changedClient) {
        for (ClientHandler player : clients) {
            if (player != changedClient) {
                player.sendMessage(changedClient.getUserName() + " changed color and is now " + (changedClient.isReady() ? "" : "not ") + "ready.");
            }
        }
    }
    
    public Boolean isValidUserName(String name) {
    	return !name.contains(" ") && name.length() <= 50;
    }
    
    public Boolean isUniqueUserName(String name) {
    	if (name == null) {
    		return false;
    	}
        int count = 0;

        for (ClientHandler client : clients) {
        	System.out.println(client.getUserName());
            if (client.getUserName().equalsIgnoreCase(name)) {
                count++;
		    
                if (count > 1) {
                    return false;
                }
            }
        }

        return true;
    }
    
    private void assignRandomColor(ClientHandler client) {
        List<String> availableColors = new ArrayList<>();
        for (String color : client.getColors()) {
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
            if (color.equalsIgnoreCase(client.getColor())) {
                return false;
            }
        }
        return true;
    }
    
    private void startGame() {
    	for (ClientHandler client : clients) {
    		if (client.getColor() == "") {
    			assignRandomColor(client);
    		}
    	}
    	for (ClientHandler client : clients) {
    		getPlayerColors(client);
    	}
    	System.out.println("Game ready to start");
    	for (ClientHandler client : clients) {
    		client.setGameStarted(true);
    	}
    	game = new Game(6, 10, clients, this);
    	game.startGame();
    	updatePlayers();
    }
    
    public void updatePlayers() {
    	String sboard = "";
    	for (int i = 0; i < game.getNumRows(); i++) {
    		for (int j = 0; j < game.getNumCols(); j++) {
    			sboard += game.getValueAtPosition(i, j) + " ";
    		}
    		sboard += "|";
    	}
    	
    	for (int i = 0; i < clients.size(); i++) {
    		clients.get(i).sendMessage(sboard + (game.currentTurn == i ? "|Your turn" : ""));
    	}
    }
    
    public ClientHandler getHandlerById(int id) throws Exception {
    	for (ClientHandler client :this.clients) {
    		if (client.getId() == id) {
    			return client;
    		}
    	}
    	throw new Exception("No Handler found for id:"+id);
    }
    
    public void pass(ClientHandler client) {
    	if (game.checkTurn(client.getId())) {
    		game.pass(client);
    		if(!game.checkEnd()) {
        		updatePlayers();
    		}else {
    			endGame();
    		}
    	}
    	else {
    		client.sendMessage("Not your turn");
    	}
    }
    
    private void endGame() {
    	ClientHandler winner = game.findWinner();

    	for (int i = 0; i < clients.size(); i++) {
    		System.out.println(clients.get(i).getUserName());
    		clients.get(i).sendMessage("The winner is "+winner.getUserName());
    	}
    }
    
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }
    
}
