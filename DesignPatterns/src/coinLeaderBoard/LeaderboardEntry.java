package coinLeaderBoard;

public class LeaderboardEntry {
    private int userId;
    private int coinBalance;

    public LeaderboardEntry(int userId, int coinBalance) {
        this.userId = userId;
        this.coinBalance = coinBalance;
    }

    public int getUserId() {
        return userId;
    }

    public int getCoinBalance() {
        return coinBalance;
    }

    @Override
    public String toString() {
        return "UserID: " + userId + ", Coins: " + coinBalance;
    }
}

