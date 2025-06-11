#INSTALLATION OF THE OPERATING SYSTEM
FROM alpine:3.18 AS base

# to install onen JDK
RUN apk add --no-cache openjdk17

LABEL authors="proma"

#PLACEMENT OF THE EXECUTABLE [MICROSERVICE] ON THE IMAGE
COPY target/fee-service-dev-1.jar app.jar

#EXPOSE PORTS FOR INCOMING TRAFFIC - HOST_PORT:CONTAINER_PORT
EXPOSE 8085:8085

#INSTALLING UTILITIES
RUN apk update
RUN apk add --no-cache gcc
RUN apk add --no-cache curl

#ENTRYPOINT OF THE CONTAINER THROUGH THE MICROSERVICE
ENTRYPOINT ["java","-Dspring.profile.active=dev","-jar","app.jar"]