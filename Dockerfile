FROM gradle:7.4.0-jdk17 AS build
WORKDIR /build
COPY . .
RUN gradle build --no-daemon

FROM openjdk:17.0.2-jdk
WORKDIR /app
COPY --from=build /build/build/libs/CatBot*.jar CatBot.jar
EXPOSE 8080
CMD ["java", "-jar", "CatBot.jar"]
