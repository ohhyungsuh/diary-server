package com.example.diary.user_group.service;

import com.example.diary.group.domain.Group;
import com.example.diary.group.exception.GroupErrorCode;
import com.example.diary.group.exception.GroupException;
import com.example.diary.group.repository.GroupRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public UserGroupDto joinGroup(Long userId, Long groupId) {
        User user = validateUserId(userId);

        Group group = validateGroupId(groupId);

        if (userGroupRepository.findByUserIdAndGroupId(userId, groupId).isPresent()) {
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

        UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new UserGroupException(UserGroupErrorCode.INVALID_USER_AND_GROUP_ID));

        if (userGroup.getRole().equals(Role.OWNER) || userGroup.getRole().equals(Role.MANAGER)) {
            throw new UserGroupException(UserGroupErrorCode.UNAUTHORIZED_ROLE);
        }

        userGroupRepository.deleteById(userGroup.getId());
    }

    public List<UserDto> getUsersInGroup(Long groupId) {
        return userGroupRepository.findByGroupId(groupId).stream()
                .map(userGroup -> modelMapper.map(userGroup.getUser(), UserDto.class))
                .toList();
    }

    @Transactional
    public void leaveGroup(Long userId, Long groupId) {
        UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new UserGroupException(UserGroupErrorCode.INVALID_USER_AND_GROUP_ID));

        if(!userGroup.getStatus().equals(Status.JOIN)) {
            throw new UserGroupException(UserGroupErrorCode.INVALID_STATUS);
        }

        if(userGroup.getRole().equals(Role.OWNER)) {
            throw new UserGroupException(UserGroupErrorCode.INVALID_ROLE);
        }

        userGroupRepository.deleteById(userGroup.getId());
    }

    private User validateUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_LOGIN_ID));
    }

    private Group validateGroupId(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.INVALID_GROUP_ID));
    }
}
