package com.kaesoron.MockManager.dao;

import com.kaesoron.MockManager.models.Journal;
import com.kaesoron.MockManager.models.Mock;
import com.kaesoron.MockManager.repository.JournalRepository;
import com.kaesoron.MockManager.repository.MockRepository;
import enums.Actions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MockDAO {

    @Autowired
    private MockRepository mockRepository;
    @Autowired
    private JournalRepository journalRepository;

    public List<Mock> index() {
        return mockRepository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Page<Mock> indexMocksWithPaginationAndSorting(int offset, int pageSize, String sortBy) {
        return mockRepository.findAll(PageRequest.of(offset - 1, pageSize, Sort.by(sortBy)));
    }

    @Transactional
    public void createOrUpdate(Mock mock) {
        if (mockRepository.existsById(mock.getMockId())) {
            Mock existingMock = mockRepository.findById(mock.getMockId()).orElse(null);
            if (existingMock != null) {
                existingMock.setMockName(mock.getMockName());
                existingMock.setMockMethod(mock.getMockMethod());
                existingMock.setMockPath(mock.getMockPath());
                existingMock.setMockTimeout(mock.getMockTimeout());
                existingMock.setMockResponse(mock.getMockResponse());
                // Consider whether updating the date here is required
                existingMock.setMockDate(Calendar.getInstance()); // Potentially update this only on creation
                mockRepository.save(existingMock);
                journalRepository.save(new Journal(existingMock, Actions.MODIFIED));
            }
        } else {
            mock.setMockDate(Calendar.getInstance()); // Set date on creation
            mockRepository.save(mock);
            journalRepository.save(new Journal(mock, Actions.CREATED));
        }
    }

    public Mock read(long mockId) {
        return mockRepository.findById(mockId).orElse(null);
    }

    public Mock readByRequest(String mockPath) {
        return mockRepository.findByMockPath(mockPath).orElse(null);    }

    @Transactional
    public void delete(Long mockId) {
        Mock mockToDelete = mockRepository.findById(mockId).orElse(null);
        if (mockToDelete != null) {
            mockRepository.delete(mockToDelete);
            journalRepository.save(new Journal(mockToDelete, Actions.DELETED));
        }
    }
}
