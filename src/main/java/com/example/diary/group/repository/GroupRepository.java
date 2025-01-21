package com.example.diary.group.repository;

import com.example.diary.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByName(String name);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Group g WHERE g.id IN :groupIds")
    void deleteByGroupIds(@Param("groupIds") List<Long> groupIds);

}
