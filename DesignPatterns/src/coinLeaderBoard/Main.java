package coinLeaderBoard;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            // Simulated database for user balances
            Map<Integer, Integer> userBalances = new HashMap<>();

            // Create message queue singleton
            MessageQueue messageQueue = MessageQueue.getInstance();

            // Instantiate services using Singleton pattern
            CoinService coinService = CoinService.getInstance(userBalances, messageQueue);
            LeaderboardService leaderboardService = LeaderboardService.getInstance(userBalances, messageQueue);
            DashboardService dashboardService = DashboardService.getInstance(leaderboardService);

            // Simulate transactions
            logger.info("Simulating transactions...");
            coinService.creditCoins(1, 100);
            coinService.creditCoins(2, 200);
            coinService.creditCoins(3, 50);
            coinService.creditCoins(1, 150);
            coinService.creditCoins(2, 300);
            coinService.creditCoins(3, 400);
            coinService.creditCoins(4, 500);
            coinService.creditCoins(5, 600);
            coinService.creditCoins(6, 700);
            coinService.creditCoins(7, 800);
            coinService.creditCoins(8, 900);
            coinService.creditCoins(9, 1000);
            coinService.creditCoins(10, 1100);
            coinService.creditCoins(11, 1200);

            // Retrieve top 10 customers for dashboard
            List<LeaderboardEntry> topCustomers = dashboardService.getTop10Customers();
            topCustomers.forEach(entry -> logger.info(entry.toString()));
        } catch (Exception e) {
            logger.error("An error occurred in the main application", e);
        }
    }
}
