public class MiniMax {
    private int x_wins;
    private int o_wins;
    private int depth;
    private float[][] ratio = new float[3][3];
    public MiniMax(){
        this.x_wins = 0;
        this.o_wins = 0;
        this.depth = 5;
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
                boolean done = false;
                for (int c = 0; c < 3; c++){
                    if (done){break;} 
                    if (grid[0][c] != ' ' || grid[1][c] != ' ' || grid[2][c] != ' '){
                        if (done){break;}
                        if (colWin(grid[0][c], 0, grid)!= ' '){ //if column has possible win
                            if (done) {break;}
                            if (colWin(grid[0][c], 0, grid) == 'X'){ // if the column has possible win for X
                                x_wins++;
                                done = true;
                                break;
                                } 
                            else if (colWin(grid[0][c], 0, grid) == 'O'){ // if column has possible win for O
                                o_wins++;
                                done = true;
                                break;
                            }
                        }     
                    }
                    
                }
                
                done = false; 
                
                for (int c = 0; c < 3; c++){
                    if (done){break;} 
                    if (grid[0][c] != ' ' || grid[1][c] != ' ' || grid[2][c] != ' '){
                        if (done){break;}
                        if (colWin(grid[1][c], 0, grid)!= ' '){ //if column has possible win
                            if (done) {break;}
                            if (colWin(grid[1][c], 0, grid) == 'X'){ // if the column has possible win for X
                                x_wins++;
                                done = true;
                                break;
                                } 
                            else if (colWin(grid[1][c], 0, grid) == 'O'){ // if column has possible win for O
                                o_wins++;
                                done = true;
                                break;
                            }
                        }     
                    }
                    
                }
                
                done = false;   // check for second column

                for (int c = 0; c < 3; c++){
                    if (done){break;} 
                    if (grid[0][c] != ' ' || grid[1][c] != ' ' || grid[2][c] != ' '){
                        if (done){break;}
                        if (colWin(grid[2][c], 0, grid)!= ' '){ //if column has possible win
                            if (done) {break;}
                            if (colWin(grid[2][c], 0, grid) == 'X'){ // if the column has possible win for X
                                x_wins++;
                                done = true;
                                break;
                                } 
                            else if (colWin(grid[2][c], 0, grid) == 'O'){ // if column has possible win for O
                                o_wins++;
                                done = true;
                                break;
                            }
                        }     
                    }
                    
                } // check for third column
                
                done = false;   
                for (int r = 0; r < 3; r++) {
                    if (done){break;}
                    if (grid[r][0] != ' ' || grid[r][1] != ' ' || grid[r][2] != ' '){ 
                        if (done){break;}
                        if (rowWin(grid[r][0], r, grid) != ' ') {
                            if (done) {break;}
                            if (rowWin(grid[r][0], r, grid) == 'X') {
                                x_wins++;
                                done = true;
                                break;
                            } else if (rowWin(grid[r][0], r, grid) == 'O') {
                                o_wins++;
                                done = true;
                                break;
                            }
                        }
                    }
                }

                done = false;   
                for (int r = 0; r < 3; r++) {
                    if (done){break;}
                    if (grid[r][0] != ' ' || grid[r][1] != ' ' || grid[r][2] != ' '){ 
                        if (done){break;}
                        if (rowWin(grid[r][1], r, grid) != ' ') {
                            if (done) {break;}
                            if (rowWin(grid[r][1], r, grid) == 'X') {
                                x_wins++;
                                done = true;
                                break;
                            } else if (rowWin(grid[r][1], r, grid) == 'O') {
                                o_wins++;
                                done = true;
                                break;
                            }
                        }
                    }
                }

                done = false;   
                for (int r = 0; r < 3; r++) {
                    if (done){break;}
                    if (grid[r][0] != ' ' || grid[r][1] != ' ' || grid[r][2] != ' '){ 
                        if (done){break;}
                        if (rowWin(grid[r][2], r, grid) != ' ') {
                            if (done) {break;}
                            if (rowWin(grid[r][2], r, grid) == 'X') {
                                x_wins++;
                                done = true;
                                break;
                            } else if (rowWin(grid[r][2], r, grid) == 'O') {
                                o_wins++;
                                done = true;
                                break;
                            }
                        }
                    }
                }
                done = false;
                for (int d = 0; d < 3; d++){ // first diagonal top left to bottom right
                    if (done){break;}
                    if (grid[d][d] != ' '){
                        if (done){break;}
                        if (grid[d][d] == 'X'){
                            if (grid[0][0] != 'O' && grid[1][1] != 'O' && grid[2][2] != 'O'){
                                x_wins++;
                                done = true;
                                break;
                            } 
                        }
                        if (grid[d][d] == 'O'){
                            if (grid[0][0] != 'X' && grid[1][1] != 'X' && grid[2][2] != 'X'){
                                o_wins++;
                                done = true;
                                break;
                            } 
                        }
                        
                    }
                }
                done = false;
                for (int d = 0; d < 3; d++){
                    if (done){break;}
                    if (grid[d][2-d] != ' '){
                        if (done){break;}
                        if (grid[d][2-d] == 'X'){
                            if (grid[0][2] != 'O' && grid[1][1] != 'O' && grid[2][0] != 'O'){
                                x_wins++;
                                done = true;
                                break;
                            } 
                        }
                        if (grid[d][2-d] == 'O'){
                            if (grid[0][2] != 'X' && grid[1][1] != 'X' && grid[2][0] != 'X'){
                                o_wins++;
                                done = true;
                                break;
                            } 
                        }
                        
                    }
                }
                done = false;
                if (x_wins == 0){
                    ratio[i][j] = (float) 100;
                }
                else {
                    ratio[i][j] = (float) o_wins / x_wins;
                    System.out.println(o_wins + " " + x_wins);
                }
                o_wins = 0;
                x_wins = 0;
               
            }
        }
        return ratio;
 }
        
    

    public char colWin(char val, int col, char[][] grid){
        if (val == 'X'){
            if (grid[0][col] != 'O' && grid[1][col] != 'O' && grid[2][col] != 'O'){
                if (grid[0][col] != ' ' || grid[1][col] != ' ' || grid[2][col] != ' '){
                    return 'X';
                }
            }

        }

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




}
