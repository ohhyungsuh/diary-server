package com.example.diary.group.service;

import com.example.diary.group.domain.Group;
import com.example.diary.group.domain.dto.CreateGroupDto;
import com.example.diary.group.domain.dto.GroupDto;
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
public class GroupService {

    private final ModelMapper modelMapper;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    @Transactional
    public GroupDto createGroup(Long userId, CreateGroupDto createGroupDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_LOGIN_ID));

        if (groupRepository.existsByName(createGroupDto.getName())) {
            throw new GroupException(GroupErrorCode.DUPLICATE_GROUP_NAME);
        }

        Group group = Group.builder()
                .name(createGroupDto.getName())
                .description(createGroupDto.getDescription())
                .build();

        groupRepository.save(group);

        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .role(Role.OWNER)
                .status(Status.JOIN)
                .build();

        userGroupRepository.save(userGroup);

        return modelMapper.map(group, GroupDto.class);
    }


}