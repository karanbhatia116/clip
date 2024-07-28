package app.clip.group_users.repositories;

import app.clip.group_users.models.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
    List<GroupUser> findByUserId(Long userId);
    List<GroupUser> findByGroupId(Long groupId);
    Optional<GroupUser> findByUserIdAndGroupId(Long userId, Long groupId);
}
