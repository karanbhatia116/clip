package app.clip.users.services;

import app.clip.commons.constants.AssetClass;
import app.clip.commons.exceptions.NotFoundException;
import app.clip.users.models.User;
import app.clip.users.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User create(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public User deleteById(Long id) throws NotFoundException {
        User user = getById(id);
        userRepository.deleteById(id);
        return user;
    }

    @Override
    @Transactional
    public User update(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User getById(Long id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(AssetClass.USER.name(), id.toString()));
    }

    @Override
    public Collection<User> findByIds(Collection<Long> ids) {
        return userRepository.findAllById(ids);
    }
}
