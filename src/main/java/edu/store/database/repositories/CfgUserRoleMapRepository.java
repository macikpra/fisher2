package edu.store.database.repositories;

import edu.store.database.entities.CfgUserRoleMap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CfgUserRoleMapRepository extends JpaRepository<CfgUserRoleMap, Long> {
    @Query("SELECT r FROM CfgUserRoleMap r ORDER BY r.id")
    List<CfgUserRoleMap> findAll();

    @Query("SELECT r FROM CfgUserRoleMap r where r.cfgUserId = ?1")
    CfgUserRoleMap findOneByCfgUserId(Long cfgUserId);
}
