FROM java:8
WORKDIR /home
COPY build/libs/Email-Service.jar /home/Email-Service.jar
ENV SERVICE_MODE=PROD
ENV STAGEMONITOR_INSTANCENAME=prod
ENV STAGEMONITOR_REPORTING_INFLUXDB_URL=metric.indiabizforsale.com:8086
CMD ["java", "-jar", "Email-Service.jar"]
