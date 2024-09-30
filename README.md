# Client credit card app

## Technologies Used

This project leverages the following technologies:

1. **Spring Boot**  
   A Java-based framework used for building web applications and microservices. It simplifies the development process by providing a wide range of pre-configured features, allowing quick setup and deployment.

2. **React**  
   A JavaScript library for building user interfaces, particularly single-page applications. It allows for fast, dynamic rendering and component-based architecture.

3. **H2 Database**  
   An in-memory relational database commonly used for development and testing purposes. It provides fast setup and easy integration with Spring Boot.

4. **Apache Kafka**  
   A distributed event streaming platform capable of handling real-time data feeds. It is used for building event-driven applications, streaming data pipelines, and pub/sub messaging systems.

5. **WebSocket**
    WebSocket
    A communication protocol providing full-duplex communication channels over a single TCP connection. It enables real-time, bi-directional communication between the client (React) and the server (Spring Boot)

### Additional Tools

- **Maven/Gradle**: For managing project dependencies and build processes.
- **JUnit**: Used for writing and running unit tests.
- **Lombok**: A Java library to minimize boilerplate code.
- **Docker**: For containerizing applications and creating a consistent environment across multiple deployments.

## Prerequisites

- **Docker** installed on your machine.
- **Docker Compose** installed.
### Setup and Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Amuhadri/credit-card.git
2. Navigate to the project directory:
    ```bash
    cd credit-card
3. Build and start the Docker containers using Docker Compose:
    ```bash
    docker-compose up --build
4. Access the application:
    Frontend (React): The application UI will be available at http://localhost:3000.
    Backend (Spring Boot): The API will be accessible at http://localhost:8080.
    
### Post Setup

-  Ensure all services (React, Spring Boot, Kafka, and H2 Database) are running properly. You can     check the logs using:
    ```bash
    docker-compose logs
-  To shut down the application, use the following command:
    ```bash
    docker-compose down
### Kafka: Sending Card Status Messages
To send card status messages to the Kafka topic (card-status-topic), follow these steps:
1. Get the container ID or name of the Kafka broker. You can list running containers with:
    ```bash
    docker ps
2. Use the docker exec command to enter the Kafka container:
    ```bash
    docker exec -it <kafka_container_id> kafka-console-producer --topic card-status-topic --bootstrap-server kafka:9092
    ```
    
   Replace <kafka_container_id> with the actual container ID or name. For example:
    ```bash
    docker exec -it d60f2b5a698048d9973882e15a068a2a1eb59b2670328b687cddf997f7af4caf kafka-console-producer --topic card-status-topic --bootstrap-server kafka:9092
3. Once the Kafka console producer is running, send card status messages by typing the OIB (personal identification number) and the corresponding status in the format oib:status. For example:
    ```makefile
    12345678901:APPROVED
    ```
    This sends a message to the card-status-topic indicating that the card with OIB 12345678901 has been approved.
4. Press Enter to send the message.

### WebSocket Functionality for Real-time Updates
The application has been extended with WebSocket functionality, enabling real-time updates on the frontend. The WebSocket server listens for Kafka messages and pushes notifications to the frontend whenever a card status update occurs.

#### How It Works
1. **Kafka Listener:** The Spring Boot backend includes a Kafka listener that consumes messages from the card-status-topic. When a message is received (e.g., 12345678901:APPROVED), it processes the card status update.

2. **WebSocket Notification**: The Spring Boot application then broadcasts the card status update via WebSocket. The frontend (React) is subscribed to this WebSocket and listens for any messages regarding card status changes.

3. **Frontend Notification and Status Update**: When a WebSocket message is received on the frontend, the application immediately displays a notification (e.g., "Card status for OIB 12345678901: APPROVED") and updates the corresponding card status in real-time on the UI.

#### Testing Real-time Updates
1. Send a Kafka message as described in the Kafka section:
    ```bash
    docker exec -it <kafka_container_id> kafka-console-producer --topic card-status-topic --bootstrap-server kafka:9092
    ```
2. Once the Kafka message is sent (e.g., 12345678901:APPROVED), the frontend will receive a real-time update.

3. A notification will appear on the UI with the status update, and the status of the card in the list will change accordingly.






