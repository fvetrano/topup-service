FROM java:8-alpine

ADD target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar", "-Xms64m"]
