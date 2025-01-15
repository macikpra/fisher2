package edu.store.database.repositories;

import edu.store.database.entities.CfgRole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CfgRoleRepository extends JpaRepository<CfgRole, Long> {
    @Query("SELECT r FROM CfgRole r ORDER BY r.id")
    List<CfgRole> findAll();

    @Query("SELECT r FROM CfgRole r where r.id = ?1")
    CfgRole findOneById(Long id);

    @Query("SELECT r FROM CfgRole r where r.name = ?1")
    CfgRole findOneByName(String name);
}
