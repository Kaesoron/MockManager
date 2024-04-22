package com.kaesoron.MockManager.controllersTests;

import com.kaesoron.MockManager.controllers.MainController;
import com.kaesoron.MockManager.dao.JournalDAO;
import com.kaesoron.MockManager.dao.MockDAO;
import com.kaesoron.MockManager.enums.Actions;
import com.kaesoron.MockManager.models.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class MainControllerTest {
    @org.mockito.Mock
    private MockDAO mockDAO;

    @org.mockito.Mock
    private JournalDAO journalDAO;

    @InjectMocks
    private MainController mainController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // mainController = new MainController(); // This line should be removed, @InjectMocks should handle this
    }

    @Test
    void testHandleRequestMatchingMock() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/test");

        Mock mock = mock(Mock.class); // Правильное использование мока для абстрактного класса
        when(mock.getMockPath()).thenReturn("test");
        when(mock.getMockMethod()).thenReturn("GET");
        when(mock.getMockResponse()).thenReturn("Mock response");
        when(mock.getMockTimeout()).thenReturn(100); // Предполагаем, что задержка есть

        when(mockDAO.readByRequest("test", "GET")).thenReturn(Optional.of(mock));

        ResponseEntity<String> response = mainController.handleRequest(request);

        assertEquals("Mock response", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(journalDAO).create(mock, Actions.RESPONSE);
    }

    @Test
    void testHandleRequestNoMatchingMock() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/test");

        when(mockDAO.readByRequest("test")).thenReturn(Optional.empty());

        ResponseEntity<String> response = mainController.handleRequest(request);

        verify(journalDAO).create(any(Mock.class), eq(Actions.NOT_FOUND));
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testHandleRequestWithInterrupt() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/test");

        Mock mock = mock(Mock.class);
        when(mock.getMockPath()).thenReturn("test");
        when(mock.getMockMethod()).thenReturn("GET");
        when(mock.getMockResponse()).thenReturn("Delayed response");
        when(mock.getMockTimeout()).thenReturn(100); // Use correct return type

        when(mockDAO.readByRequest("test")).thenReturn(Optional.of(mock));

        ResponseEntity<String> response = mainController.handleRequest(request);
        assertNull(response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }
}