FROM openjdk:23-bullseye

# maven install
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY TextTuah/pom.xml /app
COPY TextTuah/src /app/src

RUN mvn clean install -DskipTests

ENTRYPOINT ["java", "-jar", "target/TextTuah-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080