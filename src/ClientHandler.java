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
	
	public static List<String> colors = Arrays.asList("r","g","b","y","p") ;
	private static int lastId = 1;
	
	public int id;
	
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
        id = lastId;
        lastId = lastId + 1;
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
