package ru.netology.springbootdemo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Container
    private final GenericContainer<?> devapp = new GenericContainer<>("devapp:latest")
           .withExposedPorts(8080);

    @Container
    private final GenericContainer<?> prodapp  = new GenericContainer<>("prodapp:latest")
            .withExposedPorts(8081);

    @BeforeEach
    void setUp() {
        devapp.start();
        prodapp.start();

    }

    @Test
    void contextLoads() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://192.168.99.100:" + devapp.getMappedPort(8080) + "/profile", String.class);
        System.out.println("1: " + forEntity.getBody());
        assertEquals("Current profile is dev", forEntity.getBody());

        ResponseEntity<String> forEntity2 = restTemplate.getForEntity("http://192.168.99.100:" + prodapp.getMappedPort(8081) + "/profile", String.class);
        System.out.println("2: " + forEntity2.getBody());
        assertEquals("Current profile is production", forEntity2.getBody());

    }

}

