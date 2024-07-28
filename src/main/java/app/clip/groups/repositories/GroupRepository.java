package app.clip.groups.repositories;

import app.clip.groups.models.Group;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Group g SET g.simplifyDebt = true where g.id = :id")
    void simplifyDebt(@Param("id") Long id);
}
