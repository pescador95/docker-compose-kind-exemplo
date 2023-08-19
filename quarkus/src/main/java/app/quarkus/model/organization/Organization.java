package app.quarkus.model.organization;

import app.core.utils.BasicFunctions;
import app.quarkus.model.address.Address;
import app.quarkus.model.appointments.ServiceType;
import app.quarkus.model.person.PersonalActivity;
import app.quarkus.model.person.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "organization")
@JsonIgnoreProperties({"updatedBy", "active", "updatedAt", "deletedAt"})
public class Organization extends PanacheEntityBase {

    @Column()
    @SequenceGenerator(name = "organizationIdSequence", sequenceName = "organization_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "organizationIdSequence")
    @Id
    public Long id;

    @Column()
    public String name;

    @Column()
    public String identityDocument;

    @Column()
    public String telephone;

    @Column()
    public String cellphone;

    @Column()
    public String email;

    @ManyToOne
    @JoinColumn(name = "addressId")
    public Address address;

    @Column()
    @JsonIgnore
    public Boolean active;

    @Column()
    @JsonIgnore
    public LocalDateTime updatedAt;

    @Column()
    @JsonIgnore
    public LocalDateTime deletedAt;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "userId")
    public User updatedBy;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public ServiceType serviceType;

    @ManyToOne
    @JoinColumn(name = "personal_activity_id")
    private PersonalActivity personalActivity;

    public Organization() {

    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }

}
