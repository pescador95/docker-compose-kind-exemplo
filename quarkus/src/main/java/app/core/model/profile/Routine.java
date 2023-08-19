package app.core.model.profile;

import app.core.utils.BasicFunctions;
import app.quarkus.model.person.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "routine")
public class Routine extends PanacheEntityBase {

    @Column()
    @SequenceGenerator(name = "routineIdSequence", sequenceName = "routine_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "routineIdSequence")
    @Id
    public Long id;

    @Column()
    public String name;

    @Column()
    public String icon;

    @Column()
    public String path;

    @Column()
    public String title;

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

    public Routine() {

    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }
}
