package app.quarkus.model.person;

import app.core.model.auth.Role;
import app.core.utils.BasicFunctions;
import app.quarkus.model.appointments.ServiceType;
import app.quarkus.model.organization.Organization;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UserDefinition
@JsonIgnoreProperties({"active", "updatedAt", "deletedAt"})
@Entity
@Table(name = "app_user", indexes = {@Index(name = "iuserak1", columnList = "personId, login, organizationDefaultId, active")})
public class User extends PanacheEntityBase {

    public static final Long USER = 1L;
    public static final Long BOT = 2L;
    public static final Long ADMINISTRATOR = 3L;
    @Column()
    @SequenceGenerator(name = "app_userIdSequence", sequenceName = "app_user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "app_userIdSequence")
    @Id
    public Long id;
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Password
    public String password;
    @Column(nullable = false)
    @Username
    public String login;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "personId")
    public Person person;
    @Column()
    @JsonIgnore
    public Boolean active;
    @Column()
    public Boolean changePassword;
    @Column()
    public Boolean bot;
    @Column()
    public LocalDateTime updatedAt;
    @Column()
    public LocalDateTime deletedAt;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "userroles", joinColumns = {@JoinColumn(name = "userId")}, inverseJoinColumns = {
            @JoinColumn(name = "roleId")})
    @Roles
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<Role> role = new ArrayList<>();
    @Column()
    @JsonIgnore
    public String user;
    @Column()
    public String professionalName;
    @Column()
    @JsonIgnore
    public String updatedBy;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "userorganization", joinColumns = {@JoinColumn(name = "userId")}, inverseJoinColumns = {
            @JoinColumn(name = "organizationId")})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<Organization> organizations;
    @ManyToOne
    @JoinColumn(name = "organizationDefaultId")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Organization organizationDefault;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "servicetypesusers", joinColumns = {
            @JoinColumn(name = "professionalId")}, inverseJoinColumns = {
            @JoinColumn(name = "serviceTypeId")})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<ServiceType> serviceTypes = new ArrayList<>();

    public User() {

    }

    public Boolean bot() {
        return this.role.stream().anyMatch(role -> role.id.equals(BOT));
    }

    public Boolean admin() {
        return this.role.stream().anyMatch(role -> role.id.equals(ADMINISTRATOR));
    }

    public Boolean user() {
        return this.role.stream().anyMatch(role -> role.id.equals(USER));
    }

    public Boolean hasRole() {
        return BasicFunctions.isValid(this.id) && BasicFunctions.isNotEmpty(this.role);
    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }
}
