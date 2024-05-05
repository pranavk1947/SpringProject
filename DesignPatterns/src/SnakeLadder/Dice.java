package SnakeLadder;

import java.util.Random;

public class Dice {
    
    private int numberOfFaces; // Number of faces on the dice

    public Dice(int numberOfFaces) {
        this.numberOfFaces = numberOfFaces;
    }

    // Method to roll the dice and return a random number between 1 and numberOfFaces
    public int roll() {
        Random random = new Random();
        return random.nextInt(numberOfFaces) + 1; // Adding 1 to exclude 0 and include numberOfFaces
    }
    
    // Getters and setters if needed
    public int getNumberOfFaces() {
        return numberOfFaces;
    }

    public void setNumberOfFaces(int numberOfFaces) {
        this.numberOfFaces = numberOfFaces;
    }
}

