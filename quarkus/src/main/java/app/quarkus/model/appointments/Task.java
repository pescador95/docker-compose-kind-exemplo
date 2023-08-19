package app.quarkus.model.appointments;

import app.core.utils.BasicFunctions;
import app.quarkus.model.person.PersonalActivity;
import app.quarkus.model.person.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task", indexes = {@Index(name = "ifichaavaliacaoak1", columnList = "taskDate, personalactivityId, active")})
@JsonIgnoreProperties({"updatedBy", "active", "updatedAt", "deletedAt"})
public class Task extends PanacheEntityBase {

    @Column()
    @SequenceGenerator(name = "fichaavaliacaoIdSequence", sequenceName = "fichaavaliacao_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "fichaavaliacaoIdSequence")
    @Id
    public Long id;

    @Column()
    public LocalDateTime taskDate;

    @Column()
    public String task;

    @Column()
    public String summary;

    @Column()
    public String avaliation;

    @Column()
    @JsonIgnore
    public Boolean active;

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

    @Column()
    @JsonIgnore
    public LocalDateTime deletedAt;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "personalactivityId")
    public PersonalActivity personalActivity;

    public Task() {

    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }

}
