import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/UserDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "staydead09";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public boolean registerUser(String username, String password) {
        String checkUserQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
        String insertUserQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
        String insertLeaderboardQuery = "INSERT INTO leaderboard (user_id, xp, completed_challenges) VALUES (?, 0, 0)";
    
        try (Connection conn = getConnection()) {
            try (PreparedStatement checkStmt = conn.prepareStatement(checkUserQuery)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }
    
            conn.setAutoCommit(false);
            try (PreparedStatement userStmt = conn.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, username);
                userStmt.setString(2, password);
                int affectedRows = userStmt.executeUpdate();
    
                if (affectedRows > 0) {
                    ResultSet generatedKeys = userStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
    
                        try (PreparedStatement leaderboardStmt = conn.prepareStatement(insertLeaderboardQuery)) {
                            leaderboardStmt.setInt(1, userId);
                            leaderboardStmt.executeUpdate();
                        }
    
                        conn.commit();
                        return true;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public String getPassword(String username) {
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {  
            e.printStackTrace();
        }
        return null;
    }

    public int getUserId(String username) {
        String query = "SELECT user_id FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
    }

    public int getUserXP(int userId) {
        String query = "SELECT xp FROM leaderboard WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("xp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; 
    }

    public int getCompletedChallenges(int userId) {
        int completedChallenges = 0;
        String query = "SELECT completed_challenges FROM leaderboard WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                completedChallenges = rs.getInt("completed_challenges");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return completedChallenges;
    }

    public void updateLeaderboardField(int userId, String field, int value) {
        String query = "UPDATE leaderboard SET " + field + " = " + field + " + ? WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, value);
            stmt.setInt(2, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementCompletedChallenges(int userId, int incrementValue) {
        updateLeaderboardField(userId, "completed_challenges", incrementValue);
    }

    public void updateUserXP(int userId, int xp) {
        String query = "UPDATE leaderboard SET xp = ? WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, xp);  
            stmt.setInt(2, userId);
            stmt.executeUpdate();  
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateCompletedChallenges(int userId, int challengeCount) {
        String query = "UPDATE leaderboard SET completed_challenges = ? WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, challengeCount);  
            stmt.setInt(2, userId);  
            stmt.executeUpdate();  
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<LeaderboardEntry> getLeaderboard() {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        String query = "SELECT u.username, l.xp, l.completed_challenges " +
                       "FROM users u " +
                       "LEFT JOIN leaderboard l ON u.user_id = l.user_id " +
                       "ORDER BY l.xp DESC, l.completed_challenges DESC";
    
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                String username = rs.getString("username");
                int xp = rs.getInt("xp");
                int completedChallenges = rs.getInt("completed_challenges");
                leaderboard.add(new LeaderboardEntry(username, xp, completedChallenges));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaderboard;
    }

    public List<Challenges.Challenge> getAvailableChallenges() {
        List<Challenges.Challenge> availableChallenges = new ArrayList<>();
    
        String predefinedQuery = "SELECT challenge_id, challenge_name, challenge_description, xp_reward, difficulty, created_by_user_id FROM challenges";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(predefinedQuery);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int challengeId = rs.getInt("challenge_id");
                String challengeName = rs.getString("challenge_name");
                String challengeDescription = rs.getString("challenge_description");
                int xpReward = rs.getInt("xp_reward");
                String difficulty = rs.getString("difficulty");
                int createdByUserId = rs.getInt("created_by_user_id");
    
                availableChallenges.add(new Challenges.Challenge(challengeId, challengeName, xpReward, difficulty, createdByUserId, challengeDescription));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String userChallengesQuery = "SELECT challenge_id, challenge_name, challenge_description, created_by_user_id FROM user_challenges";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(userChallengesQuery);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int challengeId = rs.getInt("challenge_id");
                String challengeName = rs.getString("challenge_name");
                String challengeDescription = rs.getString("challenge_description");
                int xpReward = 20; 
                String difficulty = "Friendly";
                int createdByUserId = rs.getInt("created_by_user_id");
    
                availableChallenges.add(new Challenges.Challenge(challengeId, challengeName, xpReward, difficulty, createdByUserId, challengeDescription));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return availableChallenges;
    }
    
    public void incrementUserXP(int userId, int xpToAdd) {
        String query = "UPDATE leaderboard SET xp = xp + ? WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, xpToAdd);
            stmt.setInt(2, userId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("XP updated for user ID: " + userId + ", XP added: " + xpToAdd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateUserProfile(int userId, int xp, int completedChallenges) {
        this.updateUserXP(userId, xp); 
        this.updateCompletedChallenges(userId, completedChallenges);
    
        List<LeaderboardEntry> leaderboard = this.getLeaderboard(); 
    
        System.out.println("Updated Leaderboard:");
        for (LeaderboardEntry entry : leaderboard) {
            System.out.println(entry.getUsername() + " - XP: " + entry.getXp() + " - Completed Challenges: " + entry.getCompletedChallenges());
        }
    }

    public void completeChallenge(int userId, int challengeXP) {
        try (Connection conn = this.getConnection()) {
            conn.setAutoCommit(false);  

            int currentXP = this.getUserXP(userId);
            int completedChallenges = this.getCompletedChallenges(userId);
    
            System.out.println("Current XP: " + currentXP + ", Completed Challenges: " + completedChallenges);
            int newXP = currentXP + challengeXP; 
            int newCompletedChallenges = completedChallenges + 1; 
    
            this.updateLeaderboardField(userId, "xp", newXP);
            this.updateLeaderboardField(userId, "completed_challenges", newCompletedChallenges);
  
            System.out.println("New XP: " + newXP + ", New Completed Challenges: " + newCompletedChallenges);

            conn.commit();
            this.updateUserProfile(userId, newXP, newCompletedChallenges);
    
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = this.getConnection()) {
                conn.rollback();  
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean isUserAlreadyJoinedChallenge(int userId, int challengeId) {
        String query = "SELECT * FROM user_challenges WHERE user_id = ? AND challenge_id = ?";
        Connection conn = null;
        try {
            conn = getConnection();  
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, challengeId);
                ResultSet rs = stmt.executeQuery();
                boolean alreadyJoined = rs.next();  
                System.out.println("Checked if user " + userId + " is in challenge " + challengeId + ": " + alreadyJoined);
                return alreadyJoined;
            } catch (SQLException e) {
                System.out.println("Error preparing or executing query: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error getting database connection: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.close();  
                }
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    public void addUserToChallenge(int userId, int challengeId) {
        String checkQuery = "SELECT COUNT(*) FROM user_challenge_participants WHERE user_id = ? AND challenge_id = ?";
        try (Connection conn = getConnection(); // Use the correct connection
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, challengeId);
    
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("You are already participating in this challenge.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        String insertQuery = "INSERT INTO user_challenge_participants (user_id, challenge_id) VALUES (?, ?)";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, challengeId);
            stmt.executeUpdate();
            System.out.println("Successfully joined the challenge!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String getUsernameByUserId(int userId) {
        String query = "SELECT username FROM users WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {  
            e.printStackTrace();
        }
        return "Unknown";  
    }

    public int createChallenge(String challengeName, String challengeDescription, int xpReward, int userId) {
        try (Connection conn = getConnection()) { 
            String query = "INSERT INTO challenges (challenge_name, challenge_description, xp_reward, created_by_user_id) VALUES (?, ?, ?, ?)";
            
            try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, challengeName);
                stmt.setString(2, challengeDescription);
                stmt.setInt(3, xpReward);
                stmt.setInt(4, userId);
        
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void joinUserChallenge(int userId, int challengeId) {
        if (isUserAlreadyJoinedChallenge(userId, challengeId)) {
            System.out.println("You are already participating in this challenge.");
            return; 
        } else {
            String query = "INSERT INTO user_challenges (user_id, challenge_id) VALUES (?, ?)";
            try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, challengeId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("You have successfully joined the challenge.");
                } else {
                    System.out.println("Error: Could not join the challenge.");
                }
            } catch (SQLException e) {
                System.out.println("Error joining the challenge: " + e.getMessage());
            }
        }
    }
    
    public List<Challenges.Challenge> getUserJoinedChallenges(int userId) {
        List<Challenges.Challenge> userChallenges = new ArrayList<>();
        String query = "SELECT c.challenge_id, c.challenge_name, c.challenge_description, c.xp_reward, c.difficulty, c.created_by_user_id " +
                       "FROM challenges c " +
                       "JOIN user_challenge_participants ucp ON c.challenge_id = ucp.challenge_id " +
                       "WHERE ucp.user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);  
            ResultSet rs = stmt.executeQuery();
           
            if (!rs.next()) {
                System.out.println();
            } else {
                do {
                    int challengeId = rs.getInt("challenge_id");
                    String challengeName = rs.getString("challenge_name");
                    String challengeDescription = rs.getString("challenge_description");
                    int xpReward = rs.getInt("xp_reward");
                    String difficulty = rs.getString("difficulty");
                    int createdByUserId = rs.getInt("created_by_user_id");
    
                    userChallenges.add(new Challenges.Challenge(challengeId, challengeName, xpReward, difficulty, createdByUserId, challengeDescription));
                } while (rs.next());  
            }
        } catch (SQLException e) {
            System.err.println("Error fetching challenges for user ID: " + userId);
            e.printStackTrace(); 
        }
        
        return userChallenges;
    }
    
    public void removeUserChallenge(int userId, int challengeId) {
        String query = "DELETE FROM user_challenges WHERE user_id = ? AND challenge_id = ?";
        try (Connection connection = getConnection(); 
            PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, userId);
            statement.setInt(2, challengeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error removing challenge: " + e.getMessage());
        }
    }

}

