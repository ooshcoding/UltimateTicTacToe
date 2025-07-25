import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static int depth = 7;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String simulation_file_name = "NO_ABP_TIME_DEPTH";

        System.out.println("=== Ultimate Tic Tac Toe ===");
        System.out.println("1. Play Game");
        System.out.println("2. Run AI Performance Test");
        System.out.println("3. Run AI Performance Test with 30 Game States");
        System.out.print("Choose option (1, 2, or 3): ");
        
        int choice = sc.nextInt();
        
        if (choice == 1) {
            playGame();
        } else if (choice == 2) {
            runPerformanceTest();
        } 
        else if (choice == 3) {
            System.out.println("Running performance test with 30 game states...");
            simulateGames(30);        
        }
        else {
            System.out.println("Invalid choice. Running performance test by default.");
            runPerformanceTest();
        }
        
        sc.close();
    }
    public static void simulateGames(int numGames) {

        Scanner sc = new Scanner(System.in);
        BigBoard board = new BigBoard();
        char currentPlayer = 'X';
        MiniMax minimax = new MiniMax();
        TranspositionTable tt = new TranspositionTable();
        tt.sbPossibilities(); 
        tt.outputTableAsJsObject(
            tt.getTable(), "website_transpositionTable.js"); //JUST LOAD IN ON NEXT LINE
        tt.loadFromCSV("/Users/ryanding/VSCode/UltimateTicTacToe/smallBoard_transpositionTable.csv");
        int minimax_win = 0;
        int random_win = 0;
        for (int i = 0; i < numGames; i++) {
            while (!board.isGameOver()) {
                int br = board.getNextBoardRow();
                int bc = board.getNextBoardCol();
                
                if (currentPlayer == 'X') {
                    boolean valid = false;
                    while (!valid){
                        int[] move = findRandomMove(board);
                        if (move == null) {
                            System.out.println("No valid moves available for player X!");
                            break; // Exit the game loop
                        }
                        if (board.makeMove(move[0], move[1], move[2], move[3], currentPlayer)){
                            valid = true;
                        }
                        else {
                            System.out.println("Invalid move by player X. Trying again...");
                        }
                    }


                }
                 else {
                long startTime = System.nanoTime();
                
                int[] move = minimax.findBestMove(board, depth, -Float.MAX_VALUE, Float.MAX_VALUE, tt);
                // Check if we got a valid move
                if (move == null) {
                    System.out.println("ERROR: AI could not find a move!");
                    // Try to get any available move as emergency fallback
                    java.util.ArrayList<int[]> emergencyMoves = board.getAvailableMoves();
                    if (!emergencyMoves.isEmpty()) {
                        move = emergencyMoves.get(0);
                        System.out.println("Using emergency move: " + java.util.Arrays.toString(move));
                    } else {
                        System.out.println("CRITICAL ERROR: No moves available at all!");
                        break; // Exit the game loop
                    }
                }

                // Validate move array
                if (move.length != 4) {
                    System.out.println("ERROR: Invalid move array length: " + move.length);
                    continue;
                }



                // Validate move coordinates
                if (move[0] < 0 || move[0] > 2 || move[1] < 0 || move[1] > 2 || 
                    move[2] < 0 || move[2] > 2 || move[3] < 0 || move[3] > 2) {
                    System.out.println("ERROR: Move coordinates out of bounds: " + java.util.Arrays.toString(move));
                    continue;
                }

                boolean moveSuccess = board.makeMove(move[0], move[1], move[2], move[3], currentPlayer);
                
                long endTime = System.nanoTime();
                long durationNano = endTime - startTime;

                double durationMs = durationNano / 1_000_000.0;
                double durationSeconds = durationNano / 1_000_000_000.0;
                //board.makeMove(move[0], move[1], move[2], move[3], currentPlayer);
                //System.out.println("Computer played in board (" + move[0] + ", " + move[1] + ") at (" + move[2] + ", " + move[3] + ")");
                //System.out.printf("AI calculation time: %.2f ms (%.3f seconds)%n", durationMs, durationSeconds);
                if (!moveSuccess) {
                    System.out.println("ERROR: Failed to make AI move: " + java.util.Arrays.toString(move));
                    continue;
                }
            }
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }
            System.out.println("Game " + (i + 1) + " over! Winner: " + board.getWinner());
            if (board.getWinner() == 'X'){
                random_win++;
            }
            else{
                minimax_win++;
            }
            board.reset(); // Reset the board for the next game

            }
            
            System.out.println(minimax_win/30.0 * 100 + "% of games won by Minimax AI");
            System.out.println(minimax_win + "# of games won by Minimax AI");

            sc.close();
        }
    


    public static void playGame() {
        Scanner sc = new Scanner(System.in);
        BigBoard board = new BigBoard();
        char currentPlayer = 'X';
        
        int count = 0;
        TranspositionTable tt = new TranspositionTable();
        tt.sbPossibilities(); //ALREADY RUN
        tt.OutputCountsAsCSV(tt.getTable(), "smallBoard_transpositionTable.csv"); //JUST LOAD IN ON NEXT LINE
        tt.loadFromCSV("/Users/ryanding/VSCode/UltimateTicTacToe/smallBoard_transpositionTable.csv");
        
        MiniMax minimax = new MiniMax();
        
        while (!board.isGameOver()) {
            System.out.println("Current Player: " + currentPlayer);
            board.display();

            int br = board.getNextBoardRow();
            int bc = board.getNextBoardCol();

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
                if (count < 7){
                    depth = 9; //depth increases as the game goes on
                }
                else if (count < 27){
                    depth = 10;
                }
                else{
                    depth = 11;
                }
                long startTime = System.nanoTime();
                if (bc == -1 || br == -1){
                    depth--;
                }
                int[] move = minimax.findBestMove(board, depth, -Float.MAX_VALUE, Float.MAX_VALUE, tt);
                count++;
                // Check if we got a valid move
                if (move == null) {
                    System.out.println("ERROR: AI could not find a move!");
                    // Try to get any available move as emergency fallback
                    java.util.ArrayList<int[]> emergencyMoves = board.getAvailableMoves();
                    if (!emergencyMoves.isEmpty()) {
                        move = emergencyMoves.get(0);
                        System.out.println("Using emergency move: " + java.util.Arrays.toString(move));
                    } else {
                        System.out.println("CRITICAL ERROR: No moves available at all!");
                        break; // Exit the game loop
                    }
                }

                // Validate move array
                if (move.length != 4) {
                    System.out.println("ERROR: Invalid move array length: " + move.length);
                    continue;
                }



                // Validate move coordinates
                if (move[0] < 0 || move[0] > 2 || move[1] < 0 || move[1] > 2 || 
                    move[2] < 0 || move[2] > 2 || move[3] < 0 || move[3] > 2) {
                    System.out.println("ERROR: Move coordinates out of bounds: " + java.util.Arrays.toString(move));
                    continue;
                }

                boolean moveSuccess = board.makeMove(move[0], move[1], move[2], move[3], currentPlayer);
        
                if (!moveSuccess) {
                    System.out.println("ERROR: Failed to make AI move: " + java.util.Arrays.toString(move));
                    continue;
                }
                
                long endTime = System.nanoTime();
                long durationNano = endTime - startTime;

                double durationMs = durationNano / 1_000_000.0;
                double durationSeconds = durationNano / 1_000_000_000.0;
                //board.makeMove(move[0], move[1], move[2], move[3], currentPlayer);
                System.out.println("Computer played in board (" + move[0] + ", " + move[1] + ") at (" + move[2] + ", " + move[3] + ")");
                System.out.printf("AI calculation time: %.2f ms (%.3f seconds)%n", durationMs, durationSeconds);

                
            }

            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }

        board.display();
        System.out.println("Game over! Winner: " + board.getWinner());
        sc.close();
    }


    // Generate 30 specific game states for testing
