FROM maven:3.8.3-openjdk-17 as builder
COPY . .
RUN  mvn clean package -Dmaven.test.skip=true

FROM openjdk:17
ARG JAR_FILE=/target/*SNAPSHOT.jar
WORKDIR /app
COPY --from=builder ${JAR_FILE} sber.jar
ENTRYPOINT ["java","-jar","sber.jar"]
