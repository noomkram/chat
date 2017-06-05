#
# docker create -p 5432:5432 -e POSTGRES_DB=ora-db -e POSTGRES_PASSWORD=password -e POSTGRES_USER=system --name ora-db ora-db
#
FROM frolvlad/alpine-oraclejdk8
ADD chat-ws.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]