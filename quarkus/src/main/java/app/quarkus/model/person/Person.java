package app.quarkus.model.person;

import app.core.utils.BasicFunctions;
import app.quarkus.model.address.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "person", indexes = {
        @Index(name = "ipersonak1", columnList = "name, telephone, cellphone, birthday, document, active")})
@JsonIgnoreProperties({"updatedBy", "active", "updatedAt", "deletedAt"})
public class Person extends PanacheEntityBase {

    @Column()
    @SequenceGenerator(name = "personIdSequence", sequenceName = "person_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "personIdSequence")
    @Id
    public Long id;

    @Column()
    public String name;

    @ManyToOne
    @JoinColumn(name = "generoId")
    public Gender gender;

    @Column()
    public LocalDate birthday;

    @Column()
    public String telephone;

    @Column()
    public String cellphone;

    @Column()
    public String email;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "addressId")
    public Address address;

    @Column()
    public String document;

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
    public Boolean active;

    @Column()
    @JsonIgnore
    public LocalDateTime updatedAt;

    @Column()
    @JsonIgnore
    public LocalDateTime deletedAt;

    public Person() {

    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }

}
