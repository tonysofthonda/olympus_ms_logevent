
# Log Event Microservice  

This module is a simple **logging mechanism**. Its purpose is to record all the **events** generated by the other microservices that are part of the **Olympus** project.  

Basically, this application exposes a single endpoint (as a REST Web service) that accepts an Event (json) object, which is written in a log file.  


## How it works

1. The project includes a properties file in XML format (**properties.xml**), with the entry:  
   `logpath: To indicate the directory on the server where the logs will be stored`

2. While deploying this module, if the given **logpath** refers to:  
     - A directory that doesn't exist yet, then the application automatically creates it.
     - A location with denied access, then the application throws an exception and doesn't start.

3. On a daily basis, the module creates a log file with a name that follows the pattern:  
   `event{yyyy}{MM}{dd}.txt`
   
4. An HTTP client can log an event, by hitting the endpoint  
   `http://{server-ip}:8083/olympus/logevent/v1/event`  
   sending a POST request in JSON format, with the body containing the event data:  
   `{`  
   `   "source": "microservice-name",`  
   `   "status": status-code,`  
   `   "msg": "event-description",`  
   `    "file": "file-name" `  
   `}`  
   
5. For each event received, the module generates a log entry with the format:  
   `{date},{time},{event.source},{event.status},{event.msg},{event.file}`  
   
6. If a log entry cannot be written to the log file (due to a data validation rule or permissions issue) the corresponding  
   message is sent to the client.  

7. The health-check endpoint can be used to verify if the application is up and running.  
   `http://{server-ip}:8083/olympus/logevent/v1/health`  


## Tools  

+ Java v1.8.0_202
+ Maven v3.8.6
+ Spring Boot v2.6.14
+ JUnit v5.8.2 with AssertJ v3.21.0
+ Lombok v1.18.24
+ Logback v1.2.11


## Run the app

Obtaining the application's WAR file  
`$ mvn clean package`  
  
Running the project as an executable JAR  
`$ mvn spring-boot:run`  

Running the tests  
`$ mvn test`  


## Usage

### 1. Write event to log (sending valid data)

#### Request
`POST /olympus/logevent/v1/event`

    curl -X POST -H 'Content-Type: application/json' -d '{"source": "ms.monitor", "status": 1, "msg": "SUCCESS", "file": "file01.txt"}' http://{server-ip}:8083/olympus/logevent/v1/event

#### Response
    HTTP/1.1 200
    Content-Type: text/plain;charset=UTF-8
    Content-Length: 2
    Date: Tue, 16 May 2023 23:26:55 GMT

    OK

### 2. Write event to log (sending invalid data)

#### Request
`POST /olympus/logevent/v1/event`

    curl -i -X POST -H 'Content-Type: application/json' -d '{"file": "file01.txt"}' http://{server-ip}:8083/olympus/logevent/v1/event

#### Response
    HTTP/1.1 400
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Tue, 16 May 2023 23:32:21 GMT
    Connection: close

    {"timestamp":"2023-05-16T18:32:21.090-05:00","status":400,"error":"Bad Request","message":"Invalid msg; Invalid source; Invalid status","path":"/olympus/logevent/v1/event"}
>>>>>>> develop
