FROM amazoncorretto:17-alpine3.16-jdk
ENV JAVA_HOME=/opt/openjdk-17
WORKDIR /app

ENV PATH=/opt/openjdk-17/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

VOLUME /tmp
COPY build/libs/football-0.0.1-SNAPSHOT.jar app.jar
COPY cacerts /usr/lib/jvm/java-17-amazon-corretto/lib/security/cacerts

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]