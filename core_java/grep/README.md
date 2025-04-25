# Grep Application

## Introduction
The Grep Application is a project created to mimic the Linux `grep` command. My grep application is a command-line utility that searches for a text pattern recursively in a given directory, and outputs matched lines to a file. This project was implemented using
Core Java and its features such as Lambda, Stream and Regex and imported classes such as BufferedReader, BufferedWriter and Files used in reading and writing operations for files. For the deployment of the app, I decided on containizering the app for Docker, creating
an image of the application and allowing it to used in any platform or enivronment. 

## Quick Start
To run the application from a JAR file:

```bash
# Create a jar file
mvn clean package
# Execute the jar file with arguments
java -jar target/grep-1.0-SNAPSHOT.jar <regex_pattern> <search_directory> <outfile_location>
```
To run the application using a docker image:
```bash
# Retrieve docker image from DockerHub
docker pull visagan/shan

# Run the docker command
docker run --rm \
-v <local /data directory path>:</data directory mount point in container> \
-v <local /log directory path>:</log directory mount point in container> \
visaganshan/grep <regex_expression> <local_path to data> <local_path to log file>
```

## Implementation
The grep application was implemented using a recursive approach in which all files within a specified directory (including files within child directories) were searched, and would write all lines that matched the given regex expression to an output file.

## Pseudocode
```Pseudocode
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

## Performance Issue
In my original implementation (before implementing using lambda and streams), I used an inefficient method of reading lines from a given file and realized that by storing the lines read into a List<String> I was consuming a signficant amount of memory and leading to an `OutOfMemoryError`.
To solve this problem temporarily, I expanded the maximum heap memory allocation for the project while I worked on a sustainable and efficient solution. Instead of reaidng the entire file into memory, I used the stream processes which process files line by line, requiring less memory allocation and
allows for more simplistic and efficient performance when compared to using Lists.

## Test
The application was tested manually with various test cases using the IntelliJ IDE.

## Deployment
To deploy this application, I used the Maven Shade Plugin to create a fat JAR file which will help to create a single JAR file containing all dependencies and resources needed to run the java application instead of using multiple JAR files. I then executed `mvn clean package`
to create the fat JAR file. To turn the JAR file into a docker image, I created a Dockerfile and specified the version of Java used for the base image and included commands to copy the JAR file from my local directory to the directory inside the docker image. I then 
created an ENTRYPOINT command which sets the command that would be run when a container is started from the Docker image, which in this case would be `java -jar <jar_file>`, which executes the jar file. I then built the new docker image by using the `docker build` command,
executing the Dockerfile and building an image locally. I then used `docker run` to ensure that the image was properly built and the application was containerized before uploading it to Docker Hub using `docker push visaganshan/grep`.

## Improvements
Here are a few things I would like to improve on in the future:
1. adding functionality for more specific searches that can be implemented using options. Grep options that would be helpful to have would be, ignore capitals, return a count of the number of lines that match, etc.
2. Additional implementation of Stream in the Grep App methods. While a few methods were overridden to implement stream functionality, the code would be more efficient and readable if all iterative and recursion methods were modified to implement stream.


