# /bin/bash

if [[ $# -ge 1 ]]
then
  echo "Launch command: java -jar build/libs/agora-back-0.0.1.jar --run-custom-command=$@"
  java -jar build/libs/agora-back-0.0.1.jar --run-custom-command=$@
else
  echo "This script needs at least 1 argument !"
fi