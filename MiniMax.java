import java.util.ArrayList;
import java.util.Random;


public class MiniMax {
    private int x_wins;
    private int o_wins;
    private float[][] ratio = new float[3][3];
    private int [] bestMove = new int[4];

    public MiniMax(){
        this.x_wins = 0;
        this.o_wins = 0;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                ratio[i][j] = 0;
            }
        }
    }

    public int[] getBestMove() {
        return bestMove;
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
                        ratio[i][j] = (float) -30;
                    }
                    else{
                        ratio[i][j] = (float) 30;
                    }
                }
                else {
                    ratio[i][j] = (float) o_wins - x_wins;
                }
                if (sb.isEmpty()){
                    ratio[i][j] = (float) 0; 
                    break; //neutral position, but change to match halfway of top and bottom x ratio
                }
                if (sb.getWinner() == 'X'){
                    ratio[i][j] = (float) 30;
                } else if (sb.getWinner() == 'O'){
                    ratio[i][j] = (float) -30;
                }
                else if (sb.isPlayable() == false && sb.getWinner() == ' '){
                    ratio[i][j] = (float) -15; //if the board is full and no winner, neutral position
                }

                o_wins = 0;
                x_wins = 0;
                
            }
        }
        return ratio;
 }
        
    public float finalRatio(float[][] ratios){

        
        
        float finalRatio = -999;
        float finalRatio2 = -999;
        float ratio1 = Math.max(finalRatio, rowSum(ratios, 0));
        float ratio2 = Math.max(rowSum(ratios, 1), rowSum(ratios, 2));
        float ratio3 = Math.max(colSum(ratios, 0), colSum(ratios, 1));
        float ratio4 = Math.max(colSum(ratios, 2), diagSum1(ratios));
        float ratio5 = Math.max(diagSum2(ratios), ratio1);

        float ratio6 = Math.max(finalRatio2, rowSum2(ratios, 0));
        float ratio7 = Math.max(rowSum2(ratios, 1), rowSum2(ratios, 2));
        float ratio8 = Math.max(colSum2(ratios, 0), colSum2(ratios, 1));
        float ratio9 = Math.max(colSum2(ratios, 2), diagSumA(ratios));
        float ratio10 = Math.max(diagSumB(ratios), ratio6);

        return(Math.max(ratio5, Math.max(ratio2, Math.max(ratio3, ratio4))/(Math.max(ratio10, Math.max(ratio7, Math.max(ratio8, ratio9))))));
        
        
    }
// player X and player O evals
    public float diagSum1(float[][]ratios){return ratios[0][0] + ratios[1][1] + ratios[2][2];}
    public float diagSumA(float[][]ratios){return 1/ratios[0][0] + 1/ratios[1][1] + 1/ratios[2][2];} 
    public float diagSumB(float[][]ratios){return 1/ratios[0][2] + 1/ratios[1][1] + 1/ratios[2][0];}
    public float diagSum2(float[][]ratios){return ratios[0][2] + ratios[1][1] + ratios[2][0];}
    public float rowSum(float[][]ratios, int row){return ratios[row][0] + ratios[row][1] + ratios[row][2];}
    public float rowSum2(float[][]ratios, int row){return 1/ratios[row][0] + 1/ratios[row][1] + 1/ratios[row][2];}
    public float colSum2(float[][]ratios, int col){return 1/ratios[0][col] + 1/ratios[1][col] + 1/ratios[2][col];}
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

    
    public float miniMax(BigBoard board, int depth, int player, boolean isMaximizing, float alpha, float beta){
        ArrayList<int[]> legalMoves = board.getAvailableMoves();
        board.checkOverallWinner();
        if (depth == 0 || board.getWinner() != ' ' || legalMoves.size() == 0) {
            return finalRatio(eval(board)); //if depth is 0, return the ratio of the board
        }
        if (isMaximizing){
            float highestVal = -99999999;
            for (int[] move : legalMoves) {
                BigBoard boardCopy = deepCopy(board); 
                if (player % 2 == 0){boardCopy.makeMove(move[0], move[1], move[2], move[3], 'X');} //if player is even, X plays
                else{boardCopy.makeMove(move[0], move[1], move[2], move[3], 'O');}
                
                float value = miniMax(boardCopy, depth - 1, player+1, false, alpha, beta);
                highestVal = Math.max(highestVal, value); //max to maximize the value score,
                alpha = Math.max(alpha, value);
                if (beta <= alpha) {
                    break; // Beta is the lowest other branch move, the non maximizing always chooses lowest

                }
            }
            return highestVal;
        }
        else{
            float lowestVal = 99999999;
            for (int[] move : legalMoves) {
                BigBoard boardCopy = deepCopy(board); 

                if (player % 2 == 0){boardCopy.makeMove(move[0], move[1], move[2], move[3], 'X');} //if player is even, X plays
                else{boardCopy.makeMove(move[0], move[1], move[2], move[3], 'O');}
                

                float value = miniMax(boardCopy, depth - 1, player+1, true, alpha, beta);

                lowestVal = Math.min(lowestVal, value);
                beta = Math.min(beta, value);  
                if (beta <= alpha) {
                    break; 
                }
                //min to minimize the value score,
                //int [] bestMove1 = {move[0], move[1], move[2], move[3]};
                //bestMove = bestMove1;
                
            }
            return lowestVal;
        }
        

     


    }


    public int[] findBestMove(BigBoard board, int depth) {
        float bestVal = -99999999;
        int[] bestMoveFound = null;
        ArrayList<int[]> legalMoves = board.getAvailableMoves();
    
        for (int[] move : legalMoves) {
            // Make a move on a COPY of the board
            BigBoard boardCopy = deepCopy(board); // Assuming you have a makeMove function
    

            float moveValue = miniMax(boardCopy, depth, 0, false, -99999, 99999);    
            if (moveValue > bestVal) {
                bestVal = moveValue;
                bestMoveFound = move;
            }
        }
        this.bestMove = bestMoveFound;
        return bestMoveFound;
    }

       // In your SmallBoard.java file
    public SmallBoard deepCopy(SmallBoard sb) {
        SmallBoard newBoard = new SmallBoard(); // Assuming a default constructor
        char[][] oldGrid = sb.getGrid();
        char[][] newGrid = newBoard.getGrid();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newGrid[i][j] = oldGrid[i][j];
            }
        }

    
        return newBoard;
    }

    public BigBoard deepCopy(BigBoard board) {
        BigBoard newBigBoard = new BigBoard(); // Assuming a default constructor
        
        // For each small board in the original, create a deep copy for the new one
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newBigBoard.getBoards()[i][j] = deepCopy(board.getBoards()[i][j]);
            }
        }
        return newBigBoard;
    }

}
