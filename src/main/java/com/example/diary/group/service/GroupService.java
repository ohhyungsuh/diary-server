package com.example.diary.group.service;

import com.example.diary.group.domain.Group;
import com.example.diary.group.domain.dto.CreateGroupDto;
import com.example.diary.group.domain.dto.GroupDetailDto;
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

import java.util.List;

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
        User user = validateUserId(userId);

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

    public List<GroupDto> getGroups() {
        return groupRepository.findAll().stream()
                .map(group -> modelMapper.map(group, GroupDto.class))
                .toList();
    }

    public List<GroupDto> getMyGroups(Long userId) {
        validateUserId(userId);

        return userGroupRepository.findByUserId(userId).stream()
                .map(userGroup -> modelMapper.map(userGroup.getGroup(), GroupDto.class))
                .toList();
    }

    public GroupDetailDto getGroupDetail(Long userId, Long groupId) {
        validateUserId(userId);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.INVALID_GROUP_ID));

        // fetch join 필요 없음. groupId로 조회하면 쿼리문에 left join됨
        List<UserGroup> userGroups = userGroupRepository.findByGroupId(groupId);

        User owner = userGroups.stream()
                .filter(userGroup -> userGroup.getRole().equals(Role.OWNER))
                .findFirst()
                .get()
                .getUser();

        Status status = userGroups.stream()
                .filter(userGroup -> userGroup.getUser().getId().equals(userId))
                .map(UserGroup::getStatus)
                .findFirst()
                .orElse(Status.NOT_JOIN);

        return new GroupDetailDto(
                modelMapper.map(group, GroupDetailDto.GroupInfo.class),
                modelMapper.map(owner, GroupDetailDto.OwnerInfo.class),
                status
        );

    }

    // todo soft delete 하게된다면?
    @Transactional
    public void deleteGroup(Long groupId) {
        userGroupRepository.deleteAllByGroupId(groupId);
        groupRepository.deleteById(groupId);
    }

    private User validateUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_LOGIN_ID));
    }
}