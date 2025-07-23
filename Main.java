import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BigBoard board = new BigBoard();
        char currentPlayer = 'X';
        Random rand = new Random();
        int depth;
        int count = 0;
        TranspositionTable tt = new TranspositionTable();
        tt.sbPossibilities(); //ALREADY RUN
        tt.OutputCountsAsCSV(tt.getTable(), "smallBoard_transpositionTable.csv"); //JUST LOAD IN ON NEXT LINE
        tt.loadFromCSV("/Users/ryanding/VSCode/UltimateTicTacToe/smallBoard_transpositionTable.csv");
        
        MiniMax minimax = new MiniMax();
        
        //System.out.println(minimax.miniMax(board, 5, 0, false, -99999, 99999));
        while (!board.isGameOver()) {
            System.out.println("Current Player: " + currentPlayer);
            board.display();

            int br = board.getNextBoardRow();
            int bc = board.getNextBoardCol();

            if (br == -1 || bc == -1) {
                System.out.println("You can start ANYWHERE on the board.");
            } else {
                System.out.println("You must play in board (" + br + ", " + bc + ")");
            }

            if (currentPlayer == 'X') {
                boolean valid = false;
                while (!valid) {
                    if (br == -1 || bc == -1) {
                        System.out.print("Enter which board to play in (boardRow boardCol): ");
                        br = sc.nextInt();
                        bc = sc.nextInt();
                        if (board.getBoards()[br][bc].getWinner() != ' ') {
                            System.out.println("This board is already won. Choose another board.");
                            br = -1; bc = -1;
                            continue;
                        }
                    }

                    System.out.print("Enter your move (row col) inside the selected small board: ");
                    int r = sc.nextInt();
                    int c = sc.nextInt();
                    
                    if (board.makeMove(br, bc, r, c, currentPlayer)) {
                        valid = true;
                    }
                    else if(!board.makeMove(br, bc, r, c, currentPlayer)) {
                        System.out.println("Invalid move. Try again.");
                    }
                }
            } else {
                if (count < 4){
                    depth = 8;
                }
                else{
                    depth = 11;
                }
                long startTime = System.nanoTime();
                int[] move = minimax.findBestMove(board, depth, tt);
                count++;
                long endTime = System.nanoTime();
                long durationNano = endTime - startTime;

                double durationMs = durationNano / 1_000_000.0;
                double durationSeconds = durationNano / 1_000_000_000.0;
                board.makeMove(move[0], move[1], move[2], move[3], currentPlayer);
                System.out.println("Computer played in board (" + move[0] + ", " + move[1] + ") at (" + move[2] + ", " + move[3] + ")");
                System.out.printf("AI calculation time: %.2f ms (%.3f seconds)%n", durationMs, durationSeconds);
            }

            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }

        board.display();
        System.out.println("Game over! Winner: " + board.getWinner());
        sc.close();
    }
    } 


//what still needs to be done:
/* 
 * - make it so that the interface shows a huge x over the winning board
 * - make it so that once a board is won, it cannot be played anymore
 * - problem: whenever we are in a game, and I do an invalid move to a square that is out of bounds,
 *   the other person can play still which shouldn't happen
 * - 
 */