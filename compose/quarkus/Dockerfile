ARG JDK

FROM maven:latest as builder
WORKDIR /build

COPY /pom.xml /build
RUN mvn -B dependency:resolve dependency:resolve-plugins

COPY /src /app/src
RUN mvn clean package -DskipTests

FROM amazoncorretto:21

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'

COPY --chown=185 --from=builder /build/target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 --from=builder /build/target/quarkus-app/*.jar /deployments/
COPY --chown=185 --from=builder /build/target/quarkus-app/app/ /deployments/app/
COPY --chown=185 --from=builder /build/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE $PORT
USER root

ENV AB_JOLOKIA_OFF=""

ENV JAVA_VERSION=21
ENV PORT="6000"
ENV HOST="0.0.0.0"
ENV JAVA_OPTS="-Dquarkus.http.host=$HOST -Dquarkus.http.port=$PORT -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

CMD java $JAVA_OPTS -jar $JAVA_APP_JAR