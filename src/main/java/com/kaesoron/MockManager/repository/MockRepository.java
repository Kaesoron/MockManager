package com.kaesoron.MockManager.repository;

import com.kaesoron.MockManager.models.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MockRepository extends JpaRepository<Mock, Long> {

    Mock findByMockPath(String path);
}
