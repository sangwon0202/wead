FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/wead.jar
ADD ${JAR_FILE} wead.jar
ENTRYPOINT ["java", "-jar", "/wead.jar"]