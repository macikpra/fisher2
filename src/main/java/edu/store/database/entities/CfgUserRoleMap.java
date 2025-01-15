package edu.store.database.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The persistent class for the cfg_user_role_map database table.
 *
 */
@Entity
@Table(name = "cfg_user_role_map", schema = "public")
@NamedQuery(name = "CfgUserRoleMap.findAll", query = "SELECT c FROM CfgUserRoleMap c")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Getter
@Setter
public class CfgUserRoleMap implements Serializable {

    @Serial
    private static final long serialVersionUID = 1434957457235130976L;

    @Id
    @Column(name = "\"ID\"")
    @SequenceGenerator(
        name = "curm",
        sequenceName = "\"cfg_user_role_map_ID_seq\"",
        schema = "public",
        allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "curm")
    private Long id;

    @Column(name = "cfg_role_id")
    private Long cfgRoleId;

    @Column(name = "cfg_user_id")
    private Long cfgUserId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created;

    @Column(name = "enabled")
    private Boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified")
    private Date modified;
}
