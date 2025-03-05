FROM eclipse-temurin:21-jre

RUN apt-get update
RUN apt-get install gettext-base

COPY ./target/InventorySync.jar /usr/app/InventorySync.jar

WORKDIR /usr/app

CMD java -jar /usr/app/InventorySync.jar