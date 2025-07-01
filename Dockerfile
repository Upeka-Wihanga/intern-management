FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . /app

RUN chmod +x ./mvnw && ./mvnw -DskipTests clean package

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run"]