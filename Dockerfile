ARG BUILD_FROM=homeassistant/amd64-base:latest
FROM $BUILD_FROM

ENV LANG C.UTF-8

RUN apk add maven
RUN apk add openjdk17

WORKDIR /app
COPY . /app
COPY /app/pom.xml .
COPY /app/src/ ./src/
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests=true
COPY /app/target/app-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["sh", "/app/run.sh"]

LABEL io.hass.version="VERSION" io.hass.type="addon" io.hass.arch="armhf|aarch64|i386|amd64"