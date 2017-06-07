# targetlabs

This project implements a RESTful API spring-boot application. 
This service saves the file in a target location and returns the meta-data.
The file is saved in a different folder for validation(for future implementations).

The upload file size is set to 1 MB. Causing errors with higher file sizes when using eclipse. 
Can be set higher when using IntelliJ as IDE.



To upload a file via browser ping http://localhost:8080/
Select the file you want to upload and click submit.

To upload a file via command line use the following command

curl -F uploadFile=@"insert location of file here" http://localhost:8080/api/upload/

Update #1
Upon successful upload a file with the meta-data fields is saved in the storage folder(in txt format).