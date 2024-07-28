package app.clip.users.api;

import app.clip.users.models.User;
import app.clip.users.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserAPI {

    private final UserService userService;

    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    public User deleteById(@PathVariable("id") Long id) {
        return userService.deleteById(id);
    }

    @PutMapping("/")
    public User update(@RequestBody User user) {
        return userService.update(user);
    }
}
