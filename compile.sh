javac -classpath .:/usr/share/java/servlet-api.jar ./src/*.java
mv ./src/*.class ./deploy/WEB-INF/classes
