package com.example.diary.user_group.repository;

import com.example.diary.user_group.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    @Query("select ug from UserGroup ug join fetch ug.group where ug.user.id = :userId")
    List<UserGroup> findByUserId(@Param("userId") Long userId);

    List<UserGroup> findByGroupId(Long groupId);

    void deleteByGroupId(Long groupId);

}
