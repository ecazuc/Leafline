#!/bin/bash

displayStatus() {
		if [ $? -ne 0 ]
		then
				echo "FAILED"
				echo "(see $OUTPUT for more info)"
				exit 1
		else
				echo "OK"
		fi
}

OUTPUT=$(mktemp)
DIR=$PWD
DOMAIN=$1

echo "Checking $DOMAIN repo..."

echo -n "Check if server compiles: "
cd $DIR/server
mvn package -DskipTests=true &> $OUTPUT
displayStatus

echo -n "Check if server unit tests run: "
cd $DIR/server
mvn test &> $OUTPUT
displayStatus
	
echo -n "Generate API client for end-to-end tests: "
cd $DIR/api
bash generate*.sh &> $OUTPUT
displayStatus

echo -n "Check if server end-to-end tests run: "
cd $DIR/server
mvn spring-boot:run &> $OUTPUT &
sleep 5
cd $DIR/api/e2etests
mvn test &> $OUTPUT
displayStatus
kill %1

echo -n "Check if client compiles: "
cd $DIR/client
npm install &> $OUTPUT
npm run build &> $OUTPUT
displayStatus

echo -n "Check if client unit tests run: "
cd $DIR/client
ng test --watch=false &> $OUTPUT
displayStatus
