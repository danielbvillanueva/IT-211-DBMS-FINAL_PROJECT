import java.util.List;

public class Leaderboard {
    private Database db;

    public Leaderboard(Database db) {
        this.db = db;
    }

    public void showLeaderboard() {
        try {
            List<LeaderboardEntry> leaderboard = db.getLeaderboard();
            
            System.out.println(" ---------------------------------------------------------");
            System.out.println("|                Green Living Challenge Leaderboard        |");
            System.out.println(" ---------------------------------------------------------");
            System.out.println("| Place | Username               | Rank    | XP    | Completed Challenges |");
            System.out.println(" ---------------------------------------------------------------------------");

            int rankPosition = 1; 
            for (LeaderboardEntry entry : leaderboard) {
                String place = entry.getRank();
                String username = entry.getUsername();
                int xp = entry.getXp();
                int completedChallenges = entry.getCompletedChallenges();
                System.out.printf("| %-5d | %-22s | %-7s | %-6d | %-20d |\n", 
                                  rankPosition++, username, place, xp, completedChallenges);
            }

            System.out.println(" ---------------------------------------------------------------------------");

        } catch (Exception e) {
            System.out.println("Error retrieving leaderboard: " + e.getMessage());
        }
    }
}
