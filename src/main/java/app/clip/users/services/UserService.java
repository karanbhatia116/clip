package app.clip.users.services;

import app.clip.commons.exceptions.NotFoundException;
import app.clip.users.models.User;

import java.util.Collection;

public interface UserService {

    User create(User user);
    User deleteById(Long id) throws NotFoundException;
    User update(User user);
    User getById(Long id) throws NotFoundException;
    Collection<User> findByIds(final Collection<Long> ids);
}
