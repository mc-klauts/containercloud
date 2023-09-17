FROM openjdk:17

# copy files from gradle build into destination folder
COPY build/libs/containercloud-1.0-SNAPSHOT-all.jar home

WORKDIR home

ENTRYPOINT java -jar containercloud-1.0-SNAPSHOT-all.jar
