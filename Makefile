start:
	JAVA_HOME=/opt/openjdk-bin-25 ./mvnw quarkus:dev -X
build:
	JAVA_HOME=/opt/openjdk-bin-25 ./mvnw package -X
start_jar:
	JAVA_HOME=/opt/openjdk-bin-25 java -jar target/*-runner.jar
build_native:
	JAVA_HOME=/opt/graalvm-jdk-25 ./mvnw package -Dnative
start_native:
	target/quarkus-crud-demo-1.0.0-SNAPSHOT-runner