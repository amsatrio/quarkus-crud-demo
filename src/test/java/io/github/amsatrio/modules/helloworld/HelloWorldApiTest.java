package io.github.amsatrio.modules.helloworld;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class HelloWorldApiTest {
    private String baseUrl = "/v1/hello-world";

    @Test
    void testHelloEndpoint() {
        given().accept(ContentType.JSON)
                .when().get(baseUrl + "/hello")
                .then()
                .statusCode(200)
                .body("status", is(200))
                .body("message", is("success"))
                .body("data", is("Hello world"));
    }
}
