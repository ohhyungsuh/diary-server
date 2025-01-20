package com.example.diary.user_group.repository;

import com.example.diary.user_group.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
}
