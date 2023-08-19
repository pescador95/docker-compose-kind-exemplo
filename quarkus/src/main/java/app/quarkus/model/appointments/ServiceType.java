package app.quarkus.model.appointments;

import app.core.utils.BasicFunctions;
import app.quarkus.model.organization.Organization;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "serviceType")
public class ServiceType extends PanacheEntityBase {

    @Column()
    @SequenceGenerator(name = "serviceTypeSequence", sequenceName = "serviceType_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "serviceTypeSequence")
    @Id
    public Long id;
    @Column()
    public String serviceType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "serviceTypeorganizations", joinColumns = {
            @JoinColumn(name = "serviceTypeId")}, inverseJoinColumns = {
            @JoinColumn(name = "organizationId")})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<Organization> organizations = new ArrayList<>();

    public ServiceType() {

    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }
}
