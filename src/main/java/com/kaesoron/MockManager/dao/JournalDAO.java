package com.kaesoron.MockManager.dao;

import com.kaesoron.MockManager.models.Journal;
import com.kaesoron.MockManager.models.Mock;
import com.kaesoron.MockManager.repository.JournalRepository;
import enums.Actions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class JournalDAO {
    @Autowired
    private JournalRepository journalRepository;

    public List<Journal> index() {
        return journalRepository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Transactional
    public void create(Mock mock, Actions action) {
        journalRepository.save(new Journal(mock, action));
    }
}
