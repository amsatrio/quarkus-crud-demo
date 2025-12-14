start:
	JAVA_HOME=/opt/openjdk-bin-21 ./mvnw quarkus:dev
build:
	JAVA_HOME=/opt/openjdk-bin-21 mvn package -X
start_jar:
	JAVA_HOME=/opt/openjdk-bin-21 java -jar target/*-runner.jar
build_native:
	JAVA_HOME=/opt/graalvm-jdk-21 ./mvnw package -Dnative
start_native:
	target/quarkus-crud-demo-1.0.0-SNAPSHOT-runner