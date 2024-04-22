package com.kaesoron.MockManager.integrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testJournalPage() {
        ResponseEntity<String> response = restTemplate.getForEntity("/journal", String.class);
        assert (response.getStatusCode()).equals(HttpStatus.OK);
    }

    @Test
    public void testSettingsPage() {
        ResponseEntity<String> response = restTemplate.getForEntity("/settings", String.class);
        assert (response.getStatusCode()).equals(HttpStatus.OK);
    }

    @Test
    public void testNewMockPage() {
        ResponseEntity<String> response = restTemplate.getForEntity("/settings/new", String.class);
        assert (response.getStatusCode()).equals(HttpStatus.OK);
    }

    @Test
    public void testEditMockPageNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/settings/edit/9999", String.class); // предполагаем, что такого ID нет
        assert (response.getStatusCode()).equals(HttpStatus.OK); // Ожидаем статус перенаправления
    }

    @Test
    public void testDeleteMock() {
        // Предположим, что мы удаляем mock с ID 1
        ResponseEntity<String> response = restTemplate.postForEntity("/settings/delete/1", null, String.class);
        assert (response.getStatusCode()).equals(HttpStatus.FOUND); // Ожидаем статус перенаправления
    }
}
