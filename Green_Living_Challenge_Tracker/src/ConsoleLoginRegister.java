import java.util.Scanner;

public class ConsoleLoginRegister {
    private Database db;
    private User currentUser;

    public ConsoleLoginRegister() {
        db = new Database();
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                showLoginMenu();
                int choice = getUserInput(scanner);

                switch (choice) {
                    case 1:
                        login(scanner);
                        break;
                    case 2:
                        register(scanner);
                        break;
                    case 3:
                        System.out.println(" ==========================================");
                        System.out.println("|           Exiting... Goodbye!          |");
                        System.out.println(" ==========================================");
                        return;
                    default:
                        System.out.println("Invalid choice! Please choose a valid option (1, 2, or 3).");
                }
            }
        }
    }

    private void showLoginMenu() {
        System.out.println(" ==========================================");
        System.out.println("|        User Login / Registration        |");
        System.out.println(" ==========================================");
        System.out.println("|           1. Login                      |");
        System.out.println("|           2. Register                   |");
        System.out.println("|           3. Exit                       |");
        System.out.println(" ==========================================");
        System.out.print(" Please choose an option: ");
    }

    private void showUserDashboardMenu() {
        System.out.println("\n ==========================================");
        System.out.println("|           User Dashboard                |");
        System.out.println(" ==========================================");
        System.out.println("|           1. View Profile               |");
        System.out.println("|           2. View Challenges            |");
        System.out.println("|           3. View Leaderboard           |");
        System.out.println("|           4. Logout                     |");
        System.out.println(" ==========================================");
        System.out.print(" Please choose an option: ");
    }

    private int getUserInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input! Please enter a valid number: ");
            scanner.nextLine();  
        }
        return scanner.nextInt();
    }

    public void register(Scanner scanner) {
        System.out.println("\n ==========================================");
        System.out.println("|           Registration Form              |");
        System.out.println(" ==========================================");

        scanner.nextLine();
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();
        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        if (db.registerUser(username, password)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed. The username might already exist.");
        }
    }

    public void login(Scanner scanner) {
        System.out.println("\n ==========================================");
        System.out.println("|           Login Form                   |");
        System.out.println(" ==========================================");

        scanner.nextLine(); 
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        String storedPassword = db.getPassword(username);
        if (storedPassword == null) {
            System.out.println("User not found. Please register first.");
        } else if (storedPassword.equals(password)) {
            int userId = db.getUserId(username);
            int xp = db.getUserXP(userId);
            int completedChallenges = db.getCompletedChallenges(userId);
            User user = new User(userId, username, xp, completedChallenges, db);

            user.refreshUserDetails();

            System.out.println("Login successful! Welcome, " + username + "!");
            displayUserDashboard(user, scanner);
        } else {
            System.out.println("Incorrect password. Please try again.");
        }
    }

    private void displayUserDashboard(User user, Scanner scanner) {
        Challenges challenges = new Challenges(user, db);
        Leaderboard leaderboard = new Leaderboard(db);

        while (true) {
            showUserDashboardMenu();
            int choice = getUserInput(scanner);

            switch (choice) {
                case 1:
                    user.displayProfile(); 
                    break;
                case 2:
                    challenges.displayChallengesMenu(scanner); 
                    break;
                case 3:
                    leaderboard.showLeaderboard(); 
                    break;
                case 4:
                    user.logout(); 
                    System.out.println("Successfully logged out.");
                    return;
                default:
                    System.out.println("Invalid choice! Please select an option between 1 and 4.");
            }
        }
    }

    public void logout() {
        if (currentUser != null) {
            currentUser.logout(); 
            System.out.println("Logged out successfully.");
            currentUser = null; 
        }
    }
}
