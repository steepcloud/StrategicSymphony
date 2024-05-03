import java.util.List;
import java.util.Random;

public class Game {
	public int[][] board;
	public int numRows = 6;
	public int numCols = 10;
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
	
	public void startGame() {
		Random rand = new Random();
        for (ClientHandler player : players) {
            int row, col;
            do {
                row = rand.nextInt(numRows);
                col = rand.nextInt(numCols);
            } while (board[row][col] != 0);
            board[row][col] = player.id;
        }
        
        for (int i = 0; i < numRows; i++) {
        	for (int j = 0; j < numCols; j++) {
        		System.out.print(board[i][j] + " ");
        	}
        	System.out.println();
        }
	}
	
	public Boolean putPiece(int x, int y, int id) {
		if (id == players.get(currentTurn).id) {
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
