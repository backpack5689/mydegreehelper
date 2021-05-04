# mydegreehelper

# Database
1. In a VM or a Hosted Server, install MySQL
2. Create the database with the following specifications
```mySQL
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `user_fname` varchar(255) DEFAULT NULL,
  `user_lname` varchar(255) DEFAULT NULL,
  `user_email` varchar(255) NOT NULL,
  `user_password` varchar(255) NOT NULL,
  `user_progress` json DEFAULT NULL,
  `user_type` int DEFAULT NULL,
  `degree_id` int DEFAULT NULL,
  `user_username` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`)
);
CREATE TABLE `degree` (
  `degree_id` int NOT NULL AUTO_INCREMENT,
  `degree_name` varchar(255) NOT NULL,
  `degree_location` varchar(255) NOT NULL,
  `degree_coursehours` int NOT NULL,
  `degree_applied` date NOT NULL,
  `degree_object` json DEFAULT NULL,
  PRIMARY KEY (`degree_id`)
 );

```
# API-Server

# Android Studio
1. Install android studio
2. Clone the repo and open the project
3. Navigate to API.java and change the ip in the constant URL to reflect your database IP.
4. Once your API server and Database have been setup launch the Android Emulator and sign up a user to begin.
5. Upon login they will be routed to the catalogue page. You will need to upload templates from the Templates folder in the repo onto the device storage and use them via the "+" button dialogue to add them to the database. Then the user can apply an uploaded template and progress through the degree content.
