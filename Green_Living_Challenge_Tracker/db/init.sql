-- Create the 'users' table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Insert initial users
INSERT INTO users (username, password) VALUES
('john_doe', 'password123'),
('jane_smith', 'securepassword'),
('paul_adams', 'mypassword');

-- Create the 'leaderboard' table
CREATE TABLE leaderboard (
    user_id INT PRIMARY KEY,
    xp INT DEFAULT 0,
    completed_challenges INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Insert initial leaderboard data
INSERT INTO leaderboard (user_id, xp, completed_challenges) VALUES
(1, 100, 5),
(2, 150, 8),
(3, 200, 10);

-- Create the 'challenges' table (predefined challenges)
CREATE TABLE challenges (
    challenge_id INT AUTO_INCREMENT PRIMARY KEY,
    challenge_name VARCHAR(100) NOT NULL,
    challenge_description TEXT NOT NULL,
    xp_reward INT NOT NULL,
    difficulty ENUM('Easy', 'Medium', 'Hard') NOT NULL,
    created_by_user_id INT,
    FOREIGN KEY (created_by_user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Insert predefined challenges
INSERT INTO challenges (challenge_name, challenge_description, xp_reward, difficulty, created_by_user_id) VALUES
('Reduce Plastic Use for a Week', 'Challenge to reduce plastic usage for a week', 10, 'Easy', NULL),
('Bike to Work for a Day', 'Challenge to bike to work instead of driving', 15, 'Easy', NULL),
('Go Meatless for a Day', 'Challenge to avoid meat consumption for a day', 10, 'Easy', NULL),
('Compost Food Waste for a Week', 'Challenge to compost food waste for a week', 20, 'Medium', NULL),
('Participate in a Community Cleanup', 'Challenge to participate in a community cleanup event', 25, 'Medium', NULL);

-- Create the 'user_challenges' table (user-created challenges)
CREATE TABLE user_challenges (
    challenge_id INT AUTO_INCREMENT PRIMARY KEY,
    challenge_name VARCHAR(100) NOT NULL,
    challenge_description TEXT NOT NULL,
    created_by_user_id INT NOT NULL,
    FOREIGN KEY (created_by_user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Insert some user-created challenges
INSERT INTO user_challenges (challenge_name, challenge_description, created_by_user_id) VALUES
('Host a Zero-Waste Event', 'Challenge to host an event focused on zero waste', 1),
('Create a Reusable Items Kit', 'Challenge to create a kit of reusable items', 2);

-- Create the 'available_challenges' table
CREATE TABLE available_challenges (
    challenge_id INT PRIMARY KEY,
    challenge_name VARCHAR(100) NOT NULL,
    challenge_description TEXT NOT NULL,
    xp_reward INT NOT NULL,
    difficulty VARCHAR(20),
    created_by_user_id INT,
    FOREIGN KEY (created_by_user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Insert data into available_challenges (combining predefined and user-created challenges)
INSERT INTO available_challenges (challenge_id, challenge_name, challenge_description, xp_reward, difficulty, created_by_user_id) 
SELECT challenge_id, challenge_name, challenge_description, xp_reward, difficulty, created_by_user_id FROM challenges
UNION ALL
SELECT challenge_id, challenge_name, challenge_description, 20, 'Friendly', created_by_user_id FROM user_challenges;

-- Create the 'user_challenge_participants' table (to track which users are participating in which challenges)
CREATE TABLE user_challenge_participants (
    participant_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    challenge_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (challenge_id) REFERENCES challenges(challenge_id) ON DELETE CASCADE
);

-- Insert some sample participants
INSERT INTO user_challenge_participants (user_id, challenge_id) VALUES
(1, 1),  -- User 1 participating in Challenge 1
(2, 2),  -- User 2 participating in Challenge 2
(3, 3);  -- User 3 participating in Challenge 3

