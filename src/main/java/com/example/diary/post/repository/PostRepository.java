package com.example.diary.post.repository;

import com.example.diary.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByGroupId(Long groupId);

    void deleteAllByUserIdAndGroupId(Long userId, Long groupId);
}
