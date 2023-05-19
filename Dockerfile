FROM openjdk:19
ADD target/FrontEnd-1.0-SNAPSHOT.jar myJar.jar
ENTRYPOINT ["java", "-jar", "myJar.jar"]