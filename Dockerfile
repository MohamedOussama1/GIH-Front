FROM openjdk:19
COPY target/FrontEnd-1.0-SNAPSHOT.jar myJar.jar
CMD ["java", "-jar", "myJar.jar"]