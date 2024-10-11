FROM openjdk:21-jdk
ENV DB_URL=jdbc:postgresql://localhost:5432/postgres
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=mysecretpassword
RUN mkdir /app
COPY ./build/libs/PaymentService.jar /app/app.jar
WORKDIR /app
RUN ls
ENTRYPOINT ["java", "-jar", "app.jar"]