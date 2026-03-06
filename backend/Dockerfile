FROM eclipse-temurin:21-jre

RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY build/libs/VzBot-Discord-Bot-all.jar app.jar

CMD ["java", "-jar", "app.jar"]
