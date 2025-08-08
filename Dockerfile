FROM ibm-semeru-runtimes:open-21-jdk-jammy
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle* .
COPY settings.gradle* .
COPY src src

RUN ./gradlew clean bootJar --info --stacktrace --no-daemon

FROM ibm-semeru-runtimes:open-21-jdk-jammy
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]