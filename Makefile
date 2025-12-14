start:
	JAVA_HOME=/opt/graalvm-jdk-21.0.6+8.1 ./mvnw quarkus:dev
build:
	JAVA_HOME=/opt/graalvm-jdk-21.0.6+8.1 ./mvnw package -X
build_native:
	JAVA_HOME=/opt/graalvm-jdk-21.0.6+8.1 ./mvnw package -Dnative