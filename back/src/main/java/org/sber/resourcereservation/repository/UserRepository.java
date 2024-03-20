package org.sber.resourcereservation.repository;

import org.sber.resourcereservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Репозиторий для работы с пользователями.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по его имени.
     *
     * @param name Имя пользователя, которое необходимо найти.
     * @return Найденный пользователь.
     */
    User findByName(String name);

    /**
     * Проверяет существование пользователя по его имени.
     *
     * @param name Имя пользователя, для которого осуществляется проверка.
     * @return true, если пользователь с таким именем существует, иначе false.
     */
    boolean existsByName(String name);

    /**
     * Находит пользователя с его ролями по имени.
     *
     * @param name Имя пользователя, для которого осуществляется поиск с ролями.
     * @return Найденный пользователь с его ролями.
     */
    @Query("select u from users u join fetch u.authorities where u.name = :name")
    User findByNameWithRoles(@Param("name") String name);
}
