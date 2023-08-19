package app.core.model.profile;

import app.core.utils.BasicFunctions;
import app.quarkus.model.person.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profileAccess")
public class ProfileAccess extends PanacheEntityBase {

    @Column()
    @SequenceGenerator(name = "profileaccessIdSequence", sequenceName = "profileaccess_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "profileaccessIdSequence")
    @Id
    public Long id;

    @Column()
    public String name;

    @Column()
    public Boolean create;

    @Column()
    public Boolean read;

    @Column()
    public Boolean update;

    @Column()
    public Boolean delete;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "routineprofileaccess", joinColumns = {
            @JoinColumn(name = "profileaccessId")}, inverseJoinColumns = {
            @JoinColumn(name = "routineId")})
    public List<Routine> routines = new ArrayList<>();

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    public User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public User updatedBy;

    @Column()
    @JsonIgnore
    public LocalDateTime updatedAt;

    public ProfileAccess() {

    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }

    public Boolean hasRoutines() {
        return BasicFunctions.isValid(this.id) && BasicFunctions.isNotEmpty(this.routines);
    }
}
