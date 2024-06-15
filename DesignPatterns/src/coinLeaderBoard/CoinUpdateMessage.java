package coinLeaderBoard;

public class CoinUpdateMessage {
    private int userId;
    private int newBalance;

    public CoinUpdateMessage(int userId, int newBalance) {
        this.userId = userId;
        this.newBalance = newBalance;
    }

    public int getUserId() {
        return userId;
    }

    public int getNewBalance() {
        return newBalance;
    }

    @Override
    public String toString() {
        return "CoinUpdateMessage{userId=" + userId + ", newBalance=" + newBalance + '}';
    }
}
