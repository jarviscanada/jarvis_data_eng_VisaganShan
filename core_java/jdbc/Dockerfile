FROM openjdk:11-slim

# Set working directory inside container
WORKDIR /usr/local/app

# Copy JAR file into container
COPY target/jdbc*.jar ./jdbc/lib/jdbc.jar

# Copy properties file into container
# COPY src/main/resources/properties.txt ./src/main/resources/properties.txt

ENTRYPOINT ["java","-jar","jdbc/lib/jdbc.jar"]
