package app.clip.groups.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "groups")
public class Group implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "crt_id")
    private Long creatorId;

    private String title;

    @Column(name = "dsc")
    private String description;

    @Column(name = "crt_at")
    private Long createdAt;

    @Column(name = "upd_at")
    private Long updatedAt;

    @Column(name = "sfy_dbt")
    private Boolean simplifyDebt;

    public Group() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getSimplifyDebt() {
        return simplifyDebt;
    }

    public void setSimplifyDebt(Boolean simplifyDebt) {
        this.simplifyDebt = simplifyDebt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(creatorId, group.creatorId) && Objects.equals(title, group.title) && Objects.equals(description, group.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creatorId, title, description);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", creatorId='" + creatorId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
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
