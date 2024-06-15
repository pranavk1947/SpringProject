package coinLeaderBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardService {
    private static DashboardService instance;
    private LeaderboardService leaderboardService;
    private static final Logger logger = Logger.getLogger(DashboardService.class.getName());

    private DashboardService(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
        logger.info("DashboardService initialized");
    }

    public static DashboardService getInstance(LeaderboardService leaderboardService) {
        if (instance == null) {
            instance = new DashboardService(leaderboardService);
        }
        return instance;
    }

    public List<LeaderboardEntry> getTop10Customers() {
        List<LeaderboardEntry> top10List = new ArrayList<>();
        try {
            PriorityQueue<LeaderboardEntry> top10Queue = leaderboardService.getTop10Customers();
            top10List = new ArrayList<>(top10Queue);
            top10List.sort((e1, e2) -> Integer.compare(e2.getCoinBalance(), e1.getCoinBalance()));
            logger.info("Retrieved top 10 customers for dashboard");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving top 10 customers", e);
        }
        return top10List;
    }
}
