FROM openjdk:21-jdk as build
COPY . /app
WORKDIR /app
RUN ./gradlew build -x test

FROM openjdk:21-jdk as final
ENV DB_URL=jdbc:postgresql://host.docker.internal:5432/postgres
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=mysecretpassword
RUN mkdir /app
COPY ./build/libs/PaymentService.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]