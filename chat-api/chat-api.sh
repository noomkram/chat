docker stop ora-db
docker rm -v ora-db
docker rmi ora-db
./mvnw -P docker-db clean install docker:build
docker create -p 5432:5432 -e POSTGRES_DB=ora-db -e POSTGRES_PASSWORD=password -e POSTGRES_USER=system --name ora-db ora-db
docker start ora-db