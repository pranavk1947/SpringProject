package SnakeLadder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.io.FileInputStream;
import java.io.IOException;
public class Board {
    private int boardSize;
    private List<Cell> cells;
    public Board(String filePath) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
            boardSize = Integer.parseInt(properties.getProperty("boardsize"));
            cells = new ArrayList<>(boardSize);
            initializeBoard();
        } catch (IOException | NumberFormatException e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }
    private void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            cells.add(new Cell(i, CellType.NORMAL));
        }
        // Set specific cells as ladder start and end points
        setRandomLadders();
        // Set specific cells as snake start and end points
        setRandomSnakes();
    }
    private void setRandomLadders() {
        Random random = new Random();
        int numberOfLadders = boardSize / 8; // Example: One ladder for every 8 cells
        for (int i = 0; i < numberOfLadders; i++) {
            int start = random.nextInt(boardSize - 2)+1;
            int end = random.nextInt(boardSize - start) + start; // Ensuring end position is higher than start position
            if (start < end && cells.get(start).getType() == CellType.NORMAL
                    && cells.get(end).getType() == CellType.NORMAL) {
                cells.get(start).setType(CellType.LADDER_START);
                cells.get(end).setType(CellType.LADDER_END);
            }
        }
    }
    private void setRandomSnakes() {
        Random random = new Random();
        int numberOfSnakes = boardSize / 7; // Example: One snake for every 7 cells
        for (int i = 0; i < numberOfSnakes; i++) {
            int start = random.nextInt(boardSize - 2)+1;
            int end = random.nextInt(start-1)+1;
            if (cells.get(start).getType() == CellType.NORMAL
                    && cells.get(end).getType() == CellType.NORMAL) {
                cells.get(start).setType(CellType.SNAKE_START);
                cells.get(end).setType(CellType.SNAKE_END);
            }
        }
    }
    
    
    
     public void movePlayer(Player player, int steps) {
            int currentPosition = player.getcurrentposition();
            int newPosition = currentPosition + steps;
            // Check if the new position exceeds the board size
            if (newPosition > boardSize) {
                player.setcurrentposition(currentPosition);
            }
            
            else if (newPosition < cells.size()) {
                Cell cell = cells.get(newPosition);
                switch (cell.getType()) {
                    case LADDER_START:
                        int ladderEnd = findLadderEnd(cells, newPosition);
                        System.out.println("YAYY! climbed a Ladder");
                        player.setcurrentposition(ladderEnd);
                        break;
                    case SNAKE_START:
                        int snakeEnd = findSnakeEnd(cells, newPosition);
                        System.out.println("OOPS! Bitten by a snake");
                        player.setcurrentposition(snakeEnd);
                        break;
                    default:
                        player.setcurrentposition(newPosition);
                        break;
                }
            } 
        }
     
        // Helper method to find the ladder end position
        private int findLadderEnd(List<Cell> cells, int currentPosition) {
            for (int i = currentPosition + 1; i < cells.size(); i++) {
                if (cells.get(i).getType() == CellType.LADDER_END) {
                    return i;
                }
            }
            return currentPosition;
        }
        // Helper method to find the snake end position
        private int findSnakeEnd(List<Cell> cells, int currentPosition) {
            for (int i = currentPosition - 1; i >= 0; i--) {
                if (cells.get(i).getType() == CellType.SNAKE_END) {
                    return i;
                }
            }
            return currentPosition;
        }
    
    public int getBoardSize() {
        return boardSize;
    }
    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }
    public List<Cell> getCells() {
        return cells;
    }
    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }
    
    static void printBoard(Board board) {
        List<Cell> cells = board.getCells();
        int boardSize = board.getBoardSize();
        
        int squaresize = (int) Math.sqrt(boardSize);
        
        
        
        Cell[][] boardArray = new Cell[squaresize][squaresize]; // Assuming a square board, change as needed
        int rowIndex = 0;
        int colIndex = 0;
        for (int i = 0; i < boardSize; i++) {
            boardArray[rowIndex][colIndex] = cells.get(i);
            colIndex++;
            if (colIndex == squaresize) {
                colIndex = 0;
                rowIndex++;
            }
        }
        
        
        // Printing the board array
        for (int i = 0; i < boardArray.length; i++) {
            for (int j = 0; j < boardArray[i].length; j++) {
                Cell cell = boardArray[i][j];
                if (cell != null) {
                    switch (cell.getType()) {
                        case NORMAL:
                            System.out.print("[N] ");
                            break;
                        case LADDER_START:
                            System.out.print("[LS] ");
                            break;
                        case LADDER_END:
                            System.out.print("[LE] ");
                            break;
                        case SNAKE_START:
                            System.out.print("[SS] ");
                            break;
                        case SNAKE_END:
                            System.out.print("[SE] ");
                            break;
                        default:
                            break;
                    }
                } else {
                    System.out.print("[ ] ");
                }
            }
            System.out.println();
        }
    }
    
}