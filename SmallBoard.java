import java.util.*;

public class SmallBoard {
    private char[][] grid = new char[3][3];
    private char winner = ' ';
    private boolean full = false;
    private int count = 0;
    public SmallBoard() {
        for (char[] row : grid)
            Arrays.fill(row, ' ');
    }

    public String toString(){
        String ret = "";
        for (int i =0; i < 3; i++){
            for (int j = 0; j < 3; j++)
                ret+=grid[i][j];
        }
        return ret;
    }

    public Float eval(SmallBoard Sb){
        int x_wins = 0;
        int o_wins = 0;
        Float ret = 0f;
        Sb.checkWinner();
        if (Sb.isEmpty()){
            //Random rand = new Random();
            ret = 0f;//rand.nextFloat(); 
             //neutral position, but change to match halfway of top and bottom x ratio
        }
        else if (Sb.getWinner() == 'X'){ 
            //if board is already WON
            ret = (float) -9;
            //board.display();
        } else if (Sb.getWinner() == 'O'){
            ret = (float) 9;
            //board.display();
        }
        else if (Sb.isPlayable() == false && Sb.getWinner() == ' '){ // board is tied.
            ret = (float) -6; //if the board is full and no winner, neutral position
        }

        for (int c = 0; c < 3; c++){ //ALL THIS IS FIRST COLUMN
            if (grid[0][c] != ' '){ //grid row c, col 0
                if (colWin(grid[0][c], c, grid)!= ' '){ //if column has possible win
                    if (colWin(grid[0][c], c, grid) == 'X'){ // if the column has possible win for X
                        x_wins++;
                        } 
                    else if (colWin(grid[0][c], c, grid) == 'O'){ // if column has possible win for O
                        o_wins++;
                    }
                }     
            }
            
        }
        
        
        for (int c = 0; c < 3; c++){
            if (grid[1][c] != ' '){
                if (colWin(grid[1][c], c, grid)!= ' '){ //if column has possible win
                    if (colWin(grid[1][c], c, grid) == 'X'){ // if the column has possible win for X
                        x_wins++;
                        } 
                    else if (colWin(grid[1][c], c, grid) == 'O'){ // if column has possible win for O
                        o_wins++;
                    }
                }     
            }
            
        }
        
        for (int c = 0; c < 3; c++){
            if (grid[2][c] != ' '){
                if (colWin(grid[2][c], c, grid)!= ' '){ //if column has possible win
                    if (colWin(grid[2][c], c, grid) == 'X'){ // if the column has possible win for X
                        x_wins++;
                        } 
                    else if (colWin(grid[2][c], c, grid) == 'O'){ // if column has possible win for O
                        o_wins++;
                    }
                }     
            }
            
        } // check for third column
         
        for (int r = 0; r < 3; r++) {
            if (grid[r][0] != ' '){ 
                if (rowWin(grid[r][0], r, grid) != ' ') {
                    if (rowWin(grid[r][0], r, grid) == 'X') {
                        x_wins++;
                    } else if (rowWin(grid[r][0], r, grid) == 'O') {
                        o_wins++;
                    }
                }
            }
        }

        for (int r = 0; r < 3; r++) {
            if (grid[r][1] != ' '){ 
                if (rowWin(grid[r][1], r, grid) != ' ') {
                    if (rowWin(grid[r][1], r, grid) == 'X') {
                        x_wins++;
                    } else if (rowWin(grid[r][1], r, grid) == 'O') {
                        o_wins++;
                    }
                }
            }
        }

        for (int r = 0; r < 3; r++) {
            if (grid[r][2] != ' '){ 
                if (rowWin(grid[r][2], r, grid) != ' ') {
                    if (rowWin(grid[r][2], r, grid) == 'X') {
                        x_wins++;
                    } else if (rowWin(grid[r][2], r, grid) == 'O') {
                        o_wins++;
                    }
                }
            }
        }
        for (int d = 0; d < 3; d++){ // first diagonal top left to bottom right
            if (grid[d][d] != ' '){
                if (grid[d][d] == 'X'){
                    if (grid[0][0] != 'O' && grid[1][1] != 'O' && grid[2][2] != 'O'){
                        x_wins++;
                    } 
                }
                if (grid[d][d] == 'O'){
                    if (grid[0][0] != 'X' && grid[1][1] != 'X' && grid[2][2] != 'X'){
                        o_wins++;
                    } 
                }
                
            }
        }
        for (int d = 0; d < 3; d++){
            if (grid[d][2-d] != ' '){
                if (grid[d][2-d] == 'X'){
                    if (grid[0][2] != 'O' && grid[1][1] != 'O' && grid[2][0] != 'O'){
                        x_wins++;
                    } 
                }
                if (grid[d][2-d] == 'O'){
                    if (grid[0][2] != 'X' && grid[1][1] != 'X' && grid[2][0] != 'X'){
                        o_wins++;
                    } 
                }
                
            }
        }
        
        
        
        if (!Sb.isEmpty() && Sb.getWinner() == ' ' && Sb.isPlayable() == true){ //if the board is not empty, not won, and playable
            ret = (float) o_wins - x_wins;
        }
    
       
        o_wins = 0;
        x_wins = 0;
        
        return ret;
    }


