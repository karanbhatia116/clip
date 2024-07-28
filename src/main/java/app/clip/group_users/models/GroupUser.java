package app.clip.group_users.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "group_users")
public class GroupUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "usr_id")
    private Long userId;
    @Column(name = "grp_id")
    private Long groupId;

    @Column(name = "crt_at")
    private Long createdAt;

    @Column(name = "upd_at")
    private Long updatedAt;

    public GroupUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupUser groupUser = (GroupUser) o;
        return Objects.equals(id, groupUser.id) && Objects.equals(userId, groupUser.userId) && Objects.equals(groupId, groupUser.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, groupId);
    }

    @Override
    public String toString() {
        return "GroupUser{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }

    @PrePersist
    public void prePersist() {
        Long systemTime = System.currentTimeMillis();
        setCreatedAt(systemTime);
        setUpdatedAt(systemTime);
    }

    @PreUpdate
    public void preUpdate() {
        setUpdatedAt(System.currentTimeMillis());
    }
}
