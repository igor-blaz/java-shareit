#FROM eclipse-temurin:21-jre-jammy
#VOLUME /tmp
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]

FROM eclipse-temurin:21-jre-jammy

VOLUME /tmp

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

RUN echo "Checking app.jar:" && ls -lh /app.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]
