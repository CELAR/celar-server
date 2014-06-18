#!/bin/bash

# This is the init script for the CELAR Server daemon.
# When deployed, it should be placed under /etc/init.d/
# to be executed as a service.

[ -z "$CELAR_SERVER_HOME" ] && CELAR_SERVER_HOME="/opt/celar/celar-server"

LIB_DIR=$CELAR_SERVER_HOME/lib
CONF_DIR=$CELAR_SERVER_HOME/conf


CLASSPATH=$CONF_DIR
CLASSPATH=$CLASSPATH:$(echo $LIB_DIR/*.jar | tr ' ' ':')
CLASSPATH=$CLASSPATH:$CELAR_SERVER_HOME_DIR/celar-server.jar
PIDFILE=/tmp/celar-server.pid

start() {
    java -Dname=celar-server -cp $CLASSPATH  gr.ntua.cslab.celar.server.daemon.Main &
    echo $! > $PIDFILE;
}

stop() {
    PID=$(cat $PIDFILE)
    kill -TERM $PID
    rm -f $PIDFILE 
}

status() {
    if [ -f $PIDFILE ]; then
        PID=$(cat $PIDFILE)
        echo "CELAR Server running ($PID)"
    else
        echo "CELAR Server not running"
    fi
}

case $1 in
    start)
        start;
    ;;
    stop)   
        stop;
    ;;
    restart)    
        stop && start;
    ;;
    status)   
        status;
    ;;
    *)      
        echo "$0 {start|stop|status|restart}"
    ;;
esac