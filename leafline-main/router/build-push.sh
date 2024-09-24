IMAGE=eu.gcr.io/mightycode/cpoo_router
mvn package -DskipTests=true
docker build . -t $IMAGE
docker push $IMAGE
