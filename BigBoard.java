import java.util.ArrayList;

public class BigBoard {
    private SmallBoard[][] boards = new SmallBoard[3][3];
    private char overallWinner = ' ';
    private int nextBoardRow = -1;
    private int nextBoardCol = -1;

    public BigBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                boards[i][j] = new SmallBoard();
    }

    public boolean makeMove(int br, int bc, int r, int c, char player) {
        if (!isLegalMove(br, bc, r, c)) return false;
        
        boolean moveMade = boards[br][bc].makeMove(r, c, player);
        if (moveMade) {
            nextBoardRow = r;
            nextBoardCol = c;
            if (boards[br][bc].getWinner() == 'X'){boards[br][bc].fill('X');} 
            else if (boards[br][bc].getWinner() == 'O'){boards[br][bc].fill('O');}

            if (!boards[nextBoardRow][nextBoardCol].isPlayable()) {
                nextBoardRow = -1;
                nextBoardCol = -1;
            }
            checkOverallWinner();
        }

        return moveMade;
    }

    public boolean isLegalMove(int br, int bc, int r, int c) {
        if (br < 0 || bc < 0 || r < 0 || c < 0 || br > 2 || bc > 2 || r > 2 || c > 2)
            return false;
        if ((nextBoardRow != -1 && (br != nextBoardRow || bc != nextBoardCol)) || !boards[br][bc].isPlayable())
            return false;
        return boards[br][bc].getCell(r, c) == ' ';
    }

    public void checkOverallWinner() {
        char[][] winGrid = new char[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                winGrid[i][j] = boards[i][j].getWinner();

        for (int i = 0; i < 3; i++) {
            if (winGrid[i][0] != ' ' && winGrid[i][0] == winGrid[i][1] && winGrid[i][1] == winGrid[i][2])
                overallWinner = winGrid[i][0];
            if (winGrid[0][i] != ' ' && winGrid[0][i] == winGrid[1][i] && winGrid[1][i] == winGrid[2][i])
                overallWinner = winGrid[0][i];
        }
        if (winGrid[0][0] != ' ' && winGrid[0][0] == winGrid[1][1] && winGrid[1][1] == winGrid[2][2])
            overallWinner = winGrid[0][0];
        if (winGrid[0][2] != ' ' && winGrid[0][2] == winGrid[1][1] && winGrid[1][1] == winGrid[2][0])
            overallWinner = winGrid[0][2];
    }

    public void display() {
        for (int bigRow = 0; bigRow < 3; bigRow++) {
            for (int subRow = 0; subRow < 3; subRow++) {
                for (int bigCol = 0; bigCol < 3; bigCol++) {
                    for (int subCol = 0; subCol < 3; subCol++) {
                        char ch = boards[bigRow][bigCol].getCell(subRow, subCol);
                        System.out.print(ch == ' ' ? '.' : ch);
                    }
                    System.out.print(" ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public boolean isGameOver() {
        return overallWinner != ' ';
    }

    public char getWinner() {
        return overallWinner;
    }

    public int getNextBoardRow() {
        return nextBoardRow;
    }

    public SmallBoard[][] getBoards() {
        return boards;
    }
    public int getNextBoardCol() {
        return nextBoardCol;
    }
    
    public ArrayList<int[]> getAvailableMoves() {
        ArrayList<int[]> moves = new ArrayList<>();
       // System.out.println("nextboardrow: " + nextBoardRow + " nextboardcol: " + nextBoardCol);
        for (int br = 0; br < 3; br++)
            for (int bc = 0; bc < 3; bc++)
                if (((nextBoardRow == -1 && nextBoardCol == -1)|| (br == nextBoardRow && bc == nextBoardCol)) &&
                        boards[br][bc].isPlayable()){
                        System.out.println("br: " + br + " bc: " + bc);
                        System.out.println(nextBoardRow + "nextboardrow + " + "nextboardcol " + nextBoardCol);
                    for (int r = 0; r < 3; r++)
                        for (int c = 0; c < 3; c++)
                            if (boards[br][bc].getCell(r, c) == ' ')
                                moves.add(new int[]{br, bc, r, c});
                                }
        return moves;
    }

    
} 
