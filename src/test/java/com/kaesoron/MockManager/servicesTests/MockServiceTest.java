package com.kaesoron.MockManager.servicesTests;

import com.kaesoron.MockManager.dao.MockDAO;
import com.kaesoron.MockManager.models.Mock;
import com.kaesoron.MockManager.repository.MockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MockServiceTest {

    @MockBean
    private MockRepository mockRepository;

    @Autowired
    private MockDAO mockDAO;

    @Test
    public void testCreateOrUpdate() {
        Mock mock = new Mock();
        mock.setMockId(1L);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(mock));

        mockDAO.createOrUpdate(mock);

        verify(mockRepository).save(mock);
    }
}
