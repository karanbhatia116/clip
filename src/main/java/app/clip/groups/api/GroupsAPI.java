package app.clip.groups.api;

import app.clip.commons.exceptions.NotFoundException;
import app.clip.group_users.models.GroupUser;
import app.clip.group_users.services.GroupUserService;
import app.clip.groups.models.Group;
import app.clip.groups.services.GroupService;
import app.clip.users.models.User;
import app.clip.users.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/groups")
public class GroupsAPI {

    private final GroupService groupService;
    private final UserService userService;
    private final GroupUserService groupUserService;

    public GroupsAPI(GroupService groupService, UserService userService, GroupUserService groupUserService) {
        this.groupService = groupService;
        this.userService = userService;
        this.groupUserService = groupUserService;
    }

    @PostMapping("/")
    public Group create(@RequestBody Group group) throws NotFoundException {
        Long creatorId = group.getCreatorId();
        User user = userService.getById(creatorId);
        Group newGroup = groupService.create(group);
        groupUserService.addUserToGroup(user.getId(), newGroup.getId());
        return newGroup;
    }

    @DeleteMapping("/{id}")
    public Group deleteById(@PathVariable("id") Long id) throws NotFoundException {
        // when group is deleted, remove all the members of the group first
        // then delete the group
        Collection<GroupUser> groupUsers = groupUserService.findByGroupId(id);
        groupUserService.deleteByIds(groupUsers.stream().map(GroupUser::getId).toList());
        return groupService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Group getById(@PathVariable("id") Long id) throws NotFoundException {
        return groupService.getById(id);
    }

    @PutMapping("/")
    public Group update(@RequestBody Group group) {
        return groupService.update(group);
    }

    @PatchMapping("/{id}/simplify-debt")
    public Group simplifyDebts(@PathVariable("id") Long id) throws NotFoundException {
        return groupService.simplifyDebts(id);
    }
}
