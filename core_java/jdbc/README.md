# JDBC Application

## Introduction
The Grep Application is an application that allows a user to lookup the details of any publicly traded stock to purchase and sell at the current market value. It is intended to be used as a trading simulator, allowing users to gain practical experience on trading stocks
without the monetary risks involved. Users can also view their position and stock quote details of their positions by id or it can all be listed.

## Quick Start
To run the application from a JAR file:

```bash
# Create a jar file
mvn clean package
# Execute the jar file
java -jar target/jdbc-1.0-SNAPSHOT.jar
```
To run the application using a docker image:
```bash
# Retrieve docker image from DockerHub
docker pull visaganshan/jdbc
# Run the docker command
docker run \
-v <local /properties.txt path>:</properties.txt mount point in container> \
-it visaganshan/jdbc
```

## Implementation
This application was implemented by first retrieving stock quote data from an API URI using the OkHttp library and parsing the response into an object from a JSON string. The object can then be persisted to the postgreSQL database which is connected to the java application using
the JDBC library and Postgres Driver. Operations involving the database are implemented by creating a Data Access Layer that isolates database-related code logic using Data Access Objects (DAOs).  I implemented the service layer using two objects that would handle the main 
functions of the application such as buying and selling stock and created a controller class that would provide the user an interface to interact with and access these methods.

## ER Diagram
![image](https://github.com/user-attachments/assets/6e6e53db-42bf-44e7-878c-471f4e9cba5a)

## Test
The application was tested manually using the IntelliJ IDE and the packaged JAR file. I also created unit and integration tests for my DAO objects and service-layer objects using the Mockito and JUnit library to test and create mock objects. My final testing was
done manually using the dockerized application.

## Deployment
To deploy this application, I used the Maven Shade Plugin to create a single FAT jar file as well as built it into a docker image which I then uploaded to docker hub to allow for containerized deployment.

## Improvements
Here are a few things that can be added to improve the simulator:
- At the moment, the user has an infinite balance and while the profit/loss of sold shares are shown, it's not stored or recorded. Implementing a balance defined by the user at the beginning of the execution would beneficial to track profits and losses of shares sold but also make the simulator more realistic to the user, as they can simulate risking an amount they would be willing to risk when trading in real-life, which helps to promote risk-management skills.
