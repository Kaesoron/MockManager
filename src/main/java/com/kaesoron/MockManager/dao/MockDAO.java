package com.kaesoron.MockManager.dao;

import com.kaesoron.MockManager.enums.Actions;
import com.kaesoron.MockManager.models.Mock;
import com.kaesoron.MockManager.repository.MockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MockDAO {

    @Autowired
    private MockRepository mockRepository;
    @Autowired
    private JournalDAO journalDAO;

    public List<Mock> index() {
        return mockRepository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Page<Mock> indexMocksWithPaginationAndSorting(int offset, int pageSize, String sortBy) {
        // Создание объекта PageRequest с указанием сортировки
        PageRequest pageRequest = PageRequest.of(offset, pageSize, Sort.by(sortBy));
        // Вызов метода findAll с созданным объектом PageRequest
        return mockRepository.findAll(pageRequest);
    }

    @Transactional
    public void createOrUpdate(Mock mock) {
        Optional<Mock> existingMock = mockRepository.findById(mock.getMockId());
        if (mock.getMockId() != 0 && existingMock.isPresent()) {
            Mock mockToUpdate = existingMock.get();
//             Если ID существует и мок с таким ID найден в базе данных, обновляем его
            mockToUpdate.setMockName(mock.getMockName());
            mockToUpdate.setMockMethod(mock.getMockMethod());
            mockToUpdate.setMockPath(mock.getMockPath());
            mockToUpdate.setMockTimeout(mock.getMockTimeout());
            mockToUpdate.setMockResponse(mock.getMockResponse());
            mockToUpdate.setMockDate(Calendar.getInstance()); // Обновляем дату на текущую
            mockRepository.save(mockToUpdate); // Сохраняем изменения
            journalDAO.create(mockToUpdate, Actions.MODIFIED);
        } else {
            // Если мок с таким ID не найден, создаем новый
            mock.setMockDate(Calendar.getInstance()); // Устанавливаем дату создания на текущую
            mockRepository.save(mock);
            journalDAO.create(mock, Actions.NOT_FOUND);
        }
    }

    public Optional<Mock> read(long mockId) {
        return mockRepository.findById(mockId);
    }

    public Optional<Mock> readByRequest(String mockPath) {
        return mockRepository.findByMockPath(mockPath);
    }

    public Optional<Mock> readByRequest(String mockPath, String mockMethod) {
        return mockRepository.findByMockPathAndMockMethod(mockPath, mockMethod);
    }

    @Transactional
    public void delete(Long mockId) {
        Mock mockToDelete = mockRepository.findById(mockId).orElse(null);
        if (mockToDelete != null) {
            mockRepository.delete(mockToDelete);
            journalDAO.create(mockToDelete, Actions.DELETED);
        }
    }
}
