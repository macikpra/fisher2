package edu.store.database.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The persistent class for the cfg_user database table.
 *
 */
@Entity
@Table(name = "cfg_user", schema = "public")
@NamedQuery(name = "CfgUser.findAll", query = "SELECT c FROM CfgUser c")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Getter
@Setter
public class CfgUser implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -6552785348841731060L;

    @Id
    @SequenceGenerator(name = "cfgUser", sequenceName = "\"cfg_user_ID_seq\"", schema = "public", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cfgUser")
    @Column(name = "\"id\"", unique = true, nullable = false)
    private Long id;

    @Column(name = "\"changePasswordNextLogin\"")
    private Boolean changePasswordNextLogin;

    @Column(name = "\"created\"")
    private Timestamp created;

    @Column(length = 254)
    private String description;

    @NotNull
    @Column(name = "email", nullable = false, length = 254)
    private String email;

    private Boolean enabled;

    @Column(name = "\"lastLogin\"")
    private Timestamp lastLogin;

    private Timestamp modified;

    @Column(nullable = false, length = 254)
    private String name;

    @Column(nullable = false, length = 254)
    private String pswd;

    @Column(name = "\"userId\"", insertable = false, updatable = false)
    private Long userId;
}
