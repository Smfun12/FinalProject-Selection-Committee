package com.example.FinalProject.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @Autowired
    private TestRestTemplate template;


    @Test
    public void shouldReturnMainPage() {
        ResponseEntity<String> result = template.withBasicAuth("admin", "admin")
                .getForEntity("/", String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    public void shouldReturnStudentList() {
        ResponseEntity<String> result = template.withBasicAuth("admin", "admin")
                .getForEntity("/findStudent", String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }

}
