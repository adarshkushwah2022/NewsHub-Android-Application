**Problem Statement:**\
**Summary:**\
A news application.It **starts/stops a service**. If the **internet connection** is available, then the
service downloads the **news files(JSON)** provided by the server on a URL (like
https://petwear.in/mc2022/news/news_<num>.json ) (while the service is running) and store it in
an appropriate location that is private to the application and displays its content on the
Application interface.
The news files can be **downloaded** all at once or downloaded at an **interval of 10 seconds** while
the service is running.
When the **battery is low, or the phone is charging**, the service must automatically stop.
  
  
**Description:**
1) The service must start by clicking the “start service” button and should stop by clicking
the “stop service” button. The most recent news article’s title should be displayed on the
application interface (optionally with image and news description).\
2)Check if an internet connection is available, and if available, then download the news
files available at (https://petwear.in/mc2022/news/news_<num>.json). Each file contains
one news article. Download and save this file as private to the application.\
Here <num> can be in range (1,2,3,4….), Eg:
https://petwear.in/mc2022/news/news_0.json
Download all files available, if some file, suppose “news_10.json” is not available, stop
downloading more files. Perform the network operations using appropriate functions of
AyncTask.\
3) Use broadcast receivers to stop the service when BATTERY_LOW or
POWER_CONNECTED and resume when BATTERY_OKAY and POWER_DISCONNECTED.\
4) The news files can be downloaded all at once or downloaded at an interval of 10
seconds while the service is running.\
5) The UI of the application must be developed using fragments and must use Linear or
Relative layouts.\
Structure of news file:\
{\
“title” : “.....”,\
“body” : “.....”,\
“image-url” : “.....”\
}\
**Note:**Only displaying the title of the most recent news is mandatory for the assignment.
The rest of the content is optional and is for the bonus.\
  
  
**Resources:**
1. JSON Parsing for Android in Java :
https://www.tutorialspoint.com/android/android_json_parser.htm
2. JSON Parsing for Android in Kotlin :
https://www.geeksforgeeks.org/json-parsing-in-android 
