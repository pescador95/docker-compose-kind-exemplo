package app.core.model.contract;

import app.quarkus.model.organization.Organization;
import app.quarkus.model.person.User;
import app.core.utils.BasicFunctions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contract")
@JsonIgnoreProperties({ "updatedBy", "active", "updatedAt", "deletedAt" })
@RegisterForReflection
public class Contract extends PanacheEntityBase {

    @Column()
    @SequenceGenerator(name = "contractIdSequence", sequenceName = "contract_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "contractIdSequence")
    @Id
    public Long id;

    @ManyToOne
    @JoinColumn(name = "organizationId")
    public Organization organizationContract;

    @ManyToOne
    @JoinColumn(name = "responsibleId")
    public User responsibleContract;

    @Column()
    public Integer maximumSessions;

    @Column()
    public String considerations;

    @Column()
    public LocalDate dataContract;

    @ManyToOne
    @JoinColumn(name = "typeContractId")
    public TypeContract typeContract;

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

    public Contract() {

    }

    public Boolean uniqueSession() {
        return BasicFunctions.isValid(this.id) && BasicFunctions.isNotEmpty(this.typeContract)
                && this.typeContract.id.equals(TypeContract.UNIQUE_SESSION);
    }

    public Boolean sharedSession() {
        return BasicFunctions.isValid(this.id) && BasicFunctions.isNotEmpty(this.typeContract)
                && this.typeContract.id.equals(TypeContract.SHARED_SESSION);
    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }
}
