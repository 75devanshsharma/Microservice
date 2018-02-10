FROM openjdk:8
WORKDIR /home
COPY build/libs/Email-Service.jar /home/Email-Service.jar
ENV SERVICE_MODE=PROD
CMD ["java", "-jar", "Email-Service.jar"]
