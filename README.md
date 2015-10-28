## Generic CSV file loader for Cassandra

#### Prerequisites.

* Requires a running instance of Cassandra
* Requires Gradle

#### How to use the application

##### Clone

````
$ git clone https://github.com/paulhemmings/spark-cassandra-etl.git
````

##### Build

````
$ cd spark-cassandra-etl
$ gradle wrapper
$ ./gradlew clean build
````

##### Run unit tests

````
$ ./gradlew test
````

##### Start web service

````
$ ./gradlew run
````

##### Use

Make a POST request to the following URL

http://localhost:4567/load

##### Example payload

The column definition has two parts. First part is the name of the column in the destination table. Second part is if this column takes a value that is a string (or more specifically, requires the value to be in quotes).

````
{
  hostName: "127.0.0.1",
  keySpace: "flights",
  tableName: "testnametable",
  columnDefinitions: [ "id:false", "firstname:true", "lastname:true", "title:true" ],
  csvFileName: "[full path]/spark-cassandra-etl/example/test-file.csv"
}
````

Suggestion: Install Postman extension for chrome

https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=en

#### Known limitations

* The file has to be reachable by the server.
* No logging.
* No useful information returned from request.
* No error handling.
