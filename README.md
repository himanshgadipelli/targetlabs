# targetlabs

This project implements a RESTful API spring-boot application. 
This service saves the file in a target location and returns the meta-data.
The file is saved in a different folder for validation(for future implementations).

The upload file size is set to 1 MB. Can be set higher when using IntelliJ as IDE.



To upload a file via browser ping http://localhost:8080/
Select the file you want to upload and click submit. On successful

To upload a file via command line use the following command

curl -F uploadFile=@"insert location of file here" http://localhost:8080/api/upload/
