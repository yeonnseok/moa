#!/bin/bash

REPOSITORY=/home/ubuntu/server
cd $REPOSITORY

APP_NAME=moa
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME
JVM_OPTS="
-Dspring.profiles.active=live
-Dspring.config.location=$REPOSITORY/application-oauth.yml,$REPOSITORY/application.yml
"

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z "$CURRENT_PID" ]
then
  echo "> 종료할 것 없음." >> /home/ubuntu/deploy.log
else
  echo "> sudo kill -15 $CURRENT_PID" >> /home/ubuntu/deploy.log
  kill -15 "$CURRENT_PID" >> /home/ubuntu/deploy.log 2>&1
  sleep 5
fi

echo "> $JAR_PATH 배포"
nohup java $JVM_OPTS -jar "$JAR_PATH" >> /home/ubuntu/deploy.log 2>&1 &

echo "[$(date)] server deploy" >> /home/ubuntu/deploy.log