    public float diagSum1(float[][]ratios){return ratios[0][0] + ratios[1][1] + ratios[2][2];}
    public float diagSumA(float[][]ratios){return -ratios[0][0] - ratios[1][1] - ratios[2][2];} 
    public float diagSumB(float[][]ratios){return -ratios[0][2] - ratios[1][1] - ratios[2][0];}
    public float diagSum2(float[][]ratios){return ratios[0][2] + ratios[1][1] + ratios[2][0];}
    public float rowSum(float[][]ratios, int row){return ratios[row][0] + ratios[row][1] + ratios[row][2];}
    public float rowSum2(float[][]ratios, int row){return -ratios[row][0] - ratios[row][1] - ratios[row][2];}
    public float colSum2(float[][]ratios, int col){return -ratios[0][col] - ratios[1][col] - ratios[2][col];}
    public float colSum(float[][]ratios, int col){return ratios[0][col] + ratios[1][col] + ratios[2][col];}

    public char colWin(char val, int col, char[][] grid){//really checking of a row is possible won
        if (val == 'X'){
            if (grid[0][col] != 'O' && grid[1][col] != 'O' && grid[2][col] != 'O'){
                return 'X';
            }

        }
//DEBUG: column up the middle not checking.
        else if (val == 'O'){
            if (grid[0][col] != 'X' && grid[1][col] != 'X' && grid[2][col] != 'X'){
                return 'O';
            }
        }
        return ' ';
    }

    public char rowWin(char val, int row, char[][] grid){
        if (val == 'X'){
            if (grid[row][0] != 'O' && grid[row][1] != 'O' && grid[row][2] != 'O'){
                return 'X';
            }

        } else if (val == 'O'){
            if (grid[row][0] != 'X' && grid[row][1] != 'X' && grid[row][2] != 'X'){
                return 'O';
            }
        }
        return ' ';

    }
    public void printSmallBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean makeMove(int r, int c, char player) {

        if (grid[r][c] == ' ') { //if cell empty
            grid[r][c] = player; //set to player
            checkWinner(); //check winner
            return true;
        }
        return false;
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
        winner = ' ';
        /*System.out.println("GRID----------------------");
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                System.out.print(grid[i][j]);
            }
            System.out.println();
        } */
        for (int i = 0; i < 3; i++) {
            if (grid[i][0] != ' ' && grid[i][0]==grid[i][1] && grid[i][1] == grid[i][2]){
                winner = grid[i][0];
                return;
                }
                
                
            if (grid[0][i] != ' ' && grid[0][i] == grid[1][i] && grid[1][i] == grid[2][i]){
                winner = grid[0][i];
                return;
            }
                
                
                
        }
                
        if (grid[0][0] != ' ' && grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]){
            winner = grid[0][0];
            return;}
        if (grid[0][2] != ' ' && grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0]){
            winner = grid[0][2];
            return;}

        full = true;
        for (char[] row : grid){
            for (char cell : row)
                if (cell == ' ') {full = false; break;}
                
            if (!full) break;}// if any cell is empty, not full
    }

    public boolean isPlayable() {
        checkWinner();
        return winner == ' ' && !full;
    }

    public char getCell(int r, int c) {
        return grid[r][c];
    }

    public char getWinner(){
        checkWinner();
        return winner;
    }
    public char[][] getGrid(){
        return grid;
    }

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