package com.kaesoron.MockManager.controllersTests;

import com.kaesoron.MockManager.controllers.JournalController;
import com.kaesoron.MockManager.dao.JournalDAO;
import com.kaesoron.MockManager.models.Journal;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class JournalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JournalDAO journalDAO;

    @InjectMocks
    private JournalController journalController;

    @Test
    public void testShowJournalPage() throws Exception {
        // Создание пустой страницы, которая будет возвращена
        Page<Journal> emptyPage = new PageImpl<>(Collections.emptyList());

        // Настройка моков
        when(journalDAO.search(any(), any(), any(), any(), any())).thenReturn(emptyPage);

        // Выполнение запроса
        mockMvc.perform(get("/journal"))
                .andExpect(status().isOk())
                .andExpect(view().name("journal"))
                .andExpect(model().attributeExists("journals"))
                .andExpect(model().attribute("journals", hasProperty("content", empty())));  // Проверка, что содержимое страницы пусто
    }

    @Test
    public void testShowJournalPageWithParameters() throws Exception {
        Journal mockJournal = new Journal(); // Предполагаем, что конструктор устанавливает некоторые начальные значения
        PageImpl<Journal> page = new PageImpl<>(Collections.singletonList(mockJournal));

        when(journalDAO.search(any(), any(), any(), any(), any())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/journal")
                        .param("mockName", "testName")
                        .param("method", "GET")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "journalDateTime"))
                .andExpect(status().isOk())
                .andExpect(view().name("journal"))
                .andExpect(model().attributeExists("journals"))
                .andExpect(model().attribute("journals", page));
    }

    @Test
    public void testJournalPageWithErrorParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/journal")
                        .param("size", "-1"))  // Попытка задать недопустимое значение размера страницы
                .andExpect(status().isBadRequest());  // Ожидается ошибка в запросе
    }

    @Test
    public void testJournalPageWithDefaultParameters() throws Exception {
        Page<Journal> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10, Sort.by("journalDateTime").descending()), 0);
        when(journalDAO.search(any(), any(), any(), any(), any())).thenReturn(mockPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/journal"))
                .andExpect(status().isOk())
                .andExpect(view().name("journal"))
                .andExpect(model().attributeExists("journals"))
                .andExpect(model().attribute("journals", hasProperty("content", is(empty()))))
                .andExpect(model().attribute("journals", hasProperty("totalPages", is(0)))); // Пример дополнительных проверок
    }
}
