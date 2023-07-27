# This is just a simple sample code
```
Flink -> PostgreSQL
```
## Folder structure
```
├── build.sbt
├── compose.yml ... Docker compose file
├── init-db
│   └── init.sql ... SQL to initialize DB 
├── input
│   └── word.txt ... Input file
└── src
    └── main
        ├── resources
        │   └── log4j2.xml
        └── scala
            └── dk.com.HelloFlinkPostgre.scala ... Flink code
```
## Abut Flink application

A Flink application project using Scala and SBT.

To run and test your application locally, you can just execute `sbt run` then select the main class that contains the Flink job . 

You can also package the application into a fat jar with `sbt assembly`, then submit it as usual, with something like: 

```
flink run -c dk.com.HelloPostgre /path/to/your/project/my-app/target/scala-2.12/testme-assembly-0.1-SNAPSHOT.jar
```

# About Local enviroment
## Pre-condition
* JDK8/11
* docker / docker compose

See also [here](#appendix-A)

## To Set-up
* Just run docker compose
```
sudo docker compose up -d
```
## To Clean-up
```
sudo docker compose down

# If you want to remove docker volumes as well
# sudo docker compose down -v
```

# <span id="appendix-A">Appendix-A: Set-up docker/docker compose</span>
## To confirm your enviroment
* Java
```
$ java --version

openjdk 11.0.19 2023-04-18
OpenJDK Runtime Environment (build 11.0.19+7-post-Ubuntu-0ubuntu122.04.1)
OpenJDK 64-Bit Server VM (build 11.0.19+7-post-Ubuntu-0ubuntu122.04.1, mixed mode, sharing)
```
* docker/docker compose
```
$ docker --version
Docker version 24.0.4, build 3713ee1

$ docker compose --version
Docker version 24.0.4, build 3713ee1
```
## How to install
### Java
```
sudo apt update
sudo apt install openjdk-11-jre-headless
```
