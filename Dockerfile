FROM openjdk:17

COPY ./build/libs/*.jar /application/

WORKDIR /application

ENTRYPOINT ["java", "-jar", "/application/application.jar"]
