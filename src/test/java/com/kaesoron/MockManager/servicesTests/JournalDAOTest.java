package com.kaesoron.MockManager.servicesTests;

import com.kaesoron.MockManager.dao.JournalDAO;
import com.kaesoron.MockManager.enums.Actions;
import com.kaesoron.MockManager.models.Journal;
import com.kaesoron.MockManager.models.Mock;
import com.kaesoron.MockManager.repository.JournalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JournalDAOTest {

    @org.mockito.Mock
    private JournalRepository journalRepository;

    @InjectMocks
    private JournalDAO journalDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIndex() {
        Journal journal = new Journal();
        when(journalRepository.findAll()).thenReturn(Collections.singletonList(journal));

        List<Journal> result = journalDAO.index();

        assertEquals(1, result.size());
        assertEquals(journal, result.get(0));
        verify(journalRepository).findAll();
    }

    @Test
    public void testCreate() {
        Mock mock = new Mock();
        Actions action = Actions.CREATED;
        journalDAO.create(mock, action);

        ArgumentCaptor<Journal> captor = ArgumentCaptor.forClass(Journal.class);
        verify(journalRepository).save(captor.capture());
        Journal capturedJournal = captor.getValue();

        assertEquals(mock.getMockName(), capturedJournal.getJournalMockName());
        assertEquals(mock.getMockMethod(), capturedJournal.getJournalMockMethod());
        assertEquals(mock.getMockPath(), capturedJournal.getJournalMockRequest());
        assertEquals(mock.getMockResponse(), capturedJournal.getJournalMockResponse());
        assertEquals(action, capturedJournal.getAction());
    }

    @Test
    public void testSearch() {
        PageRequest pageable = PageRequest.of(0, 10);
        Journal journal = new Journal();
        Page<Journal> page = new PageImpl<>(Collections.singletonList(journal));

        when(journalRepository.findAll(pageable)).thenReturn(page);

        Page<Journal> result = journalDAO.search(null, null, null, null, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(journal, result.getContent().get(0));
        verify(journalRepository).findAll(pageable);
    }
}
