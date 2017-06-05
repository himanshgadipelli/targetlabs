# targetlabs

This project implements a RESTful API spring-boot application. This service saves the file in a target location and returns the meta-data.





To upload a file via command line use the following command

curl -F uploadFile=@"<insert location of file here>" http://localhost:8080/api/upload/