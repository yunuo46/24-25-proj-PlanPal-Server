FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app

COPY ./build/libs/planpal-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 3000

ENTRYPOINT ["java","-jar","app.jar"]