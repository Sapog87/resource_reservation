package org.sber.resourcereservation.repository;

import org.sber.resourcereservation.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String user);
}
