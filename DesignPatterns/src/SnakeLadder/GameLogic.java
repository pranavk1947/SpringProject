package SnakeLadder;

import java.util.List;
import java.util.Scanner;
public class GameLogic {
    private Board board;
    private Dice dice;
    private List<Player> players;
    public GameLogic(Board board, Dice dice, List<Player> players) {
        this.board = board;
        this.dice = dice;
        this.players = players;
    }
    // Method to simulate the game
    public void playGame() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // Iterate through each player's turn
            for (Player currentPlayer : players) {
                System.out.println("Player " + currentPlayer.getPlayerId() + "'s turn. Press 'r' to roll the dice.");
                // Wait for 'r' key press to roll the dice
                String input = scanner.nextLine();
                if (input.equals("r")) {
                    // Roll the dice
                    int steps = dice.roll();
                    System.out.println("Dice rolled: " + steps);
                    // Move the player on the board based on the steps rolled
                    board.movePlayer(currentPlayer, steps);
                    // Display the  new position of the player
                    System.out.println("New position of Player " + currentPlayer.getPlayerId() + ": " + currentPlayer.getcurrentposition());
                    // Check if the current player has reached the end position
                    if (currentPlayer.getcurrentposition() == board.getBoardSize()) {
                        System.out.println("Player " + currentPlayer.getPlayerId() + " wins!");
                        scanner.close();
                        return; // End the game
                    }
                }
            }
        }
    }
}