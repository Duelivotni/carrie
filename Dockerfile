FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/carrie-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
