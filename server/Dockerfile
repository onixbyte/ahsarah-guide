FROM amazoncorretto:21 AS builder
WORKDIR /app
ADD . .
RUN chmod +x ./gradlew && \
    ./gradlew build -x test

FROM amazoncorretto:21-alpine AS runner
WORKDIR /app
COPY --from=builder /app/build/libs/ahsarah-guide-server-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
