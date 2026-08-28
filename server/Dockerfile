FROM amazoncorretto:21 AS builder
WORKDIR /app
ADD . .
RUN chmod +x ./gradlew && \
    ./gradlew build -x test

FROM amazoncorretto:21-alpine AS runner
WORKDIR /app
COPY --from=builder /app/build/libs/delta-force-guide-server-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
