import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	private static final int PORT = 10001;
	private static final int MAX_PLAYERS = 5;
	private static final int MIN_PLAYERS = 2;
	private int numRows = 6;
	private int numCols = 10;
	private boolean gameStarted = false;
	
	private List<Player> players;
	private GameState gameState;
	
	public Server() {
		players = new ArrayList<>();
		gameState = new GameState(numRows, numCols);
	}
	
	public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started, waiting for players...");

            while (true) {
                if (players.size() < MAX_PLAYERS && !gameStarted) {
                    Socket socket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String playerName = in.readLine(); // Read player name from client

                    Player player = new Player(socket, playerName);
                    players.add(player);
                    
                    System.out.println("New player joined: " + player.getName());

                    if (players.size() >= MIN_PLAYERS) {
                        broadcastMessage("Enough players have joined. Send 'start' to begin the game.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public synchronized void handleCommand(Player player, String command) {
        if (command.toLowerCase().equals("start") && players.size() >= MIN_PLAYERS && !gameStarted) {
            gameStarted = true;
            broadcastMessage("Game has started!");
            initializeGame();
        } else {
            player.sendMessage("Invalid command or insufficient players to start the game.");
        }
    }
	
	private void initializeGame() {
        // TODO
        System.out.println("Game initialization...");
    }
	
	public synchronized void placePiece(Player player, int row, int col) {
		if (gameState.isValidMove(player, row, col)) {
			gameState.placePiece(player, row, col);
			broadcastMessage(player.getName() + " placed a piece to (" + row + ", " + col + ")");
			updateGameState();
		} else {
			player.sendMessage("Invalid move. Please retry.");
		}
	}
	
	public synchronized void checkPiece(Player player, int row, int col) {
		Piece piece = gameState.checkPiece(row, col);
		player.sendMessage("Piece from position (" + row + ", " + col + ") is the colour " + piece.getColor());
	}
	
	private synchronized void updateGameState() {
		for (Player player : players) {
			player.sendGameState(gameState);
		}
	}
	
	private synchronized void broadcastMessage(String message) { 
		for (Player player : players) {
			player.sendMessage(message);
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}

}
