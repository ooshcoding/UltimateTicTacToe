
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Map;

public class TranspositionTable {
    private ConcurrentHashMap<String, Float> table;

    public TranspositionTable(){
        table = new java.util.concurrent.ConcurrentHashMap<>();
    }

    public ConcurrentHashMap <String, Float> getTable() {
        return table;
    }
    public void store(String key, Float value) {
        table.put(key, value);
    }
    public Float retrieve(String key) {
        return table.get(key);
    }
    public boolean contains(String key) {
        return table.containsKey(key);
    }
    public char[][] ifA(int a, char[][] grid) {
        if (a == 0){
            grid[0][0] = 'X';
        }
        else if (a==1){
            grid[0][0] = 'O';
        }
        else if (a == 2){
            grid[0][0] = ' ';
        }
        return grid;
    }
    public char[][] ifB(int a, int b, char[][]grid){
        if (b == 0){
            grid[0][1] = 'X';
            ifA(a, grid);
        }
        else if (b == 1){
            grid[0][1] = 'O';
            ifA(a, grid);
        }
        else if (b == 2){
            grid[0][1] = ' ';
            ifA(a, grid);
        }
        return grid;
    }
    public char[][] ifC(int a, int b, int c, char[][]grid){
        if (c == 0){
            grid[0][2] = 'X';
            ifB(a, b, grid);
        }
        else if (c == 1){
            grid[0][2] = 'O';
            ifB(a, b, grid);
        }
        else if (c == 2){
            grid[0][2] = ' ';
            ifB(a, b, grid);
        }
        return grid;
    }
    public char[][] ifD(int a, int b, int c, int d, char[][]grid){
        if (d == 0){
            grid[1][0] = 'X';
            ifC(a, b, c, grid);
        }
        else if (d == 1){
            grid[1][0] = 'O';
            ifC(a, b, c, grid);
        }
        else if (d == 2){
            grid[1][0] = ' ';
            ifC(a, b, c, grid);
        }
        return grid;
    }
    public char[][] ifE(int a, int b, int c, int d, int e, char[][]grid){
        if (e == 0){
            grid[1][1] = 'X';
            ifD(a, b, c, d, grid);
        }
        else if (e == 1){
            grid[1][1] = 'O';
            ifD(a, b, c, d, grid);
        }
        else if (e == 2){
            grid[1][1] = ' ';
            ifD(a, b, c, d, grid);
        }
        return grid;
    }
public char[][] ifF(int a, int b, int c, int d, int e, int f, char[][]grid){
        if (f == 0){
            grid[1][2] = 'X';
            ifE(a, b, c, d, e, grid);
        }
        else if (f == 1){
            grid[1][2] = 'O';
            ifE(a, b, c, d, e, grid);
        }
        else if (f == 2){
            grid[1][2] = ' ';
            ifE(a, b, c, d, e, grid);
        }
        return grid;
    }
    public char[][] ifG(int a, int b, int c, int d, int e, int f, int g, char[][]grid){
        if (g == 0){
            grid[2][0] = 'X';
            ifF(a, b, c, d, e, f, grid);
        }
        else if (g == 1){
            grid[2][0] = 'O';
            ifF(a, b, c, d, e, f, grid);
        }
        else if (g == 2){
            grid[2][0] = ' ';
            ifF(a, b, c, d, e, f, grid);
        }
        return grid;
    }
    public char[][] ifH(int a, int b, int c, int d, int e, int f, int g, int h, char[][]grid){
        if (h == 0){
            grid[2][1] = 'X';
            ifG(a, b, c, d, e, f, g, grid);
        }
        else if (h == 1){
            grid[2][1] = 'O';
            ifG(a, b, c, d, e, f, g, grid);
        }
        else if (h == 2){
            grid[2][1] = ' ';
            ifG(a, b, c, d, e, f, g, grid);
        }
        return grid;
    }
    public char[][] ifI(int a, int b, int c, int d, int e, int f, int g, int h, int i, char[][]grid){
        if (i == 0){
            grid[2][2] = 'X';
            ifH(a, b, c, d, e, f, g, h, grid);
        }
        else if (i == 1){
            grid[2][2] = 'O';
            ifH(a, b, c, d, e, f, g, h, grid);
        }
        else if (i == 2){
            grid[2][2] = ' ';
            ifH(a, b, c, d, e, f, g, h, grid);
        }
        return grid;
    }
    public void sbPossibilities(){
        SmallBoard sb = new SmallBoard();
        char[][] grid = sb.getGrid();
        // Initialize the grid with empty spaces
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j] = ' ';
            }
        }
       
        //System.out.println("sjsjbsj" + table.get(key));
        for (int a = 0; a < 3; a++){
            for (int b = 0; b < 3; b++){
                for (int c =0; c< 3; c++){
                    for (int d = 0; d < 3; d++){
                        for (int e = 0; e < 3; e++){
                            for (int f = 0; f < 3; f++){
                                for (int g = 0; g < 3; g++){
                                    for (int h = 0; h < 3; h++){
                                        for (int i = 0; i < 3; i++){
                                            grid = ifI(a, b, c, d, e, f, g, h, i, grid);
                                            
                                            String key = "" + grid[0][0] + grid[0][1] + grid[0][2] +
                                                    grid[1][0] + grid[1][1] + grid[1][2] +
                                                    grid[2][0] + grid[2][1] + grid[2][2];
                                            
                                            System.out.println(sb.eval(sb));
                                    
                                            table.put(key, sb.eval(sb));
                                            
                                            
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void OutputCountsAsCSV(ConcurrentHashMap<String, Float> table, String filename) {
            
    try (FileWriter writer = new FileWriter(filename)) {
        for (ConcurrentHashMap.Entry<String, Float> entry : table.entrySet()) {
            String rowText = String.format("%s,%f\n", entry.getKey(), entry.getValue());  
            writer.write(rowText);
        }
    
    } catch (Exception e) {
        System.out.println("Saving CSV to file failed..."); 
    } finally {
        System.out.println("CSV File saved successfully...");      
    } 
}
    public void loadFromCSV(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String key = parts[0];
                    Float value = Float.parseFloat(parts[1]);
                    table.put(key, value);
                }
            }
            System.out.println("Loaded Transposition Table from CSV successfully.");
        } catch (Exception e) {
            System.out.println("Failed to load CSV file...");
            e.printStackTrace();
        }
    }
}