public static ArrayList<int[][]> generateTestGameStates() {
    ArrayList<int[][]> gameStates = new ArrayList<>();
    Random rand = new Random(12345); 
    
    int[][][] predefinedStates = {
        // State 1: Center start
        {{1, 1, 1, 1}, {1, 1, 0, 0}, {0, 0, 2, 2}},
        // State 2: Corner start
        {{0, 0, 0, 0}, {0, 0, 1, 1}, {1, 1, 2, 2}},
        // State 3: Edge start
        {{0, 1, 1, 0}, {1, 0, 0, 1}, {0, 1, 2, 2}},
        // State 4: Mixed positions
        {{2, 2, 1, 1}, {1, 1, 0, 0}, {0, 0, 2, 1}},
        // State 5: Strategic center control
        {{1, 1, 0, 0}, {0, 0, 1, 1}, {1, 1, 2, 2}},

    };
    
    // Add the 5 predefined states
    for (int[][] state : predefinedStates) {
        gameStates.add(state);
    }
    
    // Generate 25 additional random states
    for (int i = 5; i < 30; i++) {
        int[][] gameState = new int[3][4];
        BigBoard tempBoard = new BigBoard();
        char[] players = {'X', 'O', 'X'};
        boolean validState = false;
        int attempts = 0;
        
        while (!validState && attempts < 100) {
            tempBoard = new BigBoard();
            validState = true;
            attempts++;
            
            for (int moveNum = 0; moveNum < 3; moveNum++) {
                ArrayList<int[]> availableMoves = tempBoard.getAvailableMoves();
                
                if (availableMoves.isEmpty()) {
                    validState = false;
                    break;
                }
                
                int[] move = availableMoves.get(rand.nextInt(availableMoves.size()));
                
                if (!tempBoard.makeMove(move[0], move[1], move[2], move[3], players[moveNum])) {
                    validState = false;
                    break;
                }
                
                gameState[moveNum] = move.clone();
            }
            
            // Ensure the game hasn't ended and there are still moves available
            if (validState && (tempBoard.isGameOver() || tempBoard.getAvailableMoves().isEmpty())) {
                validState = false;
            }
        }
        
        if (validState) {
            gameStates.add(gameState);
        } else {
            // Fallback to a simple valid state
            int[][] fallbackState = {{i % 3, (i + 1) % 3, 0, 0}, 
                                   {(i + 1) % 3, (i + 2) % 3, 1, 1}, 
                                   {(i + 2) % 3, i % 3, 2, 2}};
            gameStates.add(fallbackState);
        }
    }
    
    return gameStates;
}

    // Run the performance test with the generated states
