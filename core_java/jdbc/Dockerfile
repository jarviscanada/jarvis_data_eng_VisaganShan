FROM openjdk:11-slim
COPY target/jdbc*.jar /usr/local/app/grep/lib/jdbc.jar
ENTRYPOINT ["java","-jar","/usr/local/app/jdbc/lib/jdbc.jar"]
