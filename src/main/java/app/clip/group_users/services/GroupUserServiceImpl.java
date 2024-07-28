package app.clip.group_users.services;

import app.clip.group_users.models.GroupUser;
import app.clip.group_users.repositories.GroupUserRepository;
import app.clip.groups.models.Group;
import app.clip.groups.services.GroupService;
import app.clip.users.models.User;
import app.clip.users.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
public class GroupUserServiceImpl implements GroupUserService {

    private final UserService userService;
    private final GroupService groupService;
    private final GroupUserRepository groupUserRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupUserServiceImpl.class);

    public GroupUserServiceImpl(UserService userService, GroupService groupService, GroupUserRepository groupUserRepository) {
        this.userService = userService;
        this.groupService = groupService;
        this.groupUserRepository = groupUserRepository;
    }


    @Override
    public Collection<Group> findGroupsOfUser(final Long userId) {
        Collection<GroupUser> groupUsers = groupUserRepository.findByUserId(userId);
        return groupService.findByIds(groupUsers.stream().map(GroupUser::getGroupId).toList());
    }

    @Override
    public Collection<User> findUsersInGroup(final Long groupId) {
        Collection<GroupUser> groupUsers = groupUserRepository.findByGroupId(groupId);
        return userService.findByIds(groupUsers.stream().map(GroupUser::getUserId).toList());
    }

    @Override
    public void addUserToGroup(Long userId, Long groupId) {
        User user = userService.getById(userId);
        Group group = groupService.getById(groupId);
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group.getId());
        groupUser.setUserId(user.getId());
        create(groupUser);
    }

    @Override
    public void removeUserFromGroup(Long userId, Long groupId) {
        User user = userService.getById(userId);
        Group group = groupService.getById(groupId);
        GroupUser groupUser = groupUserRepository.findByUserIdAndGroupId(
                        user.getId(), group.getId())
                .orElseThrow(
                        () -> new NoSuchElementException("User " + user.getId() + " doesn't belong to group " + group.getId())
                );
        groupUserRepository.deleteById(groupUser.getId());
    }

    @Override
    public GroupUser create(GroupUser groupUser) {
        return groupUserRepository.save(groupUser);
    }

    @Override
    public GroupUser deleteById(Long id) {
        GroupUser groupUser = getById(id);
        groupUserRepository.deleteById(groupUser.getId());
        return groupUser;
    }

    @Override
    public Collection<GroupUser> deleteByIds(Collection<Long> ids) {
        Collection<GroupUser> groupUsers = groupUserRepository.findAllById(ids);
        groupUserRepository.deleteAllById(ids);
        return groupUsers;
    }

    @Override
    public GroupUser update(GroupUser groupUser) {
        return groupUserRepository.save(groupUser);
    }

    @Override
    public GroupUser getById(Long id) {
        return groupUserRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Group User with id " + id + " not found"));
    }

    @Override
    public Collection<GroupUser> findByGroupId(Long groupId) {
        return groupUserRepository.findByGroupId(groupId);
    }

    @Override
    public Collection<GroupUser> findByUserId(Long userId) {
        return groupUserRepository.findByUserId(userId);
    }
}
