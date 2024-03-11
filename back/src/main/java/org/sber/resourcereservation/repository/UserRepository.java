package org.sber.resourcereservation.repository;

import org.sber.resourcereservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);

    boolean existsByName(String name);

    @Query("select u from users u join fetch u.authorities where u.name = :name")
    User findByNameWithRoles(@Param("name") String name);
}
