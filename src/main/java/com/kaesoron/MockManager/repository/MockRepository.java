package com.kaesoron.MockManager.repository;

import com.kaesoron.MockManager.models.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MockRepository extends JpaRepository<Mock, Long> {

    /**
     * Находит мок по заданному пути.
     * @param path Путь мока.
     * @return Optional содержащий найденный мок, если такой есть.
     */
    Optional<Mock> findByMockPath(String path);

    Optional<Mock> findByMockPathAndMockMethod(String path, String method);
}
