import java.util.ArrayList;
import java.util.Random;
import com.google.common.primitives.Floats;


public class MiniMax {
    private int x_wins;
    private int o_wins;
    private int depth;
    private float[][] ratio = new float[3][3];


    public MiniMax(){
        this.x_wins = 0;
        this.o_wins = 0;
        this.depth = 5; //looking five steps (branches) ahead in the tree
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                ratio[i][j] = 0;
            }
        }
    }

    public float[][] eval(BigBoard board){
        
        SmallBoard [][] boards = board.getBoards();

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                SmallBoard sb = boards[i][j];
                char [][] grid = sb.getGrid();
                //col1

                for (int c = 0; c < 3; c++){
                    if (grid[c][0] != ' ' || grid[c][1] != ' ' || grid[c][2] != ' '){
                        if (colWin(grid[c][0], 0, grid)!= ' '){ //if column has possible win
                            if (colWin(grid[c][0], 0, grid) == 'X'){ // if the column has possible win for X
                                x_wins++;
                                } 
                            else if (colWin(grid[c][0], 0, grid) == 'O'){ // if column has possible win for O
                                o_wins++;
                            }
                        }     
                    }
                    
                }
                
                
                for (int c = 0; c < 3; c++){
                    if (grid[c][0] != ' ' || grid[c][1] != ' ' || grid[c][2] != ' '){
                        if (colWin(grid[c][1], 1, grid)!= ' '){ //if column has possible win
                            if (colWin(grid[c][1], 1, grid) == 'X'){ // if the column has possible win for X
                                x_wins++;
                                } 
                            else if (colWin(grid[c][1], 1, grid) == 'O'){ // if column has possible win for O
                                o_wins++;
                            }
                        }     
                    }
                    
                }
                
                for (int c = 0; c < 3; c++){
                    if (grid[c][0] != ' ' || grid[c][1] != ' ' || grid[c][2] != ' '){
                        if (colWin(grid[c][2], 2, grid)!= ' '){ //if column has possible win
                            if (colWin(grid[c][2], 2, grid) == 'X'){ // if the column has possible win for X
                                x_wins++;
                                } 
                            else if (colWin(grid[c][2], 2, grid) == 'O'){ // if column has possible win for O
                                o_wins++;
                            }
                        }     
                    }
                    
                } // check for third column
                 
                for (int r = 0; r < 3; r++) {
                    if (grid[r][0] != ' ' || grid[r][1] != ' ' || grid[r][2] != ' '){ 
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
                    if (grid[r][0] != ' ' || grid[r][1] != ' ' || grid[r][2] != ' '){ 
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
                    if (grid[r][0] != ' ' || grid[r][1] != ' ' || grid[r][2] != ' '){ 
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
                if (x_wins == 0){
                    if (o_wins == 0){
                        ratio[i][j] = (float)-100;
                    }
                    else{
                        ratio[i][j] = (float) 100;
                    }
                }
                else {
                    ratio[i][j] = (float) o_wins / x_wins;
                    System.out.println(o_wins + " " + x_wins);
                }
                if (sb.isEmpty()){
                    ratio[i][j] = (float) 0; 
                    break; //neutral position, but change to match halfway of top and bottom x ratio
                }
                if (sb.getWinner() == 'X'){
                    ratio[i][j] = (float) 100;
                } else if (sb.getWinner() == 'O'){
                    ratio[i][j] = (float) -100;
                }
                else if (sb.isPlayable() == false && sb.getWinner() == ' '){
                    ratio[i][j] = (float) 0; //if the board is full and no winner, neutral position
                }

                o_wins = 0;
                x_wins = 0;
                
            }
        }
        return ratio;
 }
        
    public float finalRatio(float[][] ratios){
        float finalRatio = -999;
        float ratio1 = Math.max(finalRatio, rowSum(ratios, 0));
        float ratio2 = Math.max(rowSum(ratios, 1), rowSum(ratios, 2));
        float ratio3 = Math.max(colSum(ratios, 0), colSum(ratios, 1));
        float ratio4 = Math.max(colSum(ratios, 2), diagSum1(ratios));
        float ratio5 = Math.max(diagSum2(ratios), ratio1);
        return Math.max(ratio5, Math.max(ratio2, Math.max(ratio3, ratio4)));
    }

    public float diagSum1(float[][]ratios){return ratios[0][0] + ratios[1][1] + ratios[2][2];}
    public float diagSum2(float[][]ratios){return ratios[0][2] + ratios[1][1] + ratios[2][0];}
    public float rowSum(float[][]ratios, int row){return ratios[row][0] + ratios[row][1] + ratios[row][2];}
    public float colSum(float[][]ratios, int col){return ratios[0][col] + ratios[1][col] + ratios[2][col];}

    public char colWin(char val, int col, char[][] grid){
        if (val == 'X'){
            if (grid[0][col] != 'O' && grid[1][col] != 'O' && grid[2][col] != 'O'){
                if (grid[0][col] != ' ' || grid[1][col] != ' ' || grid[2][col] != ' '){
                    return 'X';
                }
            }

        }
//DEBUG: column up the middle not checking.
        else if (val == 'O'){
            if (grid[0][col] != 'X' && grid[1][col] != 'X' && grid[2][col] != 'X'){
                if (grid[0][col] != ' ' || grid[1][col] != ' ' || grid[2][col] != ' '){
                    return 'O';
                }
            }
        }
        return ' ';
    }

    public char rowWin(char val, int row, char[][] grid){
        if (val == 'X'){
            if (grid[row][0] != 'O' && grid[row][1] != 'O' && grid[row][2] != 'O'){
                if (grid[row][0] != ' ' || grid[row][1] != ' ' || grid[row][2] != ' '){
                    return 'X';
                }
            }

        } else if (val == 'O'){
            if (grid[row][0] != 'X' && grid[row][1] != 'X' && grid[row][2] != 'X'){
                if (grid[row][0] != ' ' || grid[row][1] != ' ' || grid[row][2] != ' '){
                    return 'O';
                }
            }
        }
        return ' ';

    }

    
    public int[] miniMax(BigBoard board, char player, boolean isMaximizing){
        ArrayList<int[]> legalMoves = board.legalMoves();
        int [] bestMove = new int[4];
        if (isMaximizing){
            float highestVal = -999;
            for (int[] move : legalMoves) {
                SmallBoard sb = board.getBoards()[move[0]][move[1]];
                char[][] grid = sb.getGrid();
                grid[move[2]][move[3]] = player; 
                highestVal = Math.max(highestVal, finalRatio(eval(board))); //max to maximize the value score,
                int [] bestMove1 = {move[0], move[1], move[2], move[3]};
                bestMove = bestMove1;
                grid[move[2]][move[3]] = ' '; // Reset the move
            }
            return bestMove;
        }
        else{
            float lowestVal = 999;
            for (int[] move : legalMoves) {
                SmallBoard sb = board.getBoards()[move[0]][move[1]];
                char[][] grid = sb.getGrid();
                grid[move[2]][move[3]] = player; 
                lowestVal = Math.min(lowestVal, finalRatio(eval(board))); //min to minimize the value score,
                int [] bestMove1 = {move[0], move[1], move[2], move[3]};
                bestMove = bestMove1;
                grid[move[2]][move[3]] = ' '; // Reset the move
            }
            return bestMove;
        }
        
    }
    

    public int miniMax(BigBoard board, boolean isMaximizing){
        

        ArrayList<int[]> legalMoves = board.legalMoves();
        if (legalMoves.isEmpty()) {
            return 0; // Draw
        }

        int bestValue;
        if (isMaximizing) {
            bestValue = Integer.MIN_VALUE;
            for (int[] move : legalMoves) {
                board.makeMove(move[0], move[1], move[2], move[3], 'X');
                bestValue = Math.max(bestValue, miniMax(board, false));
                board.undoMove(move[0], move[1], move[2], move[3]); // Undo the move
            }
        } else {
            bestValue = Integer.MAX_VALUE;
            for (int[] move : legalMoves) {
                board.makeMove(move[0], move[1], move[2], move[3], 'O');
                bestValue = Math.min(bestValue, miniMax(board, true));
                board.undoMove(move[0], move[1], move[2], move[3]); // Undo the move
            }
        }
        return bestValue;
    }

}
