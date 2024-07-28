package app.clip.group_users.services;


import app.clip.group_users.models.GroupUser;
import app.clip.groups.models.Group;
import app.clip.users.models.User;

import java.util.Collection;

public interface GroupUserService {

    // public
    Collection<Group> findGroupsOfUser(final Long userId);
    Collection<User> findUsersInGroup(final Long groupId);
    void addUserToGroup(final Long userId, final Long groupId);
    void removeUserFromGroup(final Long userId, final Long groupId);


    // internal
    GroupUser create(GroupUser groupUser);
    GroupUser deleteById(final Long id);
    Collection<GroupUser> deleteByIds(final Collection<Long> ids);
    GroupUser update(GroupUser groupUser);
    GroupUser getById(final Long id);
    Collection<GroupUser> findByGroupId(final Long groupId);
    Collection<GroupUser> findByUserId(final Long userId);

}
