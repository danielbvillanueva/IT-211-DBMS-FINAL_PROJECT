import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Challenges {
    private User user;
    private Database db;

    public Challenges(User user, Database db) {
        this.user = user;
        this.db = db;
    }

    public static class Challenge {
        private int challengeId;
        private String challengeName;
        private int xpReward;
        private String difficulty;
        private int createdByUserId;
        private String challengeDescription; 
    
        public Challenge(int challengeId, String challengeName, int xpReward, String difficulty, int createdByUserId) {
            this.challengeId = challengeId;
            this.challengeName = challengeName;
            this.xpReward = xpReward;
            this.difficulty = difficulty;
            this.createdByUserId = createdByUserId;
            this.challengeDescription = "";
        }
  
        public Challenge(int challengeId, String challengeName, int xpReward, String difficulty, int createdByUserId, String challengeDescription) {
            this.challengeId = challengeId;
            this.challengeName = challengeName;
            this.xpReward = xpReward;
            this.difficulty = difficulty;
            this.createdByUserId = createdByUserId;
            this.challengeDescription = challengeDescription;
        }

        public int getChallengeId() {
            return challengeId;
        }

        public String getChallengeName() {
            return challengeName;
        }

        public int getXpReward() {
            return xpReward;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public int getCreatedByUserId() {
            return createdByUserId;
        }
        
        public String getChallengeDescription() {
            return challengeDescription;
        }
    
        public void setChallengeDescription(String challengeDescription) {
            this.challengeDescription = challengeDescription;
        }
    }

    public void displayChallengesMenu(Scanner scanner) {
        while (true) {
            System.out.println(" -----------------------------------------");
            System.out.println("|           Challenges Menu:              |");
            System.out.println(" -----------------------------------------");
            System.out.println("|           1. Available Challenges       |");
            System.out.println("|           2. Join User Challenges       |");
            System.out.println("|           3. Create Your Own Challenges |");
            System.out.println("|           4. Go back to Dashboard       |");
            System.out.println(" -----------------------------------------");
            System.out.print(" Please choose an option: ");
            int choice = scanner.nextInt();
    
            switch (choice) {
                case 1:
                    displayAvailableChallenges(scanner);
                    break;
                case 2:
                    displayJoinUserChallenges(scanner);
                    break;
                case 3:
                    createUserChallenge(scanner);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayAvailableChallenges(Scanner scanner) {
        while (true) {
            System.out.println(" -----------------------------------------");
            System.out.println("|           Available Challenges          |");
            System.out.println(" -----------------------------------------");
            System.out.println("|           1. Main Challenges           |");
            System.out.println("|           2. Joined Challenges         |");
            System.out.println("|           3. Go back                   |");
            System.out.println(" -----------------------------------------");
            System.out.print(" Please choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displayMainChallenges(scanner);
                    break;
                case 2:
                    displayJoinedChallenges(scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMainChallenges(Scanner scanner) {
        List<Challenge> mainChallenges = getMainChallenges();
        System.out.println(" -----------------------------------------");
        System.out.println("|           Main Challenges              |");
        System.out.println(" -----------------------------------------");
    
        while (true) {
            for (int i = 0; i < mainChallenges.size(); i++) {
                Challenge currentChallenge = mainChallenges.get(i);
                System.out.printf("%d. %s (XP: %d, Difficulty: %s)\n", 
                    i + 1, currentChallenge.getChallengeName(), currentChallenge.getXpReward(), currentChallenge.getDifficulty());
            }
    
            System.out.println();
            System.out.print("Select a challenge to complete (1, 2, etc.) or 0 to go back: ");
            int choice = scanner.nextInt();
    
            if (choice > 0 && choice <= mainChallenges.size()) {
                Challenge completedChallenge = mainChallenges.get(choice - 1);
                completeChallenge(completedChallenge, scanner);
    
                Challenge newChallenge = generateNewChallenge(completedChallenge.getDifficulty());
                mainChallenges.set(choice - 1, newChallenge);
                System.out.println(" ------------------------------------------------");
                System.out.println("|  Challenge completed! New challenge generated. |");
                System.out.println(" ------------------------------------------------");
    
            } else if (choice == 0) {
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayJoinedChallenges(Scanner scanner) {
        List<Challenge> joinedChallenges = db.getUserJoinedChallenges(user.getUserId());
        System.out.println(" -----------------------------------------");
        System.out.println("|           Joined Challenges            |");
        System.out.println(" -----------------------------------------");

        if (joinedChallenges.isEmpty()) {
            System.out.println("No joined challenges available.");
        } else {
            for (int i = 0; i < joinedChallenges.size(); i++) {
                Challenge challenge = joinedChallenges.get(i);
                String createdByUsername = db.getUsernameByUserId(challenge.getCreatedByUserId());
                String description = challenge.getChallengeDescription().isEmpty() ? "(No description)" : challenge.getChallengeDescription();
                System.out.printf("%d. %s - Description: %s (XP: %d, Created by %s)\n", 
                    i + 1, challenge.getChallengeName(), description, challenge.getXpReward(), createdByUsername);
            }
        }
        
        System.out.println();
        System.out.print("Select a challenge to complete (1, 2, etc.) or 0 to go back: ");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= joinedChallenges.size()) {
            Challenge selectedChallenge = joinedChallenges.get(choice - 1);
            completeChallenge(selectedChallenge, scanner);
            System.out.println("Challenge completed! XP awarded: " + selectedChallenge.getXpReward());
        } else if (choice != 0) {
            System.out.println("Invalid choice. Returning to menu.");
        }
    }

    private List<Challenge> getMainChallenges() {
        List<Challenge> mainChallenges = new ArrayList<>();
        mainChallenges.add(new Challenge(1, "Reduce Plastic Use for a Week", 10, "easy", 0));
        mainChallenges.add(new Challenge(2, "Compost Food Waste for a Week", 20, "medium", 0));
        mainChallenges.add(new Challenge(3, "Go Car-Free for a Week", 50, "hard", 0));
        return mainChallenges;
    }

    private Challenge generateNewChallenge(String difficulty) {
        switch (difficulty) {
            case "easy":
                return new Challenge(4, "Go Meatless for a Day", 10, "easy", 0);
            case "medium":
                return new Challenge(5, "Participate in a Community Cleanup", 25, "medium", 0);
            case "hard":
                return new Challenge(6, "Reduce Home Energy Use by 20%", 40, "hard", 0);
            default:
                return null;
        }
    }

    private void completeChallenge(Challenge challenge, Scanner scanner) {
        user.addXP(challenge.getXpReward(), db); 
        user.completeChallenge(db); 
        db.updateUserXP(user.getUserId(), user.getXP());  
        db.incrementCompletedChallenges(user.getUserId(), 1); 
        db.removeUserChallenge(user.getUserId(), challenge.getChallengeId());
    }

    private void displayJoinUserChallenges(Scanner scanner) {
        List<Challenge> userChallenges = db.getAvailableChallenges(); 
        System.out.println(" ----------------------------------------------------");
        System.out.println("|           User-Created Challenges                 |");
        System.out.println(" ----------------------------------------------------");
    
        if (userChallenges.isEmpty()) {
            System.out.println("No challenges available.");
        } else {
            for (int i = 0; i < userChallenges.size(); i++) {
                Challenge challenge = userChallenges.get(i);
                String createdByUsername = db.getUsernameByUserId(challenge.getCreatedByUserId());
                String description = challenge.getChallengeDescription().isEmpty() ? "(No description)" : challenge.getChallengeDescription();
                System.out.printf("%d. %s - Description: %s (XP: %d, Created by %s)\n", 
                    i + 1, challenge.getChallengeName(), description, challenge.getXpReward(), createdByUsername);
            }
        }
    
        System.out.println("--------------------------------------------------------------------------");
        System.out.print("\nSelect a challenge to join (1, 2, etc.) or 0 to go back: ");
        int choice = scanner.nextInt();
    
        if (choice > 0 && choice <= userChallenges.size()) {
            Challenge selectedChallenge = userChallenges.get(choice - 1);
    
            // Check if the user is already part of the challenge
            boolean alreadyJoined = db.isUserAlreadyJoinedChallenge(user.getUserId(), selectedChallenge.getChallengeId());
            if (alreadyJoined) {
                System.out.println("You are already participating in this challenge.");
            } else {
                // Join the challenge and show success message
                db.addUserToChallenge(user.getUserId(), selectedChallenge.getChallengeId());
                System.out.println("You have successfully joined the challenge: " + selectedChallenge.getChallengeName());
            }
        } else if (choice != 0) {
            System.out.println("Invalid choice. Returning to menu.");
        }
    }
    
    private void createUserChallenge(Scanner scanner) {
        scanner.nextLine();  // Consume newline
        System.out.print("Enter challenge name: ");
        String challengeName = scanner.nextLine();
        System.out.print("Enter challenge description: ");
        String challengeDescription = scanner.nextLine(); 
        int xpReward = 20;
    
        Challenge newChallenge = new Challenge(0, challengeName, xpReward, "user-created", user.getUserId(), challengeDescription);
        db.createChallenge(newChallenge.getChallengeName(), newChallenge.getChallengeDescription(), newChallenge.getXpReward(), user.getUserId());
        System.out.println("Challenge created successfully!");
    }
}