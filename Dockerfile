FROM openjdk:17-alpine
WORKDIR /app
ARG JAR_FILE=/build/libs/*-SNAPSHOT.jar
ENV TZ Asia/Seoul
COPY ${JAR_FILE} app.jar
COPY scouter /app/scouter

EXPOSE 8080
CMD java -Xmx2048m -javaagent:/app/scouter/agent.java/scouter.agent.jar -Dscouter.config=/app/scouter.conf -Djdk.attach.allowAttachSelf=true -jar /app/app.jar
