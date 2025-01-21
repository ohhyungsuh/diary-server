package com.example.diary.group.repository;

import com.example.diary.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByName(String name);
}
