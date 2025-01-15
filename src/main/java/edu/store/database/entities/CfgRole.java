package edu.store.database.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The persistent class for the cfg_role database table.
 *
 */
@Entity
@Table(name = "\"cfg_role\"", schema = "public")
@NamedQuery(name = "CfgRole.findAll", query = "SELECT c FROM CfgRole c")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Getter
@Setter
public class CfgRole implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -2993750339732253780L;

    @Id
    @SequenceGenerator(name = "cfgRole", sequenceName = "\"cfg_role_ID_seq\"", schema = "public", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cfgRole")
    @Column(name = "\"id\"", unique = true, nullable = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created;

    @NotNull
    @Column(nullable = false, length = 125)
    @Size(min = 1, max = 125, message = "description must be longer than 1 and less than 125 characters")
    private String description;

    @Column(name = "enabled")
    private Boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified")
    private Date modified;

    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    @Size(min = 1, max = 50, message = "name must be longer than 1 and less than 50 characters")
    private String name;

    @NotNull
    @Column(name = "\"parentId\"", nullable = false)
    private BigInteger parentId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "\"deletedAt\"")
    private Date deletedAt;

    @Column(name = "\"deletedBy\"")
    private String deletedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "\"insertedAt\"")
    private Date insertedAt;

    @Column(name = "\"insertedBy\"")
    private String insertedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "\"modifiedAt\"")
    private Date modifiedAt;

    @Column(name = "\"modifiedBy\"")
    private String modifiedBy;

    @Transient
    private List<CfgRole> roles;

    @Transient
    private CfgRole supervisor;

    @Version
    @Column(name = "\"trxId\"")
    private Long trxId;

    public CfgRole() {
        roles = new ArrayList<>();
    }
}
