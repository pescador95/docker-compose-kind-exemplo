package app.quarkus.model.person;

import app.core.utils.BasicFunctions;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
@Table(name = "gender")
public class Gender extends PanacheEntityBase {

    @Column()
    @SequenceGenerator(name = "genderIdSequence", sequenceName = "gender_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "genderIdSequence")
    @Id
    public Long id;
    @Column()
    public String gender;

    public Gender() {
    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }
}
