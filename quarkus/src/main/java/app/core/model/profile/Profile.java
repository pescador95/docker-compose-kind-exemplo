package app.core.model.profile;

import app.quarkus.model.person.PersonalActivity;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "profile")

public class Profile extends PanacheEntityBase {

    @Column()
    @SequenceGenerator(name = "profileIdSequence", sequenceName = "profile_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "profileIdSequence")
    @Id
    public Long id;

    @Column()
    public String originalName;

    @Column()
    public String keyName;

    @Column()
    public String mimetype;

    @Column()
    public LocalDateTime createAt;

    @Column()
    public Long fileSize;

    @ManyToOne()
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "personalactivityId")
    public PersonalActivity personalActivity;
    @Column()
    public String fileReference;

    @Column()
    public String personName;

    public Profile() {

    }
}