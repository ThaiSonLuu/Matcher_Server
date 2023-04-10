# MATCHER SERVER

## Feature
 - Database
   - users (User.class)
   - messages (MatcherMessage.class)
 - API
   - User: 
     - Get user (GET)
     - Sig up user (POST)
     - Log in user (POST)
     - Update user (PUT)
     - Change password (PUT)
   - Message
     - Get messages (GET)
   - File(image)
     - Read image (GET)
     - Upload image profile (POST)
     - Upload image message (POST)
 - Web socket
   - Match
     - Find
     - Confirm
   - Send message
   - Cancel match
 - Firebase Cloud Message - FCM
   - Push notification when send message and cancel match 
 - Security
    - Not require token(jwt): Sign up, Log in, Read image
    - Require token(jwt): the rest

## How to run
### Manner 1. Use docker (recommend)
 - Step 1: Download docker
 - Step 2: Pull mysql image
   ```text
   docker pull mysql:8.0.32
   ```
 - Step 3: Pull matcher image
   ```text
   docker pull sanryoo/matcher:v1.0.0
   ```
   To check all images was pulled
   ```text
   docker images
   ```
 - Step 4: Run mysql in a container to create database
   ```text
   docker run --name matcher-database \
   -dp 3309:3306 \
   -e MYSQL_ROOT_PASSWORD=matcher \
   -e MYSQL_USER=matcher \
   -e MYSQL_PASSWORD=matcher \
   -e MYSQL_DATABASE=matcher \
   mysql:8.0.32
   ```
 - Step 5: Run matcher in a container
   ```text
   docker run --name matcher-server \
   --link matcher-database:mysql \
   -dp 8088:8088 \
   sanryoo/matcher:v1.0.0
   ```
   To check all container
   ```text
   docker ps -a
   ```
   To work with database
   ```text
   docker exec -it matcher-database mysql -u matcher -p
   ```
   then enter password: **matcher**
   
   Example:
   ```text
   show databases;
   use matcher;
   select id, username from users;
   ```
   To see file in server
   ```text
   docker exec -it matcher-server bash
   ```
### Manner 2. Use terminal or IDE
 - Step 1: clone code
   ```text
   git clone https://github.com/SanRyoo/Matcher_Server.git
   ```
 - Step 2: Create database matcher in your localhost
 - Step 3: Go to application.properties
   Change url to your database url
   ```text
   spring.datasource.url=jdbc:mysql://localhost:<your_database_port>/matcher
   spring.datasource.username=<your username>
   spring.datasource.password=<your password>
   ```
 - Step 4: Download openjdk 18
 - Step 5: Run project