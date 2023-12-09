FROM openjdk:17

WORKDIR /application
COPY ./build/libs/*.jar application.jar


ENTRYPOINT ["java", "-jar", "/application.jar"]
