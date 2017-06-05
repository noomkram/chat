#
# Assumes VM IP is 192.168.99.100
# Add -e datasource.host=<host.ip> to the docker create if host is on different IP
#
docker stop ora-ws
docker rm -v ora-ws
docker rmi ora-ws
./mvnw clean install docker:build
docker create -p 8080:8080 --name ora-ws ora-ws
docker start ora-ws