ARG JDK

# Build Stage
FROM maven:latest as build
WORKDIR /app
# Resolve dependencies
COPY /pom.xml /app
RUN mvn -B dependency:resolve dependency:resolve-plugins
# Compile and Build App
COPY /src /app/src
RUN mvn clean package -DskipTests

# Final Stage
FROM amazoncorretto:21

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'

COPY --chown=185 --from=build /app/target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 --from=build /app/target/quarkus-app/*.jar /deployments/
COPY --chown=185 --from=build /app/target/quarkus-app/app/ /deployments/app/
COPY --chown=185 --from=build /app/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE $PORT
USER root

ENV AB_JOLOKIA_OFF=""

ENV JAVA_VERSION=21
ENV PORT="6000"
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Dquarkus.http.port=6000 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

CMD ["java", "-Dquarkus.http.port=6000", "-jar", "/deployments/quarkus-run.jar"]
