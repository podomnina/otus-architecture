FROM bellsoft/liberica-openjre-debian:17
COPY target/otus-architecture-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]