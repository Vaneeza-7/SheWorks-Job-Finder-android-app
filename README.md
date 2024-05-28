# SheWorks - Job Finder App for Women

SheWorks is an android mobile application designed to empower women in their career development by providing a curated job board, a supportive community forum, events and connection opportunities. This app helps women create professional profiles, connect with employers, engage in a community, and discover career opportunities.


![figma design](https://github.com/Vaneeza-7/SheWorks-Job-Finder-android-app/blob/master/figma_design.jpg)

## Table of Contents
- [Overview](#overview)
- [Key Features](#key-features)
- [Screens](#screens)
- [Firebase Integration](#firebase-integration)
- [Web APIs, MySQL, and PHP Backend](#web-apis-mysql-and-php-backend)
- [Android Implementation](#android-implementation)
- [Getting Started](#getting-started)
- [License](#license)

## Overview
The current professional landscape often presents unique challenges for women seeking meaningful career opportunities and connections. SheWorks aims to address this gap by empowering women through a mobile application that combines a curated job board, a supportive community forum, and mentorship opportunities.

### Goals
SheWorks application allows women to:
1. Create a professional profile to showcase skills, experience, and career aspirations.
2. Connect with experienced mentors for guidance and support.
3. Engage in a vibrant community to share experiences and discuss career-related topics.
4. Discover relevant career opportunities from companies committed to diversity and inclusion.

## Key Features
1. **Personalized Profiles:** Showcase skills, experience, and career aspirations to attract ideal opportunities.
2. **Curated Job Board:** Filter listings from companies committed to diversity and inclusion, aided by personalized recommendations.
3. **Mentorship Hub:** Connect with experienced women for guidance and support.
4. **Vibrant Community Forum:** Discuss career-related topics, share experiences, and build connections with other SheWorks users.
5. **Inspiring Events Calendar:** Discover workshops, networking events, and conferences to enhance career development.
6. **Direct Messaging:** Network directly with employers and mentors for inquiries and potential opportunities.
7. **Job Alerts:** Stay updated on relevant job openings through personalized notifications.

### Optional Features (not implemented yet)
- **Resource Library:** Access articles, webinars, and guides on career development and professional growth.
- **Skill Badges and Endorsements:** Showcase expertise and gain credibility through verified badges and endorsements.

## Screens
The application includes the following screens:
- Splash Screen
- Login and Signup Screens
- Password Reset and Verification Screens
- User Feed, Post and Comments Screens
- Job Board, Job Discovery Screens
- Job Application Screens
- Event Calendar and Add Event Screens
- Chat and New Chat Screens
- User Career Profile and Edit Profile Screens
- Notifications, About Us, and Feedback Screens

## Firebase Integration
### Authentication
- **Firebase Authentication:** Used for user sign up, login, and password management.
- **Email/Password Authentication:** Ensures secure user management.

### Realtime Database
- **Firebase Realtime Database:** Stores user messages.
- Real-time updates ensure that users have the latest information and notifications.

### Storage
- **Firebase Storage:** Securely stores profile pictures, event images, and other media files.
- Ensures scalable and secure storage for user-generated content.

### Cloud Messaging
- **Firebase Cloud Messaging (FCM):** Sends push notifications for new messages, job alerts, and event reminders.
- Notifications are also displayed within the app's notification screen.

## Web APIs, MySQL, and PHP Backend
- **Web APIs:** Used for saving and retrieving data, excluding media which is handled by Firebase Storage.
- **MySQL Database:** Stores user data, job listings, event details, and other relevant information.
- **PHP Backend:** Handles API requests, processes data, and interacts with the MySQL database to perform CRUD operations.

## Android Implementation
### User Interface
- **Material Design:** Ensures a clean, modern, and intuitive user experience.
- **Profile Screen:** Allows users to update their profile picture, cover photo, and personal information.
- **Job Discovery:** Users can explore job listings and apply directly through the app.
- **Chat System:** Supports real-time messaging and networking with mentors and employers.

### Libraries Used
- **[Glide](https://github.com/bumptech/glide):** For image caching and loading, ensuring offline accessibility.
- **[Agora SDK](https://www.agora.io/en/):** For implementing video and audio call functionalities.
- **[10clouds/Fluid Bottom Navigation](https://github.com/10clouds/FluidBottomNavigation-android):** For implementing an animated bottom navigation.
- **[applandeo/Material Calendar View](https://github.com/Applandeo/Material-Calendar-View):** For implementing customizable events calendar with range date selection.
- **[robertlevonyan/CustomFloatingActionButton](https://github.com/robertlevonyan/custom-floating-action-button):** For creating round custom floating action button with icon.
- **[tbuonomo/Dots Indicator](https://github.com/tommybuonomo/dotsindicator):** For creating the worm hole dots indicator for carousel with view pager2.
- **Volley and okhttp3:** For making network requests.

### Key Functionalities
- **Search with Filters:** Helps users find job opportunities based on specific criteria.
- **Community Forum:** Enables discussions and networking among users.
- **Event Calendar:** Users can discover and register for career development events.
- **Offline Mode:** App functions properly offline, displaying cached data and syncing when back online. (to be implemented)

## Getting Started

### Prerequisites
- Android Studio Hedgehog or higher version.
- Firebase project set up.
- Agora API credentials (for video/audio calls).
- MySQL database setup.
- PHP backend server setup.

### Installation
1. **Clone the repository:**
    ```sh
    git clone https://github.com/Vaneeza-7/SheWorks-Job-Finder-android-app.git
    ```
2. **Open the project in Android Studio.**
3. **Connect to Firebase:**
    - Add your `google-services.json` file to the app directory.
    - Configure Firebase in your project settings.
4. **Add Agora API credentials:**
    - Follow the Agora API documentation to set up your credentials.
5. **Set up the PHP backend:**
    - Deploy the PHP backend server.
    - Ensure the API endpoints are accessible and correctly configured.
6. **Set up the MySQL database:**
    - Import the database schema and configure the database connection in the PHP backend.

### Running the App
- Open the project in Android Studio and run the app on an emulator or physical device.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
