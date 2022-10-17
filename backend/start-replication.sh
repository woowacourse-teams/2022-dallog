./gradlew bootJar

java -jar -Dspring.profiles.active=local -Dspring.config.location=./application-replication.yml ./build/libs/backend-0.0.1-SNAPSHOT.jar
