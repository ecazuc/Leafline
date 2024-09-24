IMAGE=eu.gcr.io/mightycode/cpoo_server
mvn package -DskipTests=true
docker build . -t $IMAGE
docker push $IMAGE
