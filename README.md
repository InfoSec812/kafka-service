# Vert.x Kafka Service And Client Library

This project is a microservice which uses the Vert.x EventBus to allow other Vert.x
microservices to publish messages to a Kafka Topic stream. This makes use of the Vert.x
[Service Proxy](https://vertx.io/docs/vertx-service-proxy/java/) capabilities in order
to present a simple and effective API for all other applications using it.

## Prerequisites
* JDK (OpenJDK or Oracle JDK) >= 1.8
* Docker >= 1.6.0 (Used in the integration tests)

## Build
```
./mvn clean package
```

## Client Library Usage

Add the library to your dependencies:

```xml
<dependency>
  <groupId>com.redhat.qcon</groupId>
  <artifactId>kafka-service</artifactId>
  <version>1.0.0</version>
  <classifier>client</classifier>
  <exclusions>
    <!-- Exclude these transitive dependencies as they are not needed for the client -->
    <exclusion>
      <groupId>io.vertx</groupId>
      <artifactId>vertx-kafka-client</artifactId>
    </exclusion>
    <exclusion>
      <groupId>io.vertx</groupId>
      <artifactId>vertx-web-api-contract</artifactId>
    </exclusion>
    <exclusion>
      <groupId>io.vertx</groupId>
      <artifactId>vertx-rx-java2</artifactId>
    </exclusion>
    <exclusion>
      <groupId>io.vertx</groupId>
      <artifactId>vertx-web-client</artifactId>
    </exclusion>
    <exclusion>
      <groupId>io.vertx</groupId>
      <artifactId>vertx-config-kubernetes-configmap</artifactId>
    </exclusion>
    <exclusion>
      <groupId>io.vertx</groupId>
      <artifactId>vertx-infinispan</artifactId>
    </exclusion>
  </exclusions>
</dependency>
```

```java
// Inside a Vert.x Vertical
KafkaService service = KafkaService.createProxy(vertx, address);
service.publish(myJsonData, result -> {
  // Handle results
});
```