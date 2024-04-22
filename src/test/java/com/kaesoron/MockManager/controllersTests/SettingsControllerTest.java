package com.kaesoron.MockManager.controllersTests;

import com.kaesoron.MockManager.controllers.SettingsController;
import com.kaesoron.MockManager.dao.MockDAO;
import com.kaesoron.MockManager.models.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SettingsControllerTest {
    private MockMvc mockMvc;

    @org.mockito.Mock
    private MockDAO mockDAO;

    @InjectMocks
    private SettingsController settingsController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(settingsController).build();
    }

    @Test
    public void testNewMock() throws Exception {
        mockMvc.perform(get("/settings/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("mock"))
                .andExpect(view().name("edit-mock"));
    }

    @Test
    public void testEditMockPageExistingMock() throws Exception {
        Mock mock = mock(Mock.class);
        when(mockDAO.read(1L)).thenReturn(Optional.of(mock));

        mockMvc.perform(get("/settings/edit/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("mock"))
                .andExpect(view().name("edit-mock"));
    }

    @Test
    public void testEditMockPageNonExistingMock() throws Exception {
        when(mockDAO.read(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/settings/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings"));
    }

    @Test
    public void testSaveMock() throws Exception {
        mockMvc.perform(post("/settings/save")
                        .param("mockName", "Test Mock")
                        .param("mockMethod", "GET"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings"));
        verify(mockDAO).createOrUpdate(any(Mock.class));
    }

    @Test
    public void testDeleteMock() throws Exception {
        mockMvc.perform(post("/settings/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings"));
        verify(mockDAO).delete(1L);
    }
}