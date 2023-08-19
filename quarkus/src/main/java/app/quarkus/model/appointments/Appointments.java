package app.quarkus.model.appointments;

import app.core.utils.BasicFunctions;
import app.quarkus.model.organization.Organization;
import app.quarkus.model.person.Person;
import app.quarkus.model.person.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointments", indexes = {
        @Index(name = "iappointmentsak1", columnList = "appointmentDate, appointmentTime, personId, professionalId, organizationId, bookingStatusId, active")})
@JsonIgnoreProperties({"updatedBy", "active", "updatedAt", "deletedAt", "appointmentsOld"})
@NamedQueries({
        @NamedQuery(name = "qloadListAppointmentsByUsuarioDataAgendaOrganization", query = "SELECT a FROM Appointments a JOIN FETCH a.organizationAppointments o JOIN FETCH a.professionalAppointments u WHERE o.id = :organizationId AND u.id = :professionalId AND a.appointmentDate = :appointmentDate AND a.active = true AND o.active = true AND u.active = true"),
        @NamedQuery(name = "qloadListAgendamentosByDataAgendaOrganization", query = "SELECT a FROM Appointments a JOIN FETCH a.organizationAppointments o JOIN FETCH a.professionalAppointments p WHERE o.id = :organizationId AND a.appointmentDate = :appointmentDate AND a.active = true AND o.active = true AND p.active = true")})
public class Appointments extends PanacheEntityBase {

    @Column()
    @SequenceGenerator(name = "appointmentsIdSequence", sequenceName = "appointments_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "appointmentsIdSequence")
    @Id
    public Long id;

    @ManyToOne
    @JoinColumn(name = "serviceType")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public ServiceType serviceType;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "userId")
    public User user;

    @ManyToOne
    @JoinColumn(name = "personId")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Person personAppointments;

    @ManyToOne
    @JoinColumn(name = "professionalId")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public User professionalAppointments;

    @ManyToOne
    @JoinColumn(name = "bookingStatusId")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public BookingStatus bookingStatus;

    @ManyToOne
    @JoinColumn(name = "organizationId")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Organization organizationAppointments;

    @Column()
    public LocalDate appointmentDate;

    @Column()
    public LocalTime appointmentTime;
    @Column()
    public String personName;

    @Column()
    public String professionalName;

    @Column()
    public Boolean preference;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointmentsOldId")
    public Appointments appointmentsOld;

    @Column()
    @JsonIgnore
    public Boolean active;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    public User updatedBy;

    @Column()
    @JsonIgnore
    public LocalDateTime updatedAt;

    @Column()
    @JsonIgnore
    public LocalDateTime deletedAt;

    public Appointments() {

    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }

    public Boolean preference() {
        return BasicFunctions.isNotEmpty(this.preference);
    }

    public Boolean semPreferencia() {
        return !this.preference();
    }

    public Boolean hasAgendamentoOld() {
        return BasicFunctions.isNotEmpty(this.appointmentsOld) && BasicFunctions.isValid(this.appointmentsOld.id);
    }
}
