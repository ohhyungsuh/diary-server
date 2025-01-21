package com.example.diary.user_group.service;

import com.example.diary.group.domain.Group;
import com.example.diary.group.exception.GroupErrorCode;
import com.example.diary.group.exception.GroupException;
import com.example.diary.group.repository.GroupRepository;
import com.example.diary.post.repository.PostRepository;
import com.example.diary.user.domain.User;
import com.example.diary.user.exception.UserErrorCode;
import com.example.diary.user.exception.UserException;
import com.example.diary.user.repository.UserRepository;
import com.example.diary.user_group.domain.Role;
import com.example.diary.user_group.domain.Status;
import com.example.diary.user_group.domain.UserGroup;
import com.example.diary.user_group.domain.dto.UserDto;
import com.example.diary.user_group.domain.dto.UserGroupDto;
import com.example.diary.user_group.exception.UserGroupErrorCode;
import com.example.diary.user_group.exception.UserGroupException;
import com.example.diary.user_group.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PostRepository postRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public UserGroupDto joinGroup(Long userId, Long groupId) {
        User user = validateUserId(userId);

        Group group = validateGroupId(groupId);

        Optional<UserGroup> findUserGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId);
        if (findUserGroup.isPresent()) {
            if (findUserGroup.get().getStatus().equals(Status.DENY)) {
                throw new UserGroupException(UserGroupErrorCode.DENY_USER);
            }
            throw new UserGroupException(UserGroupErrorCode.DUPLICATE_USER_GROUP);
        }

        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .build();

        userGroupRepository.save(userGroup);

        return modelMapper.map(group, UserGroupDto.class);
    }

    // user, group 까지 찾아서 예외 처리를 해야되는게 맞나? 맞는 거 같긴 한데, 이유가 그럼 데이터 불일치할까봐?
    @Transactional
    public void deleteJoinGroup(Long userId, Long groupId) {
        validateUserId(userId);

        validateGroupId(groupId);

        UserGroup userGroup = validateUserGroup(userId, groupId);

        if (!userGroup.getStatus().equals(Status.PENDING)) {
            throw new UserGroupException(UserGroupErrorCode.INVALID_STATUS);
        }

        if (userGroup.getRole().equals(Role.OWNER) || userGroup.getRole().equals(Role.MANAGER)) {
            throw new UserGroupException(UserGroupErrorCode.UNAUTHORIZED_ROLE);
        }

        userGroupRepository.deleteById(userGroup.getId());
    }

    // 그룹 내 인원 조회
    public List<UserDto> getUsersInGroup(Long userId, Long groupId) {
        UserGroup findUserGroup = validateUserGroup(userId, groupId);

        if (!findUserGroup.getStatus().equals(Status.JOIN)) {
            throw new UserGroupException(UserGroupErrorCode.UNAUTHORIZED_ROLE);
        }

        return userGroupRepository.findByGroupId(groupId).stream()
                .map(userGroup -> modelMapper.map(userGroup.getUser(), UserDto.class))
                .toList();
    }

    // 방장은 방을 나갈 수 없고, 그룹 삭제만 가능
    // todo 최적화 필요
    @Transactional
    public void leaveGroup(Long userId, Long groupId) {
        validateUserId(userId);

        validateGroupId(groupId);

        UserGroup userGroup = validateUserGroup(userId, groupId);

        if (!userGroup.getStatus().equals(Status.JOIN)) {
            throw new UserGroupException(UserGroupErrorCode.INVALID_STATUS);
        }

        if (userGroup.getRole().equals(Role.OWNER)) {
            throw new UserGroupException(UserGroupErrorCode.INVALID_ROLE);
        }
        postRepository.deleteAllByGroupId(groupId);

        userGroupRepository.deleteById(userGroup.getId());
    }

    // 가입 요청 조회는 방장 + 매니저 가능
    public List<UserDto> lookUpJoinUsers(Long userId, Long groupId) {
        validateUserId(userId);

        UserGroup findUserGroup = validateUserGroup(userId, groupId);

        if (findUserGroup.getRole().equals(Role.MEMBER)) {
            throw new UserGroupException(UserGroupErrorCode.UNAUTHORIZED_ROLE);
        }

        return userGroupRepository.findByGroupId(groupId).stream()
                .filter(userGroup -> userGroup.getStatus().equals(Status.PENDING))
                .map(userGroup -> modelMapper.map(userGroup.getUser(), UserDto.class))
                .toList();
    }

    // 가입 요청 수락은 방장 + 매니저 가능
    @Transactional
    public void acceptJoinUser(Long adminId, Long userId, Long groupId) {
        validateUserId(adminId);
        validateUserId(userId);
        UserGroup admin = validateUserGroup(adminId, groupId);

        if (admin.getRole().equals(Role.MEMBER)) {
            throw new UserGroupException(UserGroupErrorCode.UNAUTHORIZED_ROLE);
        }

        UserGroup userGroup = validateUserGroup(userId, groupId);

        if (userGroup.getStatus().equals(Status.JOIN)) {
            throw new UserGroupException(UserGroupErrorCode.ALREADY_JOINED);
        }

        userGroup.acceptUser();
    }

    // 가입 요청 거절은 방장 + 매니저 가능
    @Transactional
    public void rejectJoinUser(Long adminId, Long userId, Long groupId) {
        validateUserId(adminId);
        validateUserId(userId);

        UserGroup admin = validateUserGroup(adminId, groupId);

        if (admin.getRole().equals(Role.MEMBER)) {
            throw new UserGroupException(UserGroupErrorCode.UNAUTHORIZED_ROLE);
        }

        UserGroup userGroup = validateUserGroup(userId, groupId);

        if (!userGroup.getStatus().equals(Status.PENDING)) {
            throw new UserGroupException(UserGroupErrorCode.ALREADY_JOINED);
        }

        userGroupRepository.deleteById(userGroup.getId());
    }

    // 추방은 방장만 가능
    @Transactional
    public void expelUser(Long adminId, Long userId, Long groupId) {
        validateUserId(adminId);
        validateUserId(userId);

        UserGroup admin = validateUserGroup(adminId, groupId);

        if (!admin.getRole().equals(Role.OWNER)) {
            throw new UserGroupException(UserGroupErrorCode.UNAUTHORIZED_ROLE);
        }

        UserGroup userGroup = validateUserGroup(userId, groupId);

        // 추방당하면 관련된 글은? 그룹 나가는건 다 삭제하게함

        userGroup.expelUser();
    }

    // 매니저 승진은 방장만 가능
    @Transactional
    public void updateUser(Long adminId, Long userId, Long groupId) {
        validateUserId(adminId);
        validateUserId(userId);

        UserGroup admin = validateUserGroup(adminId, groupId);

        if (!admin.getRole().equals(Role.OWNER)) {
            throw new UserGroupException(UserGroupErrorCode.UNAUTHORIZED_ROLE);
        }

        UserGroup userGroup = validateUserGroup(userId, groupId);
        if (!userGroup.getRole().equals(Role.MEMBER) || !userGroup.getStatus().equals(Status.JOIN)) {
            throw new UserGroupException(UserGroupErrorCode.INVALID_UPDATE_ROLE);
        }

        userGroup.updateRole();
    }

    // 매니저 강등은 방장만 가능
    @Transactional
    public void demoteUser(Long adminId, Long userId, Long groupId) {
        validateUserId(adminId);
        validateUserId(userId);

        UserGroup admin = validateUserGroup(adminId, groupId);

        if (!admin.getRole().equals(Role.OWNER)) {
            throw new UserGroupException(UserGroupErrorCode.UNAUTHORIZED_ROLE);
        }

        UserGroup userGroup = validateUserGroup(userId, groupId);
        if (!userGroup.getRole().equals(Role.MANAGER) || !userGroup.getStatus().equals(Status.JOIN)) {
            throw new UserGroupException(UserGroupErrorCode.INVALID_UPDATE_ROLE);
        }

        userGroup.demoteRole();
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
