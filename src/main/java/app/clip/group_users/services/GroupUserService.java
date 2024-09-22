package app.clip.group_users.services;


import app.clip.commons.exceptions.ApplicationException;
import app.clip.commons.exceptions.NotFoundException;
import app.clip.group_users.models.GroupUser;
import app.clip.groups.models.Group;
import app.clip.users.models.User;

import java.util.Collection;

public interface GroupUserService {

    // public
    Collection<Group> findGroupsOfUser(final Long userId);
    Collection<User> findUsersInGroup(final Long groupId);
    void addUserToGroup(final Long userId, final Long groupId) throws NotFoundException;
    void removeUserFromGroup(final Long userId, final Long groupId) throws ApplicationException;


    // internal
    GroupUser create(GroupUser groupUser);
    GroupUser deleteById(final Long id) throws NotFoundException;
    Collection<GroupUser> deleteByIds(final Collection<Long> ids);
    GroupUser update(GroupUser groupUser);
    GroupUser getById(final Long id) throws NotFoundException;
    Collection<GroupUser> findByGroupId(final Long groupId);
    Collection<GroupUser> findByUserId(final Long userId);

}
