package SnakeLadder;

import java.util.List;

public class Player {
    private int playerId;
    private int currentposition;
    
    public Player(int playerId) {
        this.playerId = playerId;
        this.currentposition = 0; //assuming any new player created starts at position 0
    }

    //getters and setters
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getcurrentposition() {
        return currentposition;
    }

    public void setcurrentposition(int currentposition) {
        this.currentposition = currentposition;
    }
    
    

}
