package app.clip.users.services;

import app.clip.users.models.User;

import java.util.Collection;

public interface UserService {

    User create(User user);
    User deleteById(Long id);
    User update(User user);
    User getById(Long id);
    Collection<User> findByIds(final Collection<Long> ids);
}
