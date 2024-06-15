package coinLeaderBoard;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LeaderboardService implements MessageListener {
    private static LeaderboardService instance;
    private PriorityQueue<LeaderboardEntry> leaderboard;
    private Map<Integer, Integer> userBalances;
    private static final Logger logger = Logger.getLogger(LeaderboardService.class.getName());

    private LeaderboardService(Map<Integer, Integer> userBalances, MessageQueue messageQueue) {
        this.userBalances = userBalances;
        this.leaderboard = new PriorityQueue<>(Comparator.comparingInt(LeaderboardEntry::getCoinBalance));
        messageQueue.subscribe(this);
        logger.info("LeaderboardService initialized and subscribed to MessageQueue");
    }

    public static LeaderboardService getInstance(Map<Integer, Integer> userBalances, MessageQueue messageQueue) {
        if (instance == null) {
            instance = new LeaderboardService(userBalances, messageQueue);
        }
        return instance;
    }

    @Override
    public void onMessage(CoinUpdateMessage message) {
        try {
            int userId = message.getUserId();
            int newBalance = message.getNewBalance();

            leaderboard.removeIf(entry -> entry.getUserId() == userId);
            leaderboard.add(new LeaderboardEntry(userId, newBalance));
            logger.info("Updated leaderboard with new balance for user " + userId + ": " + newBalance);

            while (leaderboard.size() > 10) {
                LeaderboardEntry removedEntry = leaderboard.poll();
                logger.info("Removed user " + removedEntry.getUserId() + " from leaderboard to maintain top 10");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating leaderboard with message " + message, e);
        }
    }

    public PriorityQueue<LeaderboardEntry> getTop10Customers() {
        return new PriorityQueue<>(leaderboard);
    }
}

