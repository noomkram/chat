#
# docker create -p 5432:5432 -e POSTGRES_DB=ora-db -e POSTGRES_PASSWORD=password -e POSTGRES_USER=system --name ora-db ora-db
#
FROM postgres:9.6.3

ADD schema.sql /docker-entrypoint-initdb.d