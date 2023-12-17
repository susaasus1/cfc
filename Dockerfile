FROM openjdk:17-jdk-slim
WORKDIR /app

COPY  src/main/resources/store/YATrustStore.jks /app/YATrustStore.jks
COPY target/cfc-0.0.1-SNAPSHOT.jar /app/processing.jar

ENV JAVA_OPTS="-Djavax.net.ssl.trustStore=/app/YATrustStore.jks -Djavax.net.ssl.trustStorePassword=Dan123"

EXPOSE 8080
ENTRYPOINT java $JAVA_OPTS -jar processing.jar