import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Arrays;

public class MiniMax {
    private int x_wins;
    private int o_wins;
     // Original depth for the minimax algorithm
    private float[][] evaluation = new float[3][3];
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
                
                evaluation[i][j] = map.retrieve(sb.toInt());
            }
        }
        return evaluation;
 }
        
    public float finalEval(float[][] evals){

        float finalEval = -999;
        float finalEval2 = -999;
        float eval1 = Math.max(finalEval, rowSum(evals, 0));
        float eval2 = Math.max(rowSum(evals, 1), rowSum(evals, 2));
        float eval3 = Math.max(colSum(evals, 0), colSum(evals, 1));
        float eval4 = Math.max(colSum(evals, 2), diagSum1(evals));
        float eval5 = Math.max(diagSum2(evals), eval1);
        float eval6 = Math.max(finalEval2, rowSum2(evals, 0));

        float eval7 = Math.max(rowSum2(evals, 1), rowSum2(evals, 2));
        float eval8 = Math.max(colSum2(evals, 0), colSum2(evals, 1));
        float eval9 = Math.max(colSum2(evals, 2), diagSumA(evals));
        float eval10 = Math.max(diagSumB(evals), eval6);
        
        
        return(Math.max(eval5, Math.max(eval2, Math.max(eval3, eval4)))-(Math.max(eval10, Math.max(eval7, Math.max(eval8, eval9)))));
        
        
    }
// player X and player O evals
    public float diagSum1(float[][]evals){return evals[0][0] + evals[1][1] + evals[2][2];}
    public float diagSumA(float[][]evals){return -evals[0][0] - evals[1][1] - evals[2][2];} 
    public float diagSumB(float[][]evals){return -evals[0][2] - evals[1][1] - evals[2][0];}
    public float diagSum2(float[][]evals){return evals[0][2] + evals[1][1] + evals[2][0];}
    public float rowSum(float[][]evals, int row){return evals[row][0] + evals[row][1] + evals[row][2];}
    public float rowSum2(float[][]evals, int row){return -evals[row][0] - evals[row][1] - evals[row][2];}
    public float colSum2(float[][]evals, int col){return -evals[0][col] - evals[1][col] - evals[2][col];}
    public float colSum(float[][]evals, int col){return evals[0][col] + evals[1][col] + evals[2][col];}

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

    
    public float miniMax(BigBoard board, int depth, boolean isMaximizing, int originalDepth, float alpha, float beta, TranspositionTable map){
        ArrayList<int[]> legalMoves = board.getAvailableMoves();
        board.checkOverallWinner();
        if (board.getWinner() == 'X'){
            return -Float.MAX_VALUE; //if X wins, return a very low value
        }
        if (board.getWinner() == 'O'){
            return Float.MAX_VALUE; //if O wins, return a very high value
        }
        if (depth == 0 || legalMoves.size() == 0) {
            return finalEval(eval(board, map));}
        if (isMaximizing){
            float highestVal = -Float.MAX_VALUE;
            for (int[] move : legalMoves) {
                BigBoard boardCopy = deepCopy(board); 
                boardCopy.makeMove(move[0], move[1], move[2], move[3], 'O');
                float value = miniMax(boardCopy, depth - 1, false, originalDepth, alpha, beta, map); //make highestval and lowestval compare with every option.
                if (highestVal < value){
                    if (depth == originalDepth) {
                        bestMove = move.clone();}
                    highestVal = value;
                }
                alpha = Math.max(alpha, value);
                if (beta <= alpha) {
                      break; 
                }
            }
            return highestVal;
        }
        else{
            float lowestVal = Float.MAX_VALUE;
            for (int[] move : legalMoves) {
                BigBoard boardCopy = deepCopy(board); 
                boardCopy.makeMove(move[0], move[1], move[2], move[3], 'X'); 
                float value = miniMax(boardCopy, depth - 1, true, originalDepth, alpha, beta, map);//, map);
                if (lowestVal > value){
                    lowestVal = value;
                }
                beta = Math.min(beta, value);  
                if (beta <= alpha) {
                   break; 
                }
            }
            return lowestVal;
        }

    }


    public int[] findBestMove(BigBoard board, int depth, float alpha, float beta, TranspositionTable map) {
        int[] bestMoveFound = null;
        //System.out.println(depth + "depth!");
        miniMax(board, depth, true, depth, alpha, beta, map);//, map);
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
