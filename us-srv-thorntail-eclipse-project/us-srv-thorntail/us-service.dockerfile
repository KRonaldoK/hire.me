FROM openjdk:8
RUN mkdir microservices
COPY ./us-srv-thorntail.jar ./microservices
EXPOSE 8080
ENTRYPOINT java -jar -Djava.net.preferIPv4Stack=true ./microservices/us-srv-thorntail.jar