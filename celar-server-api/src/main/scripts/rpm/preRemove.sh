#!/bin/bash

service tomcat stop

WARDIR=$(ls -d $CATALINA_HOME/webapps/celar-server-api*/)		# list only dirs

WARFILE=$(ls $CATALINA_HOME/webapps/celar-server-api*.war)		# list only dirs

rm -rf $WARFILE		# remove the war
rm -rf $WARDIR		# remove the dir

exit 0
