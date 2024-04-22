//package com.kaesoron.MockManager.controllersTests;
//
//import com.kaesoron.MockManager.controllers.MainController;
//import com.kaesoron.MockManager.dao.JournalDAO;
//import com.kaesoron.MockManager.dao.MockDAO;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockHttpServletRequest;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class MainControllerTest {
//    @Mock
//    private MockDAO mockDAO;
//
//    @Mock
//    private JournalDAO journalDAO;
//
//    @InjectMocks
//    private MainController mainController;
//
//    public MainControllerTest() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testHandleRequestMatchingMock() {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.setMethod("GET");
//        request.setRequestURI("/test");
//
//        Mock mock = new Mock();
//        mock.setMockPath("test");
//        mock.setMockMethod("GET");
//        mock.setMockResponse("Mock response");
//        mock.setMockTimeout(0L);
//
//        when(mockDAO.readByRequest("test")).thenReturn(Optional.of(mock));
//
//        ResponseEntity<String> response = mainController.handleRequest(request);
//
//        verify(journalDAO).create(mock, Actions.RESPONSE);
//        assertEquals("Mock response", response.getBody());
//        assertEquals(200, response.getStatusCodeValue());
//    }
//
//    @Test
//    void testHandleRequestNoMatchingMock() {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.setMethod("POST");
//        request.setRequestURI("/test");
//
//        when(mockDAO.readByRequest("test")).thenReturn(Optional.empty());
//
//        ResponseEntity<String> response = mainController.handleRequest(request);
//
//        verify(journalDAO).create(any(Mock.class), eq(Actions.NOT_FOUND));
//        assertEquals(404, response.getStatusCodeValue());
//    }
//
//    @Test
//    void testHandleRequestWithInterrupt() throws Exception {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.setMethod("GET");
//        request.setRequestURI("/test");
//
//        Mock mock = new Mock();
//        mock.setMockPath("test");
//        mock.setMockMethod("GET");
//        mock.setMockResponse("Delayed response");
//        mock.setMockTimeout(100L);
//
//        when(mockDAO.readByRequest("test")).thenReturn(Optional.of(mock));
//
//        Thread thread = new Thread(() -> {
//            ResponseEntity<String> response = mainController.handleRequest(request);
//            assertEquals("Delayed response", response.getBody());
//        });
//        thread.start();
//        thread.interrupt();
//        thread.join();
//
//        verify(journalDAO).create(mock, Actions.RESPONSE);
//        verify(journalDAO, times(1)).create(mock, Actions.NOT_FOUND);
//    }
//}
