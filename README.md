## Project Startup Instructions

Copy and run the following commands in your terminal:

```bash
git clone https://github.com/PhoXinuS/YapYap-Communicator.git
cd pap2024z-z06
docker compose build # Docker is required to run the project
docker compose up
```
After executing these commands, open your browser and go to: [http://localhost/](http://localhost/)

## Team Members:
-   Dominik Śledziewski  
-   Jan Szwagierczak  
-   Piotr Szkoda  
-   Tomasz Okoń  

# Initial Documentation - Text Messenger

## Project Description  
The Text Messenger application allows users to exchange messages in real-time. By leveraging WebSocket technology, communication occurs without delays, enabling instant message viewing and sending.

## Features  
- **User Management**  
  - Registration ✅  
  - Login ✅  
  - Viewing user information ✅  
  - Editing username ✅  

- **Conversations**  
  - Viewing conversation list ✅  
  - Creating conversations (including group chats) ✅  
  - Expanding conversations ✅  
  - Leaving conversations ✅  
  - Viewing conversation members ✅  
  - Sending messages ✅  
  - Receiving messages ✅  
  - Reacting to messages (like) ✅  

- **Friends Management**  
  - Viewing friend list ✅  
  - Removing friends ✅  
  - Sending friend requests via email/ID ✅  
  - Accepting/rejecting friend requests ✅  

- **Posts & Interactions**  
  - Creating posts (similar to Facebook) ✅  
  - Commenting on posts ✅  
  - Reacting to posts (like) ✅  
  - Deleting posts ✅  

# Technology Stack  

## Frontend  
### Framework: React.js  
- **Core**: UI library  
- **State Management**:  
  - Context API  
  - React Hooks  
- **Routing**: React Router  
- **WebSocket**: STOMP over SockJS client  
- **HTTP Client**: Axios  
- **Development Port**: 3000  

## Backend  
### Framework: Spring Boot (Java)  
- **Security**: Spring Security with JWT  
- **API**: Spring Web (REST)  
- **WebSocket**:  
  - Protocol: STOMP  
  - Fallback: SockJS  
  - Broker: Simple Broker  
  - Endpoint: `/ws`  

## Database  
### Oracle Database  
- **ORM**: JPA/Hibernate  
- **Connection**: JDBC  
- **Queries**: Native SQL  

## Developer Tools  
- **Containerization**: Docker  
- **Version Control**: Git  
- **CI/CD**: GitLab, DigitalOcean  
- **Package Management**:  
  - Frontend: npm/yarn  
  - Backend: Maven  

## Architecture  
- **Pattern**: MVC  
- **Communication**:  
  - REST API  
  - WebSocket (real-time)  
- **Authorization**: JWT  

## Role Distribution  
- **Dominik** - Backend  
- **Piotr** - CI/CD, Server  
- **Tomasz** - Frontend & Database  
- **Jan** - Testing  

## Initial Database Design  

![1](DB/Schemat.png)  

## Initial UI Design  

### Home Page  
![2](UI/1.png)  

### Registration Panel  
![3](UI/2.png)  

### Login Panel  
![4](UI/3.png)  

### Profile  
![5](UI/5.png)  

### Username Editing  
![6](UI/6.png)  

### Posts  
![7](UI/7.png)  

### Conversations  
![8](UI/8.png)  

### Friends List  
![9](UI/9.png)  

