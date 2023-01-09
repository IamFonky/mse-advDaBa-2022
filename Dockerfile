FROM maven:latest
COPY src/ src
COPY files/ files
COPY pom.xml .
RUN mvn compile
CMD ["mvn", "exec:java"]