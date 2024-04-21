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

    public List<Mock> indexWithSorting(String sortBy) {
        return mockRepository.findAll(Sort.by(sortBy));

    }

    public Page<Mock> indexMocksWithPagination(int offset, int pageSize) {
        return mockRepository.findAll(PageRequest.of(offset, pageSize));
    }

    public Page<Mock> indexMocksWithPaginationAndSorting(int offset, int pageSize, String sortBy) {
        return mockRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(sortBy)));
    }


    @Transactional
    public void create(Mock mock) {
        mockRepository.save(mock);
        journalRepository.save(new Journal(mock, Actions.CREATED));
    }

    public Mock read(long mockId) {
        return mockRepository.getReferenceById(mockId);
    }

    public Mock readByRequest(String mockPath) {
        return mockRepository.findByMockPath(mockPath);
    }

    @Transactional
    public void update(long mockId, Mock mock) {
        Mock mockToBeUpdated = mockRepository.getReferenceById(mockId);
        mockToBeUpdated.setMockName(mock.getMockName());
        mockToBeUpdated.setMockMethod(mock.getMockMethod());
        mockToBeUpdated.setMockPath(mock.getMockPath());
        mockToBeUpdated.setMockTimeout(mock.getMockTimeout());
        mockToBeUpdated.setMockResponse(mock.getMockResponse());
        mockToBeUpdated.setMockDate(Calendar.getInstance());
        mockRepository.save(mockToBeUpdated);
        journalRepository.save(new Journal(mockToBeUpdated, Actions.MODIFIED));
    }

    @Transactional
    public void delete(Mock mock) {
        mockRepository.delete(mock);
        journalRepository.save(new Journal(mock, Actions.DELETED));
    }


}
