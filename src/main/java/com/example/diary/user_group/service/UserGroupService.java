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
import com.example.diary.user_group.domain.dto.UserGroupDto;
import com.example.diary.user_group.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.INVALID_GROUP_ID));

        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .build();

        userGroupRepository.save(userGroup);

        return modelMapper.map(group, UserGroupDto.class);
    }

    private User validateUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_LOGIN_ID));
    }
}