public static void runPerformanceTest(String simulation_file_name) {

    System.out.println("=== AI Performance Testing Framework ===");
    System.out.println("Generating 30 game states...");
    
    ArrayList<int[][]> gameStates = generateTestGameStates();
    
    // Initialize AI components
    TranspositionTable tt = new TranspositionTable();
    System.out.println("Loading transposition table...");
    tt.loadFromCSV("/Users/ryanding/VSCode/UltimateTicTacToe/smallBoard_transpositionTable.csv");
    MiniMax minimax = new MiniMax();
    
    // Prepare CSV file
    try (FileWriter csvWriter = new FileWriter(simulation_file_name + depth + ".csv")) {
        csvWriter.write("Depth,Simulation Run,Time,Board Number\n");
        
        System.out.println("Starting performance testing...");
        
        for (int boardId = 0; boardId < gameStates.size(); boardId++) {
            System.out.println("Testing Board " + (boardId + 1) + "/30...");
            
            // Display the game state moves for reference
            int[][] moves = gameStates.get(boardId);
            System.out.println("Game state moves:");
            char[] players = {'X', 'O', 'X'};
            for (int i = 0; i < 3; i++) {
                System.out.println("  Move " + (i+1) + " (" + players[i] + "): Board(" + 
                                 moves[i][0] + "," + moves[i][1] + ") Cell(" + 
                                 moves[i][2] + "," + moves[i][3] + ")");
            }
            
            // Run 50 simulations for this game state
            for (int sim = 1; sim <= 30; sim++) {
                // Create fresh board from game state
                BigBoard board = new BigBoard();
                for (int i = 0; i < 3; i++) {
                    board.makeMove(moves[i][0], moves[i][1], moves[i][2], moves[i][3], players[i]);
                }
                
                        ; // Since we're at move 4, count would be 3
                boolean nextBoardRestricted = true;
                
                // Check if next board is unrestricted
                if (board.getNextBoardRow() == -1 || board.getNextBoardCol() == -1) {
                    nextBoardRestricted = false;
                }
                
                int availableMoves = board.getAvailableMoves().size();
                
                // Time the AI move calculation
                long startTime = System.nanoTime();
                int[] move = minimax.findBestMove(board, depth, -Float.MAX_VALUE, Float.MAX_VALUE, tt);
                long endTime = System.nanoTime();
                
                // Calculate timing
                long durationNano = endTime - startTime;
                double durationMs = durationNano / 1_000_000.0;
                double durationSeconds = durationNano / 1_000_000_000.0;
                
                // Format move string
                String moveStr = "";
                if (move != null && move.length == 4) {
                    moveStr = "(" + move[0] + "," + move[1] + "," + move[2] + "," + move[3] + ")";
                } else {
                    moveStr = "ERROR";
                }
                
                // Write to CSV
                csvWriter.write(String.format("%d,%d,%.6f,%d\n", depth, sim, durationSeconds, boardId+1));                
                // Progress indicator
                if (sim % 10 == 0) {
                    System.out.printf("  Completed %d/30 simulations (avg: %.2f ms)\n", sim, durationMs);
                }
            }
        }
        
        System.out.println("\n=== Performance Testing Completed! ===");
        System.out.println("Results saved to: ai_performance_results.csv");
        System.out.println("Total data points: " + (30 * 50) + " entries");
        
    } catch (IOException e) {
        System.err.println("Error writing to CSV file: " + e.getMessage());
    }
}
public static int[] findRandomMove(BigBoard board) {
    Random random = new Random();
    ArrayList<int[]> moves = board.getAvailableMoves();
    
    if (moves.isEmpty()) {
        return null;
    }
    
    int randomIndex = random.nextInt(moves.size());
    return moves.get(randomIndex);
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