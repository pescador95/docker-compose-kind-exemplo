package app.quarkus.model.person;

import app.core.utils.BasicFunctions;
import app.quarkus.model.appointments.Task;
import app.quarkus.model.organization.Organization;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "personalActivity", indexes = {@Index(name = "ipersonalActivityak1", columnList = "personId, active")})
@JsonIgnoreProperties({"updatedBy", "active", "updatedAt", "deletedAt"})
public class PersonalActivity extends PanacheEntityBase {

    @Column()
    @SequenceGenerator(name = "personalactivityIdSequence", sequenceName = "personalactivity_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "personalactivityIdSequence")
    @Id
    public Long id;

    @Column()
    public String occupation;

    @Column()
    public String responsibleContact;

    @Column()
    public String personName;

    @Column()
    @JsonIgnore
    public Boolean active;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "personId")
    public Person person;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    public User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId")
    public User updatedBy;

    @Column()
    @JsonIgnore
    public LocalDateTime updatedAt;

    @Column()
    @JsonIgnore
    public LocalDateTime deletedAt;
    @OneToMany(mappedBy = "personalActivity", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<Organization> organizations;
    @OneToMany(mappedBy = "personalActivity", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<Task> tasks;

    public PersonalActivity() {

    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }

}
