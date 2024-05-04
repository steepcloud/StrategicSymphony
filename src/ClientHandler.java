import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
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
                
                if (lowerMessage.startsWith("color ")) {
                	String arg = lowerMessage.substring(6).trim();
                	server.changeColor(this, arg);
                } else if (lowerMessage.startsWith("ready")) {
                	server.changeReadyStatus(this);
                } else if (lowerMessage.startsWith("put")) {
                	String[] parts = lowerMessage.split(" ");
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    
                    server.putPiece(x, y, this);               
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
