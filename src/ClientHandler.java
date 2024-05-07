import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;

class ClientHandler implements Runnable {
	private static List<String> colors = Arrays.asList("r","g","b","y","p") ;
	private int id;
    private Socket socket;
    private Server server;
    private BufferedReader reader;
    private PrintWriter writer;
    private String userName;
    private Boolean ready = false;
    private String color = "";
    private Boolean freedomJoker = true;
	private Boolean replaceJoker = true;
    private Boolean doubleMoveJoker = true;
    private Boolean gameStarted = false;

    public void setFreedomJoker(Boolean freedomJoker) {
		this.freedomJoker = freedomJoker;
	}
    public void setReplaceJoker(Boolean replaceJoker) {
		this.replaceJoker = replaceJoker;
	}
    public void setDoubleMoveJoker(Boolean doubleMoveJoker) {
		this.doubleMoveJoker = doubleMoveJoker;
	}
    
    public ClientHandler(Socket socket, Server server, int id) {
        this.socket = socket;
        this.server = server;
        this.id= id;
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
    
    public String getUserName() {
    	return userName;
    }
    
    public int getId() {
    	return id;
    }
    
    public Boolean isReady() {
    	return ready;
    }
    
    public void setReady(Boolean ready) {
    	this.ready = ready;
    }
    
    public Boolean isGameStarted() {
    	return gameStarted;
    }
    
    public void setGameStarted(Boolean gameStarted) {
    	this.gameStarted = gameStarted;
    }
    
    public String getColor() {
    	return color;
    }
    
    public void setColor(String color) {
    	this.color = color;
    }
    
    public List<String> getColors() {
    	return colors;
    }
    
    @Override
    public void run() {
        try {
        	
        	sendMessage("Enter your username:");
            userName = reader.readLine();
        	// TODO: valid / unique username check
        	/*
        	boolean validUsername = false;
            while (!validUsername) {
                sendMessage("Enter your username:");
                userName = reader.readLine();
                
                if (!server.isValidUserName(userName)) {
                    sendMessage("Invalid username. Username should not contain spaces and should be at most 50 characters long.");
                } else if (!server.isUniqueUserName(userName)) {
                    sendMessage("Username already taken. Please choose a different username.");
                } else {
                    validUsername = true;
                }
            }
            */
            
            System.out.println(userName + " has connected.");

            String clientMessage;
            String lowerMessage;
            do {
                clientMessage = reader.readLine();
                lowerMessage = clientMessage.toLowerCase();
                
                //start checking for commands
                if (!gameStarted) {
	                if (lowerMessage.startsWith("color ")) {
	                	String arg = lowerMessage.substring(6).trim();
	                	server.changeColor(this, arg);
	                } else if (lowerMessage.startsWith("ready")) {
	                	server.changeReadyStatus(this);
	                }
                } else if (lowerMessage.startsWith("put")) {
                	String[] parts = lowerMessage.split(" ");
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    
                    server.putPiece(x, y, this);               
                } else if (lowerMessage.startsWith("jokers")) {
                	String message = "";
                	if (this.freedomJoker) {
                		message += "freedom joker";
                	}
                	if (this.replaceJoker) {
                		message += (message.isEmpty() ? "" : ", ") + "replace joker";
                	}
                	if (this.doubleMoveJoker) {
                		message += (message.isEmpty() ? "" : ", ") + "double move joker";
                	}
                	
                	this.sendMessage((message.isEmpty() ? "You don't have any jokers." : message + "."));
                } else if (lowerMessage.startsWith("freedom")) {
                	if (this.freedomJoker) {
                		server.toggleFreedomJoker(this);
                	} else {
                		this.sendMessage("You don't have this joker.");
                	}
                } else if (lowerMessage.startsWith("replace")) {
                	if (this.replaceJoker) {
                		server.toggleReplaceJoker(this);
                	} else {
                		this.sendMessage("You don't have this joker.");
                	}
                } else if (lowerMessage.startsWith("double move")) {
                	if (this.doubleMoveJoker) {
                		server.toggleDoubleMoveJoker(this);
                	} else {
                		this.sendMessage("You don't have this joker.");
                	}
                	
                } else if (lowerMessage.startsWith("pass")) {
                	server.pass(this);
                }
            } while (clientMessage != null && !clientMessage.equalsIgnoreCase("quit"));

            server.removeClient(this);
            socket.close();
        } catch (SocketException e) {
        	System.out.println("Connection reset by client.");
        } catch (IOException ex) {
            System.out.println("Error in ClientHandler: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
        	try {
        		reader.close();
        		writer.close();
        		socket.close();
        		server.removeClient(this);
        	} catch (IOException ex) {
        		System.out.println("Error closing resources: " + ex.getMessage());
        	}
        }
    }
    
    public void sendMessage(String message) {
        writer.println(message);
    }

}
