#!/bin/bash

TMP_DIR=/tmp/celar-server-api-rpm

WARFILE=$(ls $TMP_DIR/*.war)


mv $WARFILE $CATALINA_HOME/webapps/

rmdir $TMP_DIR
