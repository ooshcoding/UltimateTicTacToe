import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BigBoard board = new BigBoard();
        char currentPlayer = 'X';
        Random rand = new Random();

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
                    }

                    System.out.print("Enter your move (row col) inside the selected small board: ");
                    int r = sc.nextInt();
                    int c = sc.nextInt();
                    
                    if (board.makeMove(br, bc, r, c, currentPlayer)) {
                        valid = true;
                    } else {
                        System.out.println("Invalid move. Try again.");
                    }
                }
            } else {
                List<int[]> moves = board.getAvailableMoves();
                int[] move = moves.get(rand.nextInt(moves.size()));
                board.makeMove(move[0], move[1], move[2], move[3], currentPlayer);
                System.out.println("Computer played in board (" + move[0] + ", " + move[1] + ") at (" + move[2] + ", " + move[3] + ")");
            }

            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }

        board.display();
        System.out.println("Game over! Winner: " + board.getWinner());
    }
}

//what still needs to be done:
/* 
 * - make it so that the interface shows a huge x over the winning board
 * - make it so that once a board is won, it cannot be played anymore
 */