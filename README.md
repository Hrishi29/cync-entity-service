# LOS Entity Service


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Java™ Platform, Standard Edition Development Kit 
* [Spring Boot](https://spring.io/projects/spring-boot) - Framework to ease the bootstrapping and development of new Spring Applications
* [PostgreSQL](https://www.postgresql.org/) - Open-Source Relational Database Management System
* [github](https://github.com/) - Distributed version control system
* [Lombok](https://projectlombok.org/) - Never write another getter or equals method again, with one annotation your class has a fully featured builder, Automate your logging variables, and much more.
* [Swagger](https://swagger.io/) - Open-Source software framework backed by a large ecosystem of tools that helps developers design, build, document, and consume RESTful Web services.

## External Tools Used

* [Postman](https://www.getpostman.com/) - API Development Environment (Testing Docmentation)

## To-Do

- [x] Logger (Console, File, Mail)
- [x] RESTful Web Service (CRUD)
- [x] Actuator
- [x] Docker

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.arc.sbtest.SBtemplateApplication` class from your IDE.

- clone the Git repository.
- Open Command Prompt and Change directory (cd) to folder containing pom.xml
- Open Eclipse 
   - File -> Import -> Existing Maven Project -> Navigate to the folder where you unzipped the zip
   - Select the project
- Choose the Spring Boot Application file (search for @SpringBootApplication)
- Right Click on the file and Run as Java Application

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

### Actuator

To monitor and manage your application

|  URL |  Method |
|----------|--------------|
|`http://localhost:8083/actuator/actuator`  | GET |
|`http://localhost:8083/actuator/health`    | GET |
|`http://localhost:8083/actuator/info`      | GET |

## Documentation

* [Swagger](http://localhost:8083/swagger-ui.html) - Documentation & Testing


## Resources

* [My API Lifecycle Checklist and Scorecard](https://dzone.com/articles/my-api-lifecycle-checklist-and-scorecard)
* [HTTP Status Codes](https://www.restapitutorial.com/httpstatuscodes.html)
* [Common application properties](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)
