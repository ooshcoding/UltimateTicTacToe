import java.util.*;

public class SmallBoard {
    private char[][] grid = new char[3][3];
    private char winner = ' ';
    private boolean full = false;

    public SmallBoard() {
        for (char[] row : grid)
            Arrays.fill(row, ' ');
    }

    public boolean makeMove(int r, int c, char player) {
        if (grid[r][c] == ' ') {
            grid[r][c] = player;
            checkWinner();
            return true;
        }
        return false;
    }

    public void checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (grid[i][0] != ' ' && grid[i][0] == grid[i][1] && grid[i][1] == grid[i][2])
                winner = grid[i][0];
            if (grid[0][i] != ' ' && grid[0][i] == grid[1][i] && grid[1][i] == grid[2][i])
                winner = grid[0][i];
        }
        if (grid[0][0] != ' ' && grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2])
            winner = grid[0][0];
        if (grid[0][2] != ' ' && grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0])
            winner = grid[0][2];

        full = true;
        for (char[] row : grid)
            for (char cell : row)
                if (cell == ' ') full = false;
    }

    public boolean isPlayable() {
        return winner == ' ' && !full;
    }

    public char getCell(int r, int c) {
        return grid[r][c];
    }

    public char getWinner(){
        return winner;
    }
}