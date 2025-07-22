import java.util.ArrayList;
import java.util.Arrays;

public class MiniMax {
    private int x_wins;
    private int o_wins;
     // Original depth for the minimax algorithm
    private float[][] ratio = new float[3][3];
    private int[] bestMove = new int[2]; // Store the best move found
    //private static float highestVal = -99999999; // Initialize to a very low value
    //private static float lowestVal = 9999999;
    public MiniMax() {
        this.x_wins = 0;
        this.o_wins = 0;
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
                if (sb.isEmpty()){
                    //Random rand = new Random();
                    ratio[i][j] = 0;//rand.nextFloat(); 
                     //neutral position, but change to match halfway of top and bottom x ratio
                }
                else if (sb.getWinner() == 'X'){ //if board is already WON
                    ratio[i][j] = (float) -6;
                    //board.display();
                } else if (sb.getWinner() == 'O'){
                    ratio[i][j] = (float) 6;
                    //board.display();
                }
                else if (sb.isPlayable() == false && sb.getWinner() == ' '){ // board is tied.
                    ratio[i][j] = (float) -2000; //if the board is full and no winner, neutral position
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
                

                
                if (!sb.isEmpty()){
                    ratio[i][j] = (float) o_wins - x_wins;
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
        //System.out.println("Ratio1: " + ratio1);
        float ratio2 = Math.max(rowSum(ratios, 1), rowSum(ratios, 2));
        //System.out.println("Ratio2: " + ratio2);
        float ratio3 = Math.max(colSum(ratios, 0), colSum(ratios, 1));
        //System.out.println("Ratio3: " + ratio3);
        float ratio4 = Math.max(colSum(ratios, 2), diagSum1(ratios));
        //System.out.println("Ratio4: " + ratio4);
        float ratio5 = Math.max(diagSum2(ratios), ratio1);
        //System.out.println("Ratio5: " + ratio5);

        float ratio6 = Math.max(finalRatio2, rowSum2(ratios, 0));
        //System.out.println(rowSum2(ratios, 0));
        //System.out.println("Ratio6: " + ratio6);
        float ratio7 = Math.max(rowSum2(ratios, 1), rowSum2(ratios, 2));
        //System.out.println("Ratio7: " + ratio7);
        float ratio8 = Math.max(colSum2(ratios, 0), colSum2(ratios, 1));
        //System.out.println("Ratio8: " + ratio8);
        float ratio9 = Math.max(colSum2(ratios, 2), diagSumA(ratios));
        //System.out.println("Ratio9: " + ratio9);
        float ratio10 = Math.max(diagSumB(ratios), ratio6);
        //System.out.println("Ratio10: " + ratio10);
        System.out.println(Math.max(ratio5, Math.max(ratio2, Math.max(ratio3, ratio4)))+ "-" + (Math.max(ratio10, Math.max(ratio7, Math.max(ratio8, ratio9)))));
        return(Math.max(ratio5, Math.max(ratio2, Math.max(ratio3, ratio4)))-(Math.max(ratio10, Math.max(ratio7, Math.max(ratio8, ratio9)))));
        
        
    }
// player X and player O evals
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

    
    public float miniMax(BigBoard board, int depth, boolean isMaximizing, int originalDepth){//, float alpha, float beta){
        ArrayList<int[]> legalMoves = board.getAvailableMoves();
        board.checkOverallWinner();
        if (depth == 0 || board.getWinner() != ' ' || legalMoves.size() == 0) {
            return finalRatio(eval(board)); //if depth is 0, return the evaluation of the board
        } //reset highestVal for next call

        if (isMaximizing){
            float highestVal = -Float.MAX_VALUE;
            float temp;
            for (int[] move : legalMoves) {
                BigBoard boardCopy = deepCopy(board);
                boardCopy.makeMove(move[0], move[1], move[2], move[3], 'O');
                //System.out.println("Maximizing move: " + move[0] + ", " + move[1] + ", " + move[2] + ", " + move[3]);
                float value = miniMax(boardCopy, depth - 1, false, originalDepth);//, alpha, beta);
                //highestVal = Math.max(highestVal, value); 
               // System.out.println("Move: " + move[0] + ", " + move[1] + ", " + move[2] + ", " + move[3]);
               // System.out.println("value: " + value + " highest value: " + highestVal);

                if (highestVal < value){
                    if (depth == originalDepth){
                        bestMove = move;
                    }
                    highestVal = value;
                    //System.out.println("Best move found: " + bestMove[0] + ", " + bestMove[1] + ", " + bestMove[2] + ", " + bestMove[3]);
                    //System.out.println("Highest value: " + highestVal);
                    
                }
                //max to maximize the value score,
                //alpha = Math.max(alpha, value);
                //if (beta <= alpha) {
                  //  break; // Beta is the lowest other branch move, the non maximizing always chooses lowest
                System.out.println("Trying Move: " + Arrays.toString(move) + " -> Eval: " + value);
            }
            System.out.println("Returning Highest value: " + highestVal);
            temp = highestVal;
            highestVal = -Float.MAX_VALUE;
            return temp;
        }
        else{
            float lowestVal = 99999999;
            for (int[] move : legalMoves) {
                BigBoard boardCopy = deepCopy(board); 
                boardCopy.makeMove(move[0], move[1], move[2], move[3], 'X'); //if player is not maximizing, X plays
                //System.out.println("Maximizing move: " + move[0] + ", " + move[1] + ", " + move[2] + ", " + move[3]);

                
                float value = miniMax(boardCopy, depth - 1, true, originalDepth);//, alpha, beta);
                if (lowestVal > value){
                    lowestVal = value;//update best move to the current move
                }
                //i don't know if we need to add a move tracker for the lowest value


                //beta = Math.min(beta, value);  
                //if (beta <= alpha) {
                 //   break; 
               // }
                //min to minimize the value score,
                //int [] bestMove1 = {move[0], move[1], move[2], move[3]};
                //bestMove = bestMove1;
                
            }
            return lowestVal;
        }
    }

    public int[] findBestMove(BigBoard board, int depth) {
        int[] bestMoveFound = null;
        miniMax(board, depth, true, depth);
        System.out.println("Final bestMove " + Arrays.toString(bestMove));
        bestMoveFound = bestMove;
        bestMove = null;//, -99999, 99999);
        //we need to find a way to store best move and acutally have it be best move of all tries.

        return bestMoveFound;
    }

       // In your SmallBoard.java file
    public SmallBoard deepCopy(SmallBoard sb) {
        SmallBoard newBoard = new SmallBoard();
        char[][] oldGrid = sb.getGrid();
        char[][] newGrid = newBoard.getGrid();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newGrid[i][j] = oldGrid[i][j];
            }
        }
        newBoard.setWinner(sb.getWinner()); // make sure winner state is copied
        newBoard.setPlayable(sb.isPlayable());
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
