ARG BUILDPLATFORM
ARG TARGETPLATFORM

FROM --platform=$BUILDPLATFORM maven:3-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package

FROM --platform=$TARGETPLATFORM amazoncorretto:21-alpine-jdk AS run

WORKDIR /app

COPY --from=build /app/target/PureTech-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
