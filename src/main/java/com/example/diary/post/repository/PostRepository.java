package com.example.diary.post.repository;

import com.example.diary.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByGroupId(Long groupId);

    void deleteAllByUserIdAndGroupId(Long userId, Long groupId);

    void deleteAllByGroupId(Long groupId);

    void deleteAllByUserId(Long userId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Post p WHERE p.group.id IN :groupIds")
    void deleteByGroupIds(@Param("groupIds") List<Long> groupIds);
}
