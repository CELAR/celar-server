#!/bin/bash

service celar-server stop
rm  -rf /etc/init.d/celar-server
rm  -rf /opt/celar/celar-server/keystore.jks
