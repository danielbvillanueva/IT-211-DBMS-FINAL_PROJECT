public class User {
    private int userId;
    private String username;
    private int xp;
    private int completedChallenges;
    private String rank;
    private Database db;

    public User(int userId, String username, int xp, int completedChallenges, Database db) {
        this.userId = userId;
        this.username = username;
        this.xp = xp;
        this.completedChallenges = completedChallenges;
        this.rank = determineRank();
        this.db = db;
    }

    public void setXP(int xp) {
        this.xp = xp;
        this.rank = determineRank();
    }    

    public void saveUserData() {
        db.updateUserXP(userId, xp);
        db.updateCompletedChallenges(userId, completedChallenges);
    }

    private String determineRank() {
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

    public void completeChallenge(Database db) {
        this.completedChallenges++; 
        db.updateCompletedChallenges(userId, this.completedChallenges); 
    }

    public void displayProfile() {
        System.out.println("\n=====================================");
        System.out.println("           User Profile");
        System.out.println("=====================================");
        System.out.println("Username: " + this.getUsername());
        System.out.println("XP: " + this.getXP());
        System.out.println("Rank: " + this.getRank());
        System.out.println("Completed Challenges: " + this.getCompletedChallenges());
        System.out.println("=====================================");
    }

    public void addXP(int points, Database db) {
        this.xp += points; 
        db.updateUserXP(this.userId, this.xp); 
        this.rank = determineRank(); 
    }
    
    public void refreshUserDetails() {
        this.xp = db.getUserXP(userId); 
        this.completedChallenges = db.getCompletedChallenges(userId); 
        this.rank = determineRank(); 
    }

    public void logout() {
        db.updateUserXP(this.userId, this.xp);
        db.updateCompletedChallenges(this.userId, this.completedChallenges);
    }

    public void completeChallenge(int challengeXp, Database db) {
        this.completedChallenges++; 
        this.xp += challengeXp; 
        db.updateUserXP(this.userId, this.xp); 
        db.updateCompletedChallenges(this.userId, this.completedChallenges);
        this.rank = determineRank(); 
    }
    
    public void setCompletedChallenges(int completedChallenges) {
        this.completedChallenges = completedChallenges;
    }

    public String getRank() {
        return rank;
    }

    public int getXP() {
        return xp;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public int getCompletedChallenges() {
        return completedChallenges;
    }

}
