FROM ibm-semeru-runtimes:open-21-jdk-jammy
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]