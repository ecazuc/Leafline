FROM openjdk:21-slim
COPY ./target/router-0.0.1-SNAPSHOT.jar /tmp
WORKDIR /tmp
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "router-0.0.1-SNAPSHOT.jar"]
