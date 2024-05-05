package SnakeLadder;

import java.util.ArrayList;
import java.util.List;
public class GamePlay {
     private static final String FILE_PATH = "src/Configuration/snakeladder.properties";
    
    public static void main(String[] args) {
        String filePath = FILE_PATH;
        
        Board board = new Board(filePath);
        Board.printBoard(board);
        
        List<Player> players = new ArrayList<>();
        // Add players to the list
        players.add(new Player(1));
        players.add(new Player(2));
        players.add(new Player(3));
        
        
        Dice dice = new Dice(6);
        
        // Create instance of GamePlay with the initialized components
        GameLogic game = new GameLogic(board, dice, players);
        // Start the game
        game.playGame();
        
    }
}
