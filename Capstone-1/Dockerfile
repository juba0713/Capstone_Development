# Step 1: Build the Maven project
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app  
COPY Capstone-1/pom.xml .  
RUN mvn dependency:go-offline  
COPY Capstone-1/. . 
RUN mvn clean package -DskipTests  

# Step 2: Create the runtime image
FROM openjdk:17-jdk-slim
WORKDIR /app  
COPY --from=build /app/target/Capstone-1-0.0.1-SNAPSHOT.jar Capstone-1.jar  
EXPOSE 8080
ENTRYPOINT ["java","-jar","Capstone-1.jar"]
