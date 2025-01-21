package com.example.diary.user_group.repository;

import com.example.diary.user_group.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    @Query("select ug from UserGroup ug join fetch ug.group where ug.user.id = :userId")
    List<UserGroup> findByUserId(@Param("userId") Long userId);

    @Query("select ug from UserGroup ug join fetch ug.user where ug.group.id = :groupId")
    List<UserGroup> findByGroupId(@Param("groupId") Long groupId);

    void deleteAllByGroupId(Long groupId);

    Optional<UserGroup> findByUserIdAndGroupId(Long userId, Long groupId);

    void deleteAllByUserId(Long userId);

    @Query("SELECT g.id FROM UserGroup ug JOIN ug.group g WHERE ug.user.id = :userId")
    List<Long> findGroupIdsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM UserGroup ug WHERE ug.group.id IN :groupIds")
    void deleteByGroupIds(@Param("groupIds") List<Long> groupIds);
}
