# FileAccess
Design decission

FileAccessApplication is a Springboot application. 
I chose Spring boot, because is a rapid application development platform. It uses various components of Spring, and has the ability to package the application as a runnable jar, which includes an embedded tomcat server.

Advantages of usage of java.nio for file opperations: 
java.nio is the newest and the most performant java api for I/O opperations.

REST API:

See javadoc folder under project root.

Run instructions:
Before run the project please change the storage location: fileAccess.path in application.properties
The application is running on port: 8090
Cmd command: 
maven package
(run it from the project folder which contains the pom.xml file)
mvn spring-boot:run


