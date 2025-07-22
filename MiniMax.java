import java.util.ArrayList;
import java.util.Arrays;

public class MiniMax {

    public static class MoveScore {
        int[] move;
        float score;

        MoveScore(int[] move, float score) {
            this.move = move;
            this.score = score;
        }
    }

    public float evaluateBoard(BigBoard board) {
        if (board.getWinner() == 'O') return 1000;
        if (board.getWinner() == 'X') return -1000;
        return finalRatio(eval(board));
    }

    public MoveScore miniMax(BigBoard board, int depth, boolean isMaximizing, int originalDepth) {
        ArrayList<int[]> legalMoves = board.getAvailableMoves();

        if (depth == 0 || board.getWinner() != ' ' || legalMoves.size() == 0) {
            return new MoveScore(null, evaluateBoard(board));
        }

        if (isMaximizing) {
            float bestScore = -Float.MAX_VALUE;
            int[] bestMove = null;
            for (int[] move : legalMoves) {
                BigBoard boardCopy = deepCopy(board);
                boardCopy.makeMove(move[0], move[1], move[2], move[3], 'O');
                boardCopy.checkOverallWinner();  // Ensures board winner is updated

                float score = miniMax(boardCopy, depth - 1, false, originalDepth).score;
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
            return new MoveScore(bestMove, bestScore);
        } else {
            float bestScore = Float.MAX_VALUE;
            int[] bestMove = null;
            for (int[] move : legalMoves) {
                BigBoard boardCopy = deepCopy(board);
                boardCopy.makeMove(move[0], move[1], move[2], move[3], 'X');
                boardCopy.checkOverallWinner();  // Ensures board winner is updated

                float score = miniMax(boardCopy, depth - 1, true, originalDepth).score;
                if (score < bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
            return new MoveScore(bestMove, bestScore);
        }
    }

    public int[] findBestMove(BigBoard board, int depth) {
        MoveScore result = miniMax(board, depth, true, depth);
        System.out.println("Final bestMove " + Arrays.toString(result.move) + " | Score: " + result.score);
        return result.move;
    }

    public BigBoard deepCopy(BigBoard board) {
    BigBoard newBigBoard = new BigBoard();
    
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            newBigBoard.getBoards()[i][j] = deepCopy(board.getBoards()[i][j]);
        }
    }

    newBigBoard.setWinner(board.getWinner());
    newBigBoard.setCurrentBoard(board.getCurrentBoardRow(), board.getCurrentBoardCol());

    return newBigBoard;
    }

    // Reuse your eval, finalRatio, colWin, rowWin, and deepCopy methods here
    // Example stubs shown below for completeness, replace with your actual logic:

    public float[][] eval(BigBoard board) {
        // Return a 3x3 evaluation matrix based on your logic
        return new float[3][3];
    }

    public float finalRatio(float[][] ratios) {
        // Calculate a single float score from the ratio board
        return 0;
    }

    public BigBoard deepCopy(BigBoard board) {
        BigBoard newBoard = new BigBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newBoard.getBoards()[i][j] = deepCopy(board.getBoards()[i][j]);
            }
        }
        return newBoard;
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
        newBoard.setWinner(sb.getWinner()); // make sure winner state is copied
        newBoard.setPlayable(sb.isPlayable());
        return newBoard;
    }
}
