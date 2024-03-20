package org.sber.resourcereservation.repository;

import org.sber.resourcereservation.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с ресурсами.
 */
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    /**
     * Находит ресурс по его имени.
     *
     * @param name Имя ресурса, которое необходимо найти.
     * @return Найденный ресурс.
     */
    Resource findByName(String name);
}
