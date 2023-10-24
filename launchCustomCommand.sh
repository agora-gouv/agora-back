# /bin/bash

if [[ $# > 1 ]]
then
  echo "Launch command: java -jar build/libs/agora-back-0.0.1.jar --run-custom-command=$1 $2"
  java -jar build/libs/agora-back-0.0.1.jar --run-custom-command=$1 $2
elif [[  $# == 1 ]]
then
  echo "Launch command: java -jar build/libs/agora-back-0.0.1.jar --run-custom-command=$1"
  java -jar build/libs/agora-back-0.0.1.jar --run-custom-command=$1
else
  echo "This script needs at least 1 argument !"
fi