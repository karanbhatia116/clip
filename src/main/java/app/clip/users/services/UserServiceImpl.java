package app.clip.users.services;

import app.clip.users.models.User;
import app.clip.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User deleteById(Long id) {
        User user = getById(id);
        userRepository.deleteById(id);
        return user;
    }

    @Override
    public User update(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));
    }

    @Override
    public Collection<User> findByIds(Collection<Long> ids) {
        return userRepository.findAllById(ids);
    }
}
