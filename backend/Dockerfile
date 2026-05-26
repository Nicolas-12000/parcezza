# Multi-stage build: build with Maven, run on a small distroless Java base.
# This Dockerfile produces a compact runtime image suitable for production.

FROM maven:3.9.16 as build
WORKDIR /app
COPY pom.xml mvnw .mvn/ ./
COPY src ./src
RUN mvn -B -DskipTests package

# Use a distroless Java runtime to minimize image size. This example uses Java 17
# distroless. If your application strictly requires Java 21 features, change the
# final image to an appropriate Java 21 runtime or use a jlink-based custom runtime.
FROM eclipse-temurin:21-jre
WORKDIR /app
# copy the fat jar produced by spring-boot
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
