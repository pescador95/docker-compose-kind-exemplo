package app.quarkus.model.address;

import app.core.utils.BasicFunctions;
import app.quarkus.model.person.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "address")
@JsonIgnoreProperties({"updatedBy", "active", "updatedAt", "deletedAt"})
public class Address extends PanacheEntityBase {

    @Column()
    @SequenceGenerator(name = "addressIdSequence", sequenceName = "address_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "addressIdSequence")
    @Id
    public Long id;

    @Column()
    public String zipCode;

    @Column()
    public String publicPlace;

    @Column()
    public Long number;

    @Column()
    public String complement;

    @Column()
    public String city;

    @Column()
    public String state;

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

    public Address() {

    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }

}
