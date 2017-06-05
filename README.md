# Chat

## Prerequisites
* [Git](https://git-scm.com/download/mac)
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 
* [Docker](https://docs.docker.com/docker-for-mac/install/) is installed with a VM using an IP address of 192.168.99.100, see below for configuring the application to run on a different host IP  

## Clone
Clone the Chat application via
```
git clone https://github.com/noomkram/chat.git
```

## Install
From the `/chat` directory execute:
```
./chat.sh
```

## API
The Chat application endpoints are accessible via:
* http://192.168.99.100:8080/

Endpoint details can be found [here](http://docs.oracodechallenge.apiary.io/#).

### Logs
The application logs can be viewed via:
```
docker logs -f ora-ws 
```

### Configuring the host IP
If the host is running on an IP other than 192.168.99.100, in `/chat-ws/chat-ws.sh` change:
```
docker create -p 8080:8080 --name ora-ws ora-ws
```
to
```
docker create -p 8080:8080 -e datasource.host <host IP> --name ora-ws ora-ws
```

### Postman
A sample postman (v2) file can be found at:
```
/chat/ora.postman_collection
```
