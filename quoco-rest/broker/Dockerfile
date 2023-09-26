FROM openjdk:8-jre-alpine
COPY target/broker-1.0.0.jar /broker-1.0.0.jar
CMD  ["/usr/bin/java", "-jar",  "/broker-1.0.0.jar", "http://auldfellas:8081/quotations, http://dodgydrivers:8082/quotations, http://girlpower:8083/quotations"]