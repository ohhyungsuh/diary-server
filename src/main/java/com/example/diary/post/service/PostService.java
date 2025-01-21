package com.example.diary.post.service;

import com.example.diary.group.domain.Group;
import com.example.diary.group.exception.GroupErrorCode;
import com.example.diary.group.exception.GroupException;
import com.example.diary.group.repository.GroupRepository;
import com.example.diary.post.domain.Post;
import com.example.diary.post.domain.dto.WritePostDto;
import com.example.diary.post.domain.dto.PostDetailDto;
import com.example.diary.post.domain.dto.PostDto;
import com.example.diary.post.exception.PostErrorCode;
import com.example.diary.post.exception.PostException;
import com.example.diary.post.repository.PostRepository;
import com.example.diary.user.domain.User;
import com.example.diary.user.exception.UserErrorCode;
import com.example.diary.user.exception.UserException;
import com.example.diary.user.repository.UserRepository;
import com.example.diary.user_group.domain.Role;
import com.example.diary.user_group.domain.Status;
import com.example.diary.user_group.domain.UserGroup;
import com.example.diary.user_group.exception.UserGroupErrorCode;
import com.example.diary.user_group.exception.UserGroupException;
import com.example.diary.user_group.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;

    @Transactional
    public PostDto createPost(Long userId, Long groupId, WritePostDto writePostDto) {
        User user = validateUserId(userId);
        Group group = validateGroupId(groupId);
        UserGroup userGroup = validateUserGroup(userId, groupId);

        if (userGroup.getStatus().equals(Status.PENDING) || userGroup.getStatus().equals(Status.DENY)) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_ROLE);
        }

        Post post = Post.builder()
                .title(writePostDto.getTitle())
                .body(writePostDto.getBody())
                .user(user)
                .group(group)
                .build();

        postRepository.save(post);

        return modelMapper.map(post, PostDto.class);
    }

    // todo DENY된 post 안 보이게
    public List<PostDto> getPosts(Long userId, Long groupId) {
        validateUserId(userId);
        validateGroupId(groupId);
        UserGroup userGroup = validateUserGroup(userId, groupId);

        if (userGroup.getStatus().equals(Status.PENDING) || userGroup.getStatus().equals(Status.DENY)) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_ROLE);
        }

        return postRepository.findAllByGroupId(groupId).stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();
    }

    public PostDetailDto getPost(Long userId, Long groupId, Long postId) {
        validateUserId(userId);
        validateGroupId(groupId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.INVALID_POST_ID));

        return modelMapper.map(post, PostDetailDto.class);
    }

    @Transactional
    public PostDto updatePost(Long userId, Long groupId, Long postId, WritePostDto writePostDto) {
        validateUserId(userId);
        validateGroupId(groupId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.INVALID_POST_ID));

        post.update(writePostDto);

        return modelMapper.map(post, PostDto.class);
    }

    @Transactional
    public void deletePost(Long userId, Long groupId, Long postId) {
        validateUserId(userId);
        validateGroupId(groupId);

        postRepository.deleteById(postId);
    }

    @Transactional
    public void deleteAllPost(Long userId, Long groupId) {
        validateUserId(userId);
        validateGroupId(groupId);

        postRepository.deleteAllByUserIdAndGroupId(userId, groupId);
    }

    @Transactional
    public void deletePostByForce(Long adminId, Long groupId, Long postId) {
        validateUserId(adminId);
        validateGroupId(groupId);
        UserGroup userGroup = validateUserGroup(adminId, groupId);

        if (userGroup.getRole().equals(Role.MEMBER) || userGroup.getStatus().equals(Status.DENY)) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_ROLE);
        }

        postRepository.deleteById(postId);
    }

    private User validateUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_LOGIN_ID));
    }

    private Group validateGroupId(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.INVALID_GROUP_ID));
    }

    private UserGroup validateUserGroup(Long userId, Long groupId) {
        return userGroupRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new UserGroupException(UserGroupErrorCode.INVALID_USER_AND_GROUP_ID));
    }
}
