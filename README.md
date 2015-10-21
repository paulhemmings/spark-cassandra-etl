## Generic CSV file loader for Cassandra

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

##### Run

````
$ ./gradlew run
$ open http://localhost:4567
````

##### Use

Make a POST request to the following URL

http://localhost:4567/load

Example payload

````
{
 hostName: "127.0.0.1",
 keySpace: "flights",
 tableName: "testnametable",
 columns: [ "id", "firstname", "lastname", "title" ],
csvFileName: "[full path]/spark-cassandra-etl/example/test-file.csv"
}
````

Suggestion: Install Postman extension for chrome

https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=en
