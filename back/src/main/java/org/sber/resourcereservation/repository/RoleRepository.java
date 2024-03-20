package org.sber.resourcereservation.repository;

import org.sber.resourcereservation.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с ролями пользователей.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Находит роль по её имени.
     *
     * @param name Имя роли, которую необходимо найти.
     * @return Найденная роль.
     */
    Role findByName(String name);
}
