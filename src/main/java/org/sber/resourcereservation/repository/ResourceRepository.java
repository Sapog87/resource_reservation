package org.sber.resourcereservation.repository;

import org.sber.resourcereservation.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Resource findByName(String name);
}
