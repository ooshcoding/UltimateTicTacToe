import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Arrays;

public class MiniMax {
    private int x_wins;
    private int o_wins;
     // Original depth for the minimax algorithm
    private float[][] ratio = new float[3][3];
    private int[] bestMove = new int[4]; // Store the best move found
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
    
    
    public float[][] eval(BigBoard board, TranspositionTable map){
        
        SmallBoard [][] boards = board.getBoards();

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                SmallBoard sb = boards[i][j];
                
                ratio[i][j] = map.retrieve(sb.toInt());
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
        //System.out.println(Math.max(ratio5, Math.max(ratio2, Math.max(ratio3, ratio4)))+ "-" + (Math.max(ratio10, Math.max(ratio7, Math.max(ratio8, ratio9)))));
        
        
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

    
    public float miniMax(BigBoard board, int depth, boolean isMaximizing, int originalDepth, TranspositionTable map){
        ArrayList<int[]> legalMoves = board.getAvailableMoves();
        board.checkOverallWinner();
        if (board.getWinner() == 'X'){
            return -Float.MAX_VALUE; //if X wins, return a very low value
        }
        if (board.getWinner() == 'O'){
            return Float.MAX_VALUE; //if O wins, return a very high value
        }
        if (depth == 0 || legalMoves.size() == 0) {//2nd 2 conditions will not happen
            //System.out.println("zero depth reached, returning evaluation: " + finalRatio(eval(board)));
            return finalRatio(eval(board, map));//, map)); //if depth is 0, return the evaluation of the board
        } //reset highestVal for nex0 t call

        if (isMaximizing){
            float highestVal = -Float.MAX_VALUE;
            //for (int i=0;i<legalMoves.size(); i++)
                //System.out.println(Arrays.toString(legalMoves.get(i)) + "depth: " + depth);
            for (int[] move : legalMoves) {
                BigBoard boardCopy = deepCopy(board); 
                boardCopy.makeMove(move[0], move[1], move[2], move[3], 'O');
                //System.out.println("Making Maximizing move: " + move[0] + ", " + move[1] + ", " + move[2] + ", " + move[3] + "depth: " + depth);
                float value = miniMax(boardCopy, depth - 1, false, originalDepth, map); //make highestval and lowestval compare with every option.
                //highestVal = Math.max(highestVal, value); 
               // System.out.println("Move: " + move[0] + ", " + move[1] + ", " + move[2] + ", " + move[3]);
               // System.out.println("value: " + value + " highest value: " + highestVal);

                if (highestVal < value){
                     //if we are at the original depth, update the best move
                    if (depth == originalDepth) {
                        bestMove = move.clone();}
                        //System.out.println("Best move found: " + bestMove[0] + ", " + bestMove[1] + ", " + bestMove[2] + ", " + bestMove[3] + "Highest Value: " + highestVal);}
                    highestVal = value;
                    
                    //System.out.println("Highest value: " + highestVal);
                    
                }
                //max to maximize the value score,
                /*alpha = Math.max(alpha, value);
                if (beta <= alpha) {
                      break; // Beta is the lowest other branch move, the non maximizing always chooses lowest
                }*/
            }
            //System.out.println("Returning Highest value: " + highestVal + "depth: " + depth);
            return highestVal;
        }
        else{
            float lowestVal = Float.MAX_VALUE;
            for (int[] move : legalMoves) {
                BigBoard boardCopy = deepCopy(board); 
                boardCopy.makeMove(move[0], move[1], move[2], move[3], 'X'); //if player is not maximizing, X plays
                //System.out.println("Minimizing move: " + move[0] + ", " + move[1] + ", " + move[2] + ", " + move[3] + "depth: " + depth);

                
                float value = miniMax(boardCopy, depth - 1, true, originalDepth, map);//, map);
                if (lowestVal > value){
                    lowestVal = value;//update best move to the current move
                }
                //i don't know if we need to add a move tracker for the lowest value


                /*beta = Math.min(beta, value);  
                if (beta <= alpha) {
                   break; 
                }*/
            }
            //System.out.println("Lowest value: " + lowestVal + "depth: " + depth);
            return lowestVal;
        }
        

     


    }


    public int[] findBestMove(BigBoard board, int depth, TranspositionTable map) {
        int[] bestMoveFound = null;
        //System.out.println(depth + "depth!");
        miniMax(board, depth, true, depth, map);//, map);
        //System.out.println("Final bestMove " + Arrays.toString(bestMove));
        bestMoveFound = bestMove;
        bestMove = null;//, -99999, 99999);
        //we need to find a way to store best move and acutally have it be best move of all tries.

        return bestMoveFound;
    }

       // In your SmallBoard.java file
    public SmallBoard deepCopy(SmallBoard sb) {
        SmallBoard newBoard = new SmallBoard(); // Assuming a default constructor
        char[][] oldGrid = sb.getGrid();
        char[][] newGrid = newBoard.getGrid();
        newBoard.setWinner(sb.getWinner());
        newBoard.setFull(sb.isFull());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newGrid[i][j] = oldGrid[i][j];
            }
        }

    
        return newBoard;
    }

    public BigBoard deepCopy(BigBoard board) {
        BigBoard newBigBoard = new BigBoard(); // Assuming a default constructor
        newBigBoard.setNextBoards(board.getNextBoardRow(), board.getNextBoardCol());
        newBigBoard.setOverallWinner(board.getWinner());
        
        // For each small board in the original, create a deep copy for the new one
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newBigBoard.getBoards()[i][j] = deepCopy(board.getBoards()[i][j]);
            }
        }
        return newBigBoard;
    }

}
