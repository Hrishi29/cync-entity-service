FROM amazoncorretto:8
# RUN apk add --update --progress
ENV PROFILE_NAME dev
EXPOSE 3000
WORKDIR /usr/src/app/
ADD . $WORKDIR
CMD java -jar -Dspring.profiles.active=$PROFILE_NAME target/cync-los-entity-0.0.1-SNAPSHOT.jar --server.port=3000 --server.servlet.context-path=/api/entity
