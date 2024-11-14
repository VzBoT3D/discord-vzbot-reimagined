FROM gradle:8.10 as build

WORKDIR /app

COPY build/libs/VzBot-Discord-Bot-all.jar app.jar

CMD ["java", "-jar", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "app.jar"]
EXPOSE 5005