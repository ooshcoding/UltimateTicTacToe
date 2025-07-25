import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.List;
import java.util.Arrays;

public class ParallelMiniMax {
    private int x_wins;
    private int o_wins;
    private float[][] evaluations = new float[3][3];
    private int[] bestMove = new int[4];
    
    //only parallelizing the first level gives better performance
    // Going deeper creates too much overhead
    private static final int PARALLEL_DEPTH_LIMIT = 1; 
    
    public ParallelMiniMax() {
        this.x_wins = 0;
        this.o_wins = 0;
        // Initialize ratio matrix to zeros
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                evaluations[i][j] = 0;
            }
        }
    }

    // Board evaluation - same logic as before but with better error handling
    public float[][] eval(BigBoard board, TranspositionTable map){
        SmallBoard [][] boards = board.getBoards();
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                SmallBoard sb = boards[i][j];
                Float value = map.retrieve(sb.toInt());
                if (value != null) {
                    evaluations[i][j] = value;
                } else {
                    // DEBUG: This shouldn't happen often
                    System.out.println("Missing evaluation for board state: |" + sb.toString() + "|");
                }
            }
        }
        return evaluations;
    }
    
    // Calculate final board position value - this took me a while to get right
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
    

    // Main minimax with parallel processing - this is where the magic happens
    public float parallelMiniMax(BigBoard board, int depth, boolean isMaximizing, int originalDepth, 
                                float alpha, float beta, TranspositionTable map, int currentLevel) {
        
        ArrayList<int[]> legalMoves = board.getAvailableMoves();
        board.checkOverallWinner();
        
        // Base cases - game over or max depth reached
        if (board.getWinner() == 'X') return -Float.MAX_VALUE;
        if (board.getWinner() == 'O') return Float.MAX_VALUE;
        if (depth == 0 || legalMoves.size() == 0) {
            return finalEval(eval(board, map));
        }

        // Only use parallel processing for shallow depths to avoid thread overhead
        if (currentLevel < PARALLEL_DEPTH_LIMIT && legalMoves.size() > 1) {
            System.out.println("DEBUG: Using parallel search at level " + currentLevel + " with " + legalMoves.size() + " moves");
            return parallelSearch(board, depth, isMaximizing, originalDepth, alpha, beta, map, currentLevel);
        } else {
            // Fall back to normal minimax for deeper levels
            return sequentialMiniMax(board, depth, isMaximizing, originalDepth, alpha, beta, map, currentLevel);
        }
    }

    // Parallel search using thread pool - distributes move evaluation across multiple threads
    private float parallelSearch(BigBoard board, int depth, boolean isMaximizing, int originalDepth,
                               float alpha, float beta, TranspositionTable map, int currentLevel) {
        
        ArrayList<int[]> legalMoves = board.getAvailableMoves();
        
        // Don't create more threads than we have CPU cores or moves to evaluate
        int numThreads = Math.min(Runtime.getRuntime().availableProcessors(), legalMoves.size());
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        List<Future<MoveResult>> futures = new ArrayList<>();
        
        try {
            // Submit each move as a separate task
            for (int[] move : legalMoves) {
                Future<MoveResult> future = executor.submit(new MoveEvaluator(
                    deepCopy(board), move, depth, isMaximizing, originalDepth, 
                    alpha, beta, map, currentLevel
                ));
                futures.add(future);
            }
            
            // Wait for all evaluations to complete and find the best one
            float bestValue = isMaximizing ? -Float.MAX_VALUE : Float.MAX_VALUE;
            int[] currentBestMove = null;
            
            for (int i = 0; i < futures.size(); i++) {
                try {
                    MoveResult result = futures.get(i).get();
                    
                    if (isMaximizing) {
                        if (result.value > bestValue) {
                            bestValue = result.value;
                            currentBestMove = result.move;
                            // DEBUG output to track best move changes
                            //System.out.println("New best maximizing move: " + Arrays.toString(result.move) + " value: " + result.value);
                        }
                    } else {
                        if (result.value < bestValue) {
                            bestValue = result.value;
                            currentBestMove = result.move;
                        }
                    }
                    
                    // Store the best move if we're at the root level
                    if (depth == originalDepth && currentBestMove != null) {
                        bestMove = currentBestMove.clone();
                    }
                    
                } catch (Exception e) {
                    System.err.println("ERROR: Thread execution failed: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            return bestValue;
            
        } finally {
            executor.shutdown(); // Clean up thread pool
        }
    }

    // Regular minimax for deeper levels - avoids thread creation overhead
    private float sequentialMiniMax(BigBoard board, int depth, boolean isMaximizing, int originalDepth,
                                  float alpha, float beta, TranspositionTable map, int currentLevel) {
        
        ArrayList<int[]> legalMoves = board.getAvailableMoves();
        
        if (isMaximizing) {
            float highestVal = -Float.MAX_VALUE;
            
            for (int[] move : legalMoves) {
                BigBoard boardCopy = deepCopy(board);
                boardCopy.makeMove(move[0], move[1], move[2], move[3], 'O');
                
                float value = parallelMiniMax(boardCopy, depth - 1, false, originalDepth, alpha, beta, map, currentLevel + 1);
                
                if (highestVal < value) {
                    // Update best move if we're at root
                    if (depth == originalDepth) {
                        bestMove = move.clone();
                    }
                    highestVal = value;
                }
                
                alpha = Math.max(alpha, value);
                if (beta <= alpha) {
                    // Alpha-beta cutoff - no need to evaluate remaining moves
                    break; 
                }
            }
            return highestVal;
            
        } else {
            float lowestVal = Float.MAX_VALUE;
            
            for (int[] move : legalMoves) {
                BigBoard boardCopy = deepCopy(board);
                boardCopy.makeMove(move[0], move[1], move[2], move[3], 'X');
                
                float value = parallelMiniMax(boardCopy, depth - 1, true, originalDepth, alpha, beta, map, currentLevel + 1);
                
                if (lowestVal > value) {
                    lowestVal = value;
                }
                
                beta = Math.min(beta, value);
                if (beta <= alpha) break; // Pruning
            }
            return lowestVal;
        }
    }

    // Worker class for parallel move evaluation
    private class MoveEvaluator implements Callable<MoveResult> {
        private final BigBoard board;
        private final int[] move;
        private final int depth;
        private final boolean isMaximizing;
        private final int originalDepth;
        private final float alpha;
        private final float beta;
        private final TranspositionTable map;
        private final int currentLevel;
        
        public MoveEvaluator(BigBoard board, int[] move, int depth, boolean isMaximizing,
                           int originalDepth, float alpha, float beta, TranspositionTable map, int currentLevel) {
            this.board = board;
            this.move = move;
            this.depth = depth;
            this.isMaximizing = isMaximizing;
            this.originalDepth = originalDepth;
            this.alpha = alpha;
            this.beta = beta;
            this.map = map;
            this.currentLevel = currentLevel;
        }
        
        @Override
        public MoveResult call() {
            char player = isMaximizing ? 'O' : 'X';
            board.makeMove(move[0], move[1], move[2], move[3], player);
            
            float value = parallelMiniMax(board, depth - 1, !isMaximizing, originalDepth, 
                                        alpha, beta, map, currentLevel + 1);
            
            return new MoveResult(move, value);
        }
    }

    // Simple container for move results
    private static class MoveResult {
        final int[] move;
        final float value;
        
        public MoveResult(int[] move, float value) {
            this.move = move.clone();
            this.value = value;
        }
    }

    // Main entry point - call this to get the best move using parallel processing
    public int[] findBestMoveParallel(BigBoard board, int depth, TranspositionTable map) {
        bestMove = null;
        
        long startTime = System.nanoTime();
        parallelMiniMax(board, depth, true, depth, -Float.MAX_VALUE, Float.MAX_VALUE, map, 0);
        long endTime = System.nanoTime();
        
        System.out.println("Parallel minimax completed in " + ((endTime - startTime) / 1_000_000.0) + " ms");
        
        int[] result = bestMove;
        bestMove = null; // Clean up
        return result;
    }

    // Deep copy methods - necessary to avoid race conditions in parallel execution
    public SmallBoard deepCopy(SmallBoard sb) {
        SmallBoard newBoard = new SmallBoard();
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
        BigBoard newBigBoard = new BigBoard();
        newBigBoard.setNextBoards(board.getNextBoardRow(), board.getNextBoardCol());
        newBigBoard.setOverallWinner(board.getWinner());
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newBigBoard.getBoards()[i][j] = deepCopy(board.getBoards()[i][j]);
            }
        }
        return newBigBoard;
    }

    // Alternative implementation using Fork-Join framework
    // This is actually more efficient for recursive divide-and-conquer algorithms like minimax
    public int[] findBestMoveForkJoin(BigBoard board, int depth, TranspositionTable map) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        try {
            System.out.println("Starting Fork-Join minimax with " + forkJoinPool.getParallelism() + " threads");
            
            MiniMaxTask task = new MiniMaxTask(board, depth, true, depth, 
                                             -Float.MAX_VALUE, Float.MAX_VALUE, map, 0);
            MoveResult result = forkJoinPool.invoke(task);
            return result.move;
        } finally {
            forkJoinPool.shutdown();
        }
    }

    // Fork-Join implementation - divides work recursively
    private class MiniMaxTask extends RecursiveTask<MoveResult> {
        private final BigBoard board;
        private final int depth;
        private final boolean isMaximizing;
        private final int originalDepth;
        private final float alpha;
        private final float beta;
        private final TranspositionTable map;
        private final int currentLevel;
        
        public MiniMaxTask(BigBoard board, int depth, boolean isMaximizing, int originalDepth,
                          float alpha, float beta, TranspositionTable map, int currentLevel) {
            this.board = board;
            this.depth = depth;
            this.isMaximizing = isMaximizing;
            this.originalDepth = originalDepth;
            this.alpha = alpha;
            this.beta = beta;
            this.map = map;
            this.currentLevel = currentLevel;
        }
        
        @Override
        protected MoveResult compute() {
            ArrayList<int[]> legalMoves = board.getAvailableMoves();
            board.checkOverallWinner();
            
            // Terminal cases
            if (board.getWinner() == 'X') return new MoveResult(new int[]{-1,-1,-1,-1}, -Float.MAX_VALUE);
            if (board.getWinner() == 'O') return new MoveResult(new int[]{-1,-1,-1,-1}, Float.MAX_VALUE);
            if (depth == 0 || legalMoves.size() == 0) {
                return new MoveResult(new int[]{-1,-1,-1,-1}, finalEval(eval(board, map)));
            }
            
            // Only parallelize shallow levels
            if (currentLevel < PARALLEL_DEPTH_LIMIT && legalMoves.size() > 1) {
                return computeParallel(legalMoves);
            } else {
                return computeSequential(legalMoves);
            }
        }
        
        private MoveResult computeParallel(ArrayList<int[]> legalMoves) {
            List<MiniMaxTask> subtasks = new ArrayList<>();
            
            // Create child tasks for each possible move
            for (int[] move : legalMoves) {
                BigBoard boardCopy = deepCopy(board);
                char player = isMaximizing ? 'O' : 'X';
                boardCopy.makeMove(move[0], move[1], move[2], move[3], player);
                
                MiniMaxTask subtask = new MiniMaxTask(boardCopy, depth - 1, !isMaximizing, 
                                                    originalDepth, alpha, beta, map, currentLevel + 1);
                subtasks.add(subtask);
            }
            
            // Fork all child tasks
            for (MiniMaxTask task : subtasks) {
                task.fork();
            }
            
            // Collect results and find best move
            float bestValue = isMaximizing ? -Float.MAX_VALUE : Float.MAX_VALUE;
            int[] bestMoveFound = null;
            
            for (int i = 0; i < subtasks.size(); i++) {
                MoveResult result = subtasks.get(i).join();
                
                if (isMaximizing) {
                    if (result.value > bestValue) {
                        bestValue = result.value;
                        bestMoveFound = legalMoves.get(i);
                    }
                } else {
                    if (result.value < bestValue) {
                        bestValue = result.value;
                        bestMoveFound = legalMoves.get(i);
                    }
                }
            }
            
            return new MoveResult(bestMoveFound, bestValue);
        }
        
        // Sequential computation for deeper levels
        private MoveResult computeSequential(ArrayList<int[]> legalMoves) {
            float bestValue = isMaximizing ? -Float.MAX_VALUE : Float.MAX_VALUE;
            int[] bestMoveFound = null;
            
            for (int[] move : legalMoves) {
                BigBoard boardCopy = deepCopy(board);
                char player = isMaximizing ? 'O' : 'X';
                boardCopy.makeMove(move[0], move[1], move[2], move[3], player);
                
                MiniMaxTask subtask = new MiniMaxTask(boardCopy, depth - 1, !isMaximizing, 
                                                    originalDepth, alpha, beta, map, currentLevel + 1);
                MoveResult result = subtask.compute();
                
                if (isMaximizing) {
                    if (result.value > bestValue) {
                        bestValue = result.value;
                        bestMoveFound = move;
                    }
                } else {
                    if (result.value < bestValue) {
                        bestValue = result.value;
                        bestMoveFound = move;
                    }
                }
            }
            
            return new MoveResult(bestMoveFound, bestValue);
        }
    }
}
