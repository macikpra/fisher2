package edu.store.database.repositories;

import edu.store.database.entities.CfgUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CfgUserRepository extends JpaRepository<CfgUser, Long> {
    @Query("SELECT r FROM CfgUser r ORDER BY r.id")
    List<CfgUser> findAll();

    @Query("SELECT r FROM CfgUser r where r.userId = ?1")
    CfgUser findByUserId(Long id);

    @Query("SELECT r FROM CfgUser r where r.email = ?1")
    CfgUser findByEmail(String email);
}
