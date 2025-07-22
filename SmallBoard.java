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

        if (grid[r][c] == ' ') { //if cell empty
            grid[r][c] = player; //set to player
            checkWinner(); //check winner
            return true;
        }
        return false;
    }
    public SmallBoard deepCopy(SmallBoard sb) {
    SmallBoard newBoard = new SmallBoard();
    char[][] oldGrid = sb.getGrid();
    char[][] newGrid = newBoard.getGrid();

    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            newGrid[i][j] = oldGrid[i][j];
        }
    }

    // Copy winner and playable status
    newBoard.setWinner(sb.getWinner());
    newBoard.setPlayable(sb.isPlayable());

    return newBoard;
}

    public boolean isEmpty(){
        for (char[] row : grid) {
            for (char cell : row) {
                if (cell != ' ') return false;
            }
        }
        return true;
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
        checkWinner();
        return winner == ' ' && !full;
    }

    public char getCell(int r, int c) {
        return grid[r][c];
    }

    public char getWinner(){
        return winner;
    }
    public char[][] getGrid(){
        return grid;
    }
    public void setWinner(char winner) { this.winner = winner; }
    public void setPlayable(boolean playable) { this.playable = playable; }


    public void setGrid(char[][] newGrid){
        grid = newGrid;
    }


    public void setWinner(char newWinner){
        winner = newWinner;
    }
    public boolean isFull() {
        return full;
    }
    
    public void setFull(boolean full) {
        this.full = full;
    }
    public void fill(char player) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                grid[r][c] = player;
            }
        }
    }
    /*public ArrayList<int[]> getSmallBoardMoves() {
        ArrayList<int[]> moves = new ArrayList<>();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (grid[r][c] == ' ') {
                    moves.add(new int[]{r, c});
                }
            }
        }
        return moves;
    }*/
}