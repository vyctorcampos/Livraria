
FROM maven:3.9.6-eclipse-temurin-17 AS build


WORKDIR /app


COPY pom.xml .


RUN mvn dependency:go-offline -B


COPY src ./src


RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jre-alpine


RUN apk add --no-cache curl


RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup


WORKDIR /app


COPY --from=build /app/target/*.jar app.jar


RUN chown -R appuser:appgroup /app


USER appuser


EXPOSE 8080


ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseContainerSupport"


ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
