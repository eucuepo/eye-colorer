# Eyecolorer

This is the documentation for the cloudspokes challenge “Eye is in the beauty of the beholder”. The specification is to design an app in the cloud where the user can upload an image, and the app will change his/her eye color.
## Running the application locally

First build with:

    $mvn clean install

Then run it with:

    $java -jar target/dependency/webapp-runner-7.0.22.jar target/eye-colorer.war

## Backend
For the backend part, I have chosen vanilla servlet, due being perfectly adapted to heroku and the libraries. There is one servlet doing the calculation and storing the generated image in a temp folder (UploadImage) and another for getting the image back to the frontend (getImage)
Heroku gives Tomcat 7 or Jetty as servlet container options, I went for tomcat due to the support for the servlet 3.0 API.

## Frontend
The frontend is a simple page in html with a form. The form has two fields, a file and a color. The file and the hex value of the color are sent via AJAX to the servlet, and it returns the URL for the generated image. To send the file via AJAX, I have used fileuploader (https://github.com/valums/file-uploader) which relays on jQuery (www.jquery.org) to work. For the color picker, I have used http://jscolor.com/.

## RESTful API
I have developed a simple RESTful API for the eye colorer service. Another app can send an image and the eye color in hex, and it will get the colorized image as response. This can be used to allow a mobile device app to upload a picture and get the results, or to integrate the eye coloring functionality to any app, being it desktop, cloud or mobile based.
