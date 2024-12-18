# Green Living Challenge Tracker

<img src="https://raw.githubusercontent.com/danielbvillanueva/Green-Living-Challenge-Tracker/main/green2-single-cropped-01.png" alt="Green Living Challenge Tracker" style="width: 600px; background-color: transparent;">

A console-based application that encourages sustainable living by tracking user participation in various environmental challenges.

## Table of Contents
1. [Project Overview](#-project-overview)  
2. [Objectives](#-objectives)  
3. [Features](#-features)  
4. [SDGs Connection](#-sdgs-connection)  
5. [Programming Principles](#-programming-principles)  
6. [Technologies Used](#backend)  
7. [Future Enhancement](#-future-enhancements)  
8. [Contributors](#-contributors)  

## 🌍 Project Overview
The Green Living Challenge Tracker is designed to help users engage in and track their participation in environmental challenges. The platform motivates users to complete challenges that promote sustainability, such as reducing plastic use or volunteering for environmental causes. It also includes a leaderboard to foster friendly competition and reward progress with experience points (XP).

## 🎯 Objectives
- **Sustainability Awareness:** Encourage users to adopt more sustainable habits through interactive challenges.
  
- **Progress Tracking:** Track XP and completed challenges for each user.
  
- **Leaderboard System:** Rank users based on their XP to encourage participation and completion of challenges.  

## 🚀 Features
- User Registration and Login Authentication. 🔐  
- Dashboard with user profile, rank, and XP. 📊  
- Join, create, and view environmental challenges. 🌱  
- Challenge completion tracking and XP awarding. 🎉  
- Leaderboard showing top-ranked users based on XP. 🏆  
- Option to create personalized challenges and join predefined ones. 🔧  
- User profiles displaying completed challenges and current rank. 🧑‍💻  

## 🌏 SDGs Connection
This project supports several **United Nations Sustainable Development Goals (SDGs):**

- **SDG 12: Responsible Consumption and Production**  
  Encourages users to reduce waste, adopt sustainable habits, and track progress in reducing their ecological footprint.  

- **SDG 13: Climate Action**  
  Motivates participation in activities like tree planting, energy conservation, and community cleanups to combat climate change.  

- **SDG 17: Partnerships for the Goals**  
  Facilitates collaboration among individuals and groups to collectively work towards achieving sustainability.  

## 🧩 Programming Principles
This project adheres to fundamental object-oriented programming principles:

- **Encapsulation** 🛡️: Private fields in classes like `User` and `Challenge` protect data integrity, with getters and setters controlling access.
  
- **Inheritance** 🪜: `SpecialChallenge` extends `Challenge`, reusing properties and methods to demonstrate a hierarchical relationship.
  
- **Polymorphism** 🎭:  
  - **Method Overriding:** `SpecialChallenge` customizes the `displayDetails()` method to display unique outputs for completed challenges.  
  - **Method Overloading:** In the `User` class, methods such as `addXP()` handle varying parameters for flexibility.
    
- **Abstraction** 🎨: The `ChallengeBase` interface defines shared methods like `completeChallenge()`, ensuring implementation consistency across `Challenge` and `SpecialChallenge`.  

## ⚙️ Technologies Used
### Backend
- **Programming Language:** Java
  
- **Database:** MySQL for storing user and challenge data.
  
- **Data Structures:** ArrayList for user and challenge management.
  
- **Error Handling:** Implemented error handling for system stability and user experience.  

### 👥 User Interface
- **Console-based:** Provides an interactive command-line interface for users to engage with the system.

## 🚀 Future Enhancements

- **Mobile Application:** Develop a mobile version for broader accessibility.

- **Gamification Features:** Introduce badges and achievements to increase user engagement.

- **Social Integration:** Allow users to share progress and challenges on social media.

- **AI Suggestions:** Use AI to recommend personalized challenges based on user behavior.

## 👷 Contributors
| Name                    | Role       | Email                          | Other Contacts        |  
|-------------------------|------------|--------------------------------|-----------------------|  
| Daniel B. Villanueva    | Developer  | 23-01037@g.batstate-u.edu.ph   | +63 9271646563        |  

## 📘 Course  
- IT 211 - Database Management System

## 🧑‍🏫 Course Facilitator  
- Owen Patrick Falculan
