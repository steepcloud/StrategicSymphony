import java.util.List;
import java.util.Random;

public class Game {
	private int[][] board;
	private int numRows = 6;
	private int numCols = 10;
	private List<ClientHandler> players;
	public int currentTurn;
	private boolean keepTurn = false;
	private Server server;
	
	public Game(int numRows, int numCols, List<ClientHandler> players, Server server) {
		this.numRows = numRows;
		this.numCols = numCols;
		this.board = new int[numRows][numCols];
		this.players = players;
		currentTurn = 0;
		this.server = server;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumCols() {
		return numCols;
	}
	
	public int getValueAtPosition(int x, int y) {
		return board[x][y];
	}
	
	public void startGame() {
		Random rand = new Random();
        for (ClientHandler player : players) {
            int row, col;
            do {
                row = rand.nextInt(numRows);
                col = rand.nextInt(numCols);
            } while (board[row][col] != 0);
            board[row][col] = player.getId();
        }
        
        for (int i = 0; i < numRows; i++) {
        	for (int j = 0; j < numCols; j++) {
        		System.out.print(board[i][j] + " ");
        	}
        	System.out.println();
        }
	}
	
	public Boolean putPiece(int x, int y, int id) {
		if (id == players.get(currentTurn).getId()) {
			board[x][y] = id;
			advanceTurn(); 
			return true;
		}
		else {
			return false;
		}
	}
	
	private void advanceTurn() {
		if (!keepTurn) {
			currentTurn = (currentTurn + 1) % players.size();
		}
		else {
			keepTurn = false;
		}
		
		server.updatePlayers();
	}
}
