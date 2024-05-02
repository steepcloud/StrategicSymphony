import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Player {
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private String name;
	private PrintWriter out;
    private BufferedReader in;
	
    /*
	public Player(Socket socket) {
		this.socket = socket;
		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
    public Player(Socket socket, String playerName) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.name = playerName;

        // Send player name to server upon connection
        sendMessage(playerName);
    }
    
    public String getName() {
        return name;
    }
	
    /*
	public void run() {
		try {
			name = (String) inputStream.readObject();
			System.out.println("Player " + name + " connected.");
			
			joinGame();
			
			while(true) {
				// TODO
				Object receivedObject = inputStream.readObject();
				if (receivedObject instanceof String) {
					String jokerType = (String) receivedObject;
					handleJoker(this, jokerType);
				}
				else {
					// is this even needed? idk
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	*/
    
    public void run() {
        try {
            String playerName = getPlayerNameFromInput();
            System.out.println("Connected to server. Player name: " + playerName);

            sendMessage(playerName); // Send player name to server upon connection

            while (true) {
                // Handle communication with the server
            	String receivedMessage = receiveMessage();
                System.out.println("Server: " + receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }
    
    private String getPlayerNameFromInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your player name:");
        String playerName = scanner.nextLine();
        scanner.close();
        return playerName;
    }
	
	public void placePiece(int row, int col) {
		/*try {
			outputStream.writeObject(new Move(row, col));
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	
	public void useJoker(String jokerType) {
		try {
			outputStream.writeObject(jokerType);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleJoker(Player player, String jokerType) {
		// TODO
		switch (jokerType) {
			case "mutare dubla":
				player.useJoker("mutare dubla");
			case "inlocuire":
				player.useJoker("inlocuire");
			case "libertate":
				player.useJoker("libertate");
			default:
				System.out.println("Unknown joker type.");
				break;
		}
	}
	
	public void updateGameState(GameState gameState) {
		// TODO
	}
	
	private void joinGame() {
		try {
			outputStream.writeObject("JOIN_GAME");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void disconnect() {
		try {
			outputStream.writeObject("DISCONNECT");
			socket.close();
			System.out.println("Player " + name + " disconnected.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message) {
		// TODO
		out.println(message);
	}
	
	public String receiveMessage() throws IOException {
        return in.readLine();
    }
	
	public void startListeningForMessages() {
        new Thread(() -> {
            try {
                String fromServer;
                while ((fromServer = in.readLine()) != null) {
                    System.out.println("Server: " + fromServer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }).start();
    }
	
	public void closeConnection() {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing the connection: " + e.getMessage());
        }
    }
	
	public void sendGameState(GameState gameState) {
		// TODO
	}
	
	public void receiveMessages() {
        new Thread(() -> {
            try {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
	
	public static void main(String[] args) {
	    try {
	        Socket socket = new Socket("localhost", 10001);
	        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	        Scanner scanner = new Scanner(System.in);
	        System.out.println("Enter your player name:");
	        String playerName = scanner.nextLine();

	        // Send player name to server upon connection
	        out.println(playerName);

	        // Close the resources associated with this player
	        out.close();
	        in.close();
	        socket.close();
	        scanner.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
