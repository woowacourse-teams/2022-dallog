./gradlew bootJar

java -jar -Dspring.profiles.active=prod -Dspring.config.location=./application-multi-slave-replication.yml ./build/libs/backend-0.0.1-SNAPSHOT.jar