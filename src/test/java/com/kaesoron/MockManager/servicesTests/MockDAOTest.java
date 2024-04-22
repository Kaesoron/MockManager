package com.kaesoron.MockManager.servicesTests;

import com.kaesoron.MockManager.dao.JournalDAO;
import com.kaesoron.MockManager.dao.MockDAO;
import com.kaesoron.MockManager.enums.Actions;
import com.kaesoron.MockManager.models.Mock;
import com.kaesoron.MockManager.repository.MockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MockDAOTest {

    @org.mockito.Mock
    private MockRepository mockRepository;

    @org.mockito.Mock
    private JournalDAO journalDAO;

    @InjectMocks
    private MockDAO mockDAO;

    @Test
    public void testIndex() {
        Mock mock1 = new Mock();
        Mock mock2 = new Mock();
        when(mockRepository.findAll()).thenReturn(Arrays.asList(mock1, mock2));

        List<Mock> result = mockDAO.index();

        assertEquals(2, result.size());
        assertTrue(result.contains(mock1));
        assertTrue(result.contains(mock2));
    }

    @Test
    public void testCreateOrUpdate_ExistingMock() {
        // Подготовка
        long mockId = 1L;
        Mock existingMock = new Mock();
        existingMock.setMockId(mockId);
        existingMock.setMockName("Existing Name");

        when(mockRepository.findById(mockId)).thenReturn(Optional.of(existingMock));

        // Действие
        Mock updatedMock = new Mock();
        updatedMock.setMockId(mockId);
        updatedMock.setMockName("Updated Name");
        mockDAO.createOrUpdate(updatedMock);

        // Проверка
        ArgumentCaptor<Mock> mockCaptor = ArgumentCaptor.forClass(Mock.class);
        verify(mockRepository).save(mockCaptor.capture());

        Mock savedMock = mockCaptor.getValue();
        assertEquals("Updated Name", savedMock.getMockName());
        assertEquals(mockId, savedMock.getMockId());
    }

    @Test
    public void testCreateOrUpdate_NewMock() {
        Mock newMock = new Mock();
        newMock.setMockName("New Mock");

        when(mockRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(mockRepository.save(any(Mock.class))).thenReturn(newMock);

        mockDAO.createOrUpdate(newMock);

        verify(mockRepository).save(newMock);
        verify(journalDAO).create(newMock, Actions.CREATED);
    }

    @Test
    public void testDelete() {
        Mock mockToDelete = new Mock();
        mockToDelete.setMockId(1L);

        when(mockRepository.findById(1L)).thenReturn(Optional.of(mockToDelete));

        mockDAO.delete(1L);

        verify(mockRepository).delete(mockToDelete);
        verify(journalDAO).create(mockToDelete, Actions.DELETED);
    }
}