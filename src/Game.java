import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
	private int[][] board;
	private int numRows = 6;
	private int numCols = 10;
	private List<ClientHandler> players;
	
	public Game(int numRows, int numCols, List<ClientHandler> players) {
		this.numRows = numRows;
		this.numCols = numCols;
		this.board = new int[numRows][numCols];
		this.players = players;
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
}
