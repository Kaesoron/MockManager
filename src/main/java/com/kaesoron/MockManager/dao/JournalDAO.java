package com.kaesoron.MockManager.dao;

import com.kaesoron.MockManager.enums.Actions;
import com.kaesoron.MockManager.models.Journal;
import com.kaesoron.MockManager.models.Mock;
import com.kaesoron.MockManager.repository.JournalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class JournalDAO {
    @Autowired
    private JournalRepository journalRepository;

    public List<Journal> index() {
        return journalRepository.findAll();
    }

    @Transactional
    public void create(Mock mock, Actions action) {
        Journal journal = new Journal(mock, action);
        System.out.printf("Creating journal entry: {%s}", journal);
        journalRepository.save(journal);
        System.out.println("Journal entry saved.");
    }

    public Page<Journal> search(String mockName, Long mockId, String method, Date date, Pageable pageable) {
        Page<Journal> journals = journalRepository.findAll(pageable);
        if (journals.isEmpty()) {
            return new PageImpl<>(Collections.emptyList());
        }
        return journals;
    }
}
