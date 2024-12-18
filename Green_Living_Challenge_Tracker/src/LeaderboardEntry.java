public class LeaderboardEntry {
    private String username;
    private int xp;
    private int completedChallenges;
    private String rank; 

    public LeaderboardEntry(String username, int xp, int completedChallenges) {
        this.username = username;
        this.xp = xp;
        this.completedChallenges = completedChallenges;
        this.rank = calculateRank(xp); 
    }

    private String calculateRank(int xp) {
        if (xp >= 1000) {
            return "Platinum";
        } else if (xp >= 500) {
            return "Gold";
        } else if (xp >= 100) {
            return "Silver";
        } else {
            return "Bronze";
        }
    }

    public String getUsername() {
        return username;
    }

    public int getXp() {
        return xp;
    }

    public int getCompletedChallenges() {
        return completedChallenges;
    }

    public String getRank() {
        return rank;
    }
}
