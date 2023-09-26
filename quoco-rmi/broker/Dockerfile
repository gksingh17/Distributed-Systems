FROM openjdk:18
COPY target/broker-0.0.1-jar-with-dependencies.jar /broker.jar
CMD ["/usr/bin/java", "-cp", "/broker.jar", "Server"]