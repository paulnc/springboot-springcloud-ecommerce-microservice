package com.ecomm.microservices.order;

import org.springframework.boot.test.context.SpringBootTest;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;

import static org.hamcrest.MatcherAssert.assertThat;


//@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Testcontainers
class OrderServiceApplicationTests {


    @ServiceConnection
    //@Container
    private static final MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:latest")
            .withDatabaseName("order_service")
            .withUsername("root")
            .withPassword("mysql")
            .withStartupTimeoutSeconds(720)
            .withConnectTimeoutSeconds(720)
            //.withReuse(true)
            ;


    @DynamicPropertySource
    private static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }


    @Test
    void contextLoads() throws Exception {
    }


    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mySQLContainer.start();
    }

    @Test
    void shouldSubmitOrder() {
        String submitOrderJson = """
                {
                "skuCode": "iphone_15",
                "price": 1000,
                "quantity": 1
                }
                """;
        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .body(submitOrderJson)
                .when()
                .post("/api/order")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body().asString();
        assertThat(responseBodyString, Matchers.is("Order Placed Successfully"));
    }
}


