import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {
	private int[][] board;
	private int numRows = 6;
	private int numCols = 10;
	private List<ClientHandler> players;
	public int currentTurn;
	private Server server;
	private boolean doubleMoveJokerUsed = false;
    private Boolean replaceJokerUsed = false;
    private Boolean freedomJokerUsed = false;
    private List<ClientHandler> playerPassOrder = new CopyOnWriteArrayList<>();
	
	public Game(int numRows, int numCols, List<ClientHandler> players, Server server) {
		this.numRows = numRows;
		this.numCols = numCols;
		this.board = new int[numRows][numCols];
		this.players =  new ArrayList<>(players);
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
	
	public Boolean getFreedomJoker() {
		return freedomJokerUsed;
	}
	
	public Boolean getDoubleMoveJoker() {
		return doubleMoveJokerUsed;
	}
	
	public Boolean getReplaceJoker() {
		return replaceJokerUsed;
	}
	
	public void setFreedomJoker(Boolean freedomJokerUsed) {
		this.freedomJokerUsed = freedomJokerUsed;
	}
	
	public void setReplaceJoker(Boolean replaceJokerUsed) {
		this.replaceJokerUsed = replaceJokerUsed;
	}
	
	public void setDoubleMoveJoker(Boolean doubleMoveJokerUsed) {
		this.doubleMoveJokerUsed = doubleMoveJokerUsed;
	}
	
	public Boolean checkTurn(int id) {
		return id == players.get(currentTurn).getId();
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
		if (checkTurn(id)) {
			board[x][y] = id;
			removeJokers();
			advanceTurn();
			resetJokers();
			return true;
		}
		else {
			return false;
		}
	}
	
	private void advanceTurn() {
		if (!doubleMoveJokerUsed) {
			currentTurn = (currentTurn + 1) % players.size();
		}
		else {
			doubleMoveJokerUsed = false;
		}
		
		server.updatePlayers();
	}
	
	public Boolean isValidMove(int row, int col, int colorId) {
		// check cell within bounds
		if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
			return false;
		}
		
		// check for empty cell
		if (!replaceJokerUsed && board[row][col] != 0 || board[row][col] == colorId) {
			return false;
		}
		
		// check neighboring cells
		if (!freedomJokerUsed) {
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = col - 1; j <= col + 1; j++) {
					if (i >= 0 && i < numRows && j >= 0 && j < numCols) {
						if (board[i][j] == colorId) {
							return true;
						}
					}
				}
			}
		}
		else {
			return true;
		}
		
		return false;
	}
	
	public void resetJokers() {
		doubleMoveJokerUsed = false;
		replaceJokerUsed = false;
		freedomJokerUsed = false;
	}
	
	private void removeJokers() {
		ClientHandler handler;
		try {
			handler = server.getHandlerById(players.get(currentTurn).getId());

			if (this.freedomJokerUsed) {
				handler.setFreedomJoker(false);
			}
			if (this.replaceJokerUsed) {
				handler.setReplaceJoker(false);
			}
			if (this.doubleMoveJokerUsed) {
				handler.setDoubleMoveJoker(false);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void pass(ClientHandler player) {
		resetJokers();
		players.remove(currentTurn);

		if (currentTurn>=players.size()) {
			currentTurn = 0;
		}
		playerPassOrder.add(player);
		System.out.println(playerPassOrder);
	}
	
	public boolean checkEnd() {
		System.out.println(players);
		return players.isEmpty();
	}
	
	private int calculateScore(int id) {
		int score = 0;
		for (int i = 0 ; i < numRows ; i ++) {
			for (int j = 0; j < numCols ; j++) {
				if(board[i][j]==id) score ++;
			}
		}
		return score;
	}
	
	public ClientHandler findWinner() {
		ClientHandler winner = null;
		int winnerScore = 0;
		for(ClientHandler player : playerPassOrder) {
			int score = calculateScore(player.getId());
			if(score >= winnerScore) {
				winnerScore = score;
				winner = player;
			}
		}

		return winner;
	}
}
