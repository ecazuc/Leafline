FROM openjdk:20
COPY ./target/server-0.0.1-SNAPSHOT.jar /tmp
WORKDIR /tmp
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "server-0.0.1-SNAPSHOT.jar"]
