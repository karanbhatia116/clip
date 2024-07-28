package app.clip.group_users.api;

import app.clip.group_users.services.GroupUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups/users")
public class GroupUserAPI {

    private final GroupUserService groupUserService;

    public GroupUserAPI(GroupUserService groupUserService) {
        this.groupUserService = groupUserService;
    }

    @PostMapping("/add")
    public void addUserToGroup(@RequestHeader("x-user-id") Long userId, @RequestHeader("x-group-id") Long groupId) {
        groupUserService.addUserToGroup(userId, groupId);
    }

    @DeleteMapping("/remove")
    public void removeUserFromGroup(@RequestHeader("x-user-id") Long userId, @RequestHeader("x-group-id") Long groupId) {
        groupUserService.removeUserFromGroup(userId, groupId);
    }

    @GetMapping("/find")
    public List<?> findUsersInAGroup(@RequestParam(value = "groupId", required = false) Long groupId, @RequestParam(value = "userId", required = false) Long userId) {
        if(userId != null) {
            return groupUserService.findGroupsOfUser(userId).stream().toList();
        }
        else {
            return groupUserService.findUsersInGroup(groupId).stream().toList();
        }
    }

}
