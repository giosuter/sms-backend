1.a Start sms-backend as a Spring Boot App.
or
1.b Start sms-backend mvn spring-boot:run from the root directory of the project (where pom.xml is located)
2. You can reach the application by using the URLs for the available endpoints


Working URLs on localhost:

Available endpoints:
------------------- 

1. GET
------

--> Retrieves all students
--  ----------------------
http://localhost:8080/sms/students


--> Retrieves details for given student by e-mail
--  --------------------------------------------- 
http://localhost:8080/sms/students/email/giovanni.suter@me.com


--> Retrieves details for given student by first name
--  -------------------------------------------------
http://localhost:8080/sms/students/firstname/Giovanni


2. POST
-------
--> Creates new student
--  -------------------
http://localhost:8080/sms/students 


GitHub:
------

TOKEN GitHub for SMS Back-End Project Token
-------------------------------------------

Username: giosuter
Password: XXXXXXXXX

