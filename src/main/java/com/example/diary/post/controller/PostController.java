package com.example.diary.post.controller;

import com.example.diary.global.response.ApiResponse;
import com.example.diary.post.domain.dto.WritePostDto;
import com.example.diary.post.domain.dto.PostDetailDto;
import com.example.diary.post.domain.dto.PostDto;
import com.example.diary.post.service.PostService;
import com.example.diary.user.session.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{groupId}")
public class PostController {

    private final PostService postService;

    // 게시글 작성
    @PostMapping
    public ApiResponse<PostDto> createPost(@PathVariable("groupId") Long groupId,
                                           @RequestBody WritePostDto writePostDto, HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        PostDto postDto = postService.createPost(userId, groupId, writePostDto);
        return new ApiResponse<>(HttpStatus.CREATED, postDto);
    }

    // 게시글 전체 조회
    @GetMapping
    public ApiResponse<PostDto> getPosts(@PathVariable("groupId") Long groupId, HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        List<PostDto> posts = postService.getPosts(userId, groupId);
        return new ApiResponse<>(HttpStatus.OK, posts);
    }

    // 게시글 조회
    @GetMapping("/{postId}")
    public ApiResponse<PostDetailDto> getPost(@PathVariable("groupId") Long groupId,
                                              @PathVariable("postId") Long postId, HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        PostDetailDto post = postService.getPost(userId, groupId, postId);
        return new ApiResponse<>(HttpStatus.OK, post);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ApiResponse<PostDto> updatePost(@PathVariable("groupId") Long groupId,
                                           @PathVariable("postId") Long postId,
                                           @RequestBody WritePostDto writePostDto, HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        PostDto postDto = postService.updatePost(userId, groupId, postId, writePostDto);
        return new ApiResponse<>(HttpStatus.OK, postDto);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<?> deletePost(@PathVariable("groupId") Long groupId,
                                           @PathVariable("postId") Long postId, HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        postService.deletePost(userId, groupId, postId);
        return new ApiResponse<>(HttpStatus.OK);
    }

    // 게시글 전체 삭제
    @DeleteMapping
    public ApiResponse<?> deleteAllPost(@PathVariable("groupId") Long groupId, HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        postService.deleteAllPost(userId, groupId);
        return new ApiResponse<>(HttpStatus.OK);
    }

    // todo 게시글 선택 삭제

    // 게시글 강제 삭제
    @DeleteMapping("/{postId}/force")
    public ApiResponse<?> deletePostByForce(@PathVariable("groupId") Long groupId,
                                     @PathVariable("postId") Long postId, HttpServletRequest request) {
        Long adminId = SessionUtils.getUserIdBySession(request);
        postService.deletePostByForce(adminId, groupId, postId);
        return new ApiResponse<>(HttpStatus.OK);
    }
}
