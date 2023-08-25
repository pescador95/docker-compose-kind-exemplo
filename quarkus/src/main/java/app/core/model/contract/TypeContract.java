package app.core.model.contract;

import app.core.utils.BasicFunctions;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;

@Entity
@Table(name = "typecontract")
@RegisterForReflection
public class TypeContract extends PanacheEntityBase {

    public static final Long UNIQUE_SESSION = 1L;
    public static final Long SHARED_SESSION = 2L;
    @Column()
    @SequenceGenerator(name = "typeContractIdSequence", sequenceName = "typecontract_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "typeContractIdSequence")
    @Id
    public Long id;
    @Column()
    public String typeContract;
    @Column()
    public String description;

    public TypeContract() {

    }

    public Boolean uniqueSession() {
        return BasicFunctions.isValid(this.id) && this.id.equals(TypeContract.UNIQUE_SESSION);
    }

    public Boolean sharedSession() {
        return BasicFunctions.isValid(this.id) && this.id.equals(TypeContract.SHARED_SESSION);
    }

    public void setTipoSessaoUnica() {
        this.id = TypeContract.UNIQUE_SESSION;
    }

    public void setTipoSessaoCompartilhada() {
        this.id = TypeContract.SHARED_SESSION;
    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }
}
