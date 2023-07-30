FROM amd64/eclipse-temurin:17.0.8_7-jdk-alpine

COPY ./build/libs/application.jar /opt/app/application.jar

WORKDIR /opt/app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

CMD ["java", "-jar", "/opt/app/application.jar"]