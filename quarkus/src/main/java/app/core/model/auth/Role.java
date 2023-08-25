package app.core.model.auth;

import app.core.utils.BasicFunctions;
import app.quarkus.model.person.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.RolesValue;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role extends PanacheEntityBase {

    @Column()
    @SequenceGenerator(name = "roleIdSequence", sequenceName = "role_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "roleIdSequence")
    @Id
    public Long id;

    @Column
    @RolesValue
    public String role;

    @Column
    public Boolean admin;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public User user;

    public Role() {

    }

    public Boolean hasPrivilegio() {
        return BasicFunctions.isNotEmpty(this.role);
    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }

    public Boolean admin() {
        return BasicFunctions.isNotEmpty(admin) && this.admin;
    }

    public void setUser() {
        this.id = User.USER;
    }

    public void setAdmin() {
        this.id = User.ADMINISTRATOR;
        this.admin = true;
    }

    public void setBot() {
        this.id = User.BOT;
    }
}