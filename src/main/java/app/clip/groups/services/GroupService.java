package app.clip.groups.services;

import app.clip.groups.models.Group;

import java.util.Collection;

public interface GroupService {
    Group create(Group group);
    Group update(Group group);
    Group getById(final Long id);
    Group deleteById(final Long id);
    Collection<Group> findByIds(final Collection<Long> ids);
    Group simplifyDebts(final Long id);
}
