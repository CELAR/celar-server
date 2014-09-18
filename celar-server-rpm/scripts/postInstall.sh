#!/bin/bash

RANDOM_PASSWORD=$(cat /dev/urandom | tr -dc 0-9a-zA-Z | head -c 16)
CELAR_SERVER_HOME=/opt/celar/celar-server
KEYSTORE_PATH=$CELAR_SERVER_HOME/keystore.jks
CONF_FILE=$CELAR_SERVER_HOME/conf/celar-server.properties

create_keystore(){
keytool -genkey \
-keyalg RSA \
-alias celar-server \
-keypass $RANDOM_PASSWORD \
-storepass $RANDOM_PASSWORD \
-keystore $KEYSTORE_PATH \
-validity 730 << EOF
Giannis Giannakopoulos
ATHENA
CELAR
Europe
Europe
EU
yes
EOF
}

configure_server(){
sed -i 's|# server.ssl.keystore.path = |server.ssl.keystore.path = '$KEYSTORE_PATH'|' $CONF_FILE;
sed -i "s/# server.ssl.keystore.password = /server.ssl.keystore.password = $RANDOM_PASSWORD/" $CONF_FILE
sed -i "s/# server.ssl.port = 8443/server.ssl.port = 8443/" $CONF_FILE
}

create_service(){
	ln -sv $CELAR_SERVER_HOME/bin/celar-server /etc/init.d/
}
create_keystore;

configure_server;

create_service

service celar-server start;