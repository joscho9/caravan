
FROM amazoncorretto:17
WORKDIR /app
COPY target/*.jar /app.jar

# 4. Lege den Entrypoint fest
ENTRYPOINT ["java","-jar","/app.jar"]