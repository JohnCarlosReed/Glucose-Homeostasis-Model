#!/bin/sh

rm deploy/model.war
sudo /etc/init.d/tomcat7 stop
sudo -u tomcat7 rm -rf /var/lib/tomcat7/webapps/model*
cd deploy
# change the filename to whatever makes more sense
jar cvf model.war *
sudo -u tomcat7 cp model.war /var/lib/tomcat7/webapps/.
sudo /etc/init.d/tomcat7 start
