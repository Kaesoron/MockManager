package com.kaesoron.MockManager.repository;

import com.kaesoron.MockManager.models.Journal;
import com.kaesoron.MockManager.models.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
}
