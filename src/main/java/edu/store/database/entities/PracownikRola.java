package edu.store.database.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "pracownik_rola", schema = "public")
@NamedQuery(name = "PracownikRola.findAll", query = "SELECT u FROM PracownikRola u")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@ToString
@Getter
@Setter
public class PracownikRola implements Serializable {

    /** */
    @Serial
    private static final long serialVersionUID = -4779485869092445937L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PracownikRolaGen")
    @SequenceGenerator(
        name = "PracownikRolaGen",
        sequenceName = "pracownik_rola_id_seq",
        schema = "public",
        allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "pracownik_id")
    private Long pracownikId;

    @Column(name = "cfg_role_id")
    private Long cfgRoleId;

    @Column(name = "data_zatrudnienia")
    private Date dataZatrudnienia;

    @Column(name = "data_zwolnienia")
    private Date dataZwolnienia;
}
