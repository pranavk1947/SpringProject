package coinLeaderBoard;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoinService {
    private static CoinService instance;
    private Map<Integer, Integer> userBalances;
    private MessageQueue messageQueue;
    private static final Logger logger = Logger.getLogger(CoinService.class.getName());

    private CoinService(Map<Integer, Integer> userBalances, MessageQueue messageQueue) {
        this.userBalances = userBalances;
        this.messageQueue = messageQueue;
        logger.info("CoinService initialized");
    }

    public static CoinService getInstance(Map<Integer, Integer> userBalances, MessageQueue messageQueue) {
        if (instance == null) {
            instance = new CoinService(userBalances, messageQueue);
        }
        return instance;
    }

    public void creditCoins(int userId, int amountSpent) {
        try {
            int coins = amountSpent / 10;
            int currentBalance = userBalances.getOrDefault(userId, 0);
            int newBalance = currentBalance + coins;
            userBalances.put(userId, newBalance);
            logger.info("Credited " + coins + " coins to user " + userId + ". New balance: " + newBalance);
            messageQueue.publish(new CoinUpdateMessage(userId, newBalance));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error crediting coins to user " + userId, e);
        }
    }
}
