package app.quarkus.model.appointments;

import app.core.utils.BasicFunctions;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
@Table(name = "bookingStatus")
public class BookingStatus extends PanacheEntityBase {

    public static final long SCHEDULED = 1;
    public static final long RESCHEDULED = 2;
    public static final long CANCELED = 4;
    public static final long FREE = 5;
    @Column()
    @SequenceGenerator(name = "statusAgendamentoIdSequence", sequenceName = "statusAgendamento_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "statusAgendamentoIdSequence")
    @Id
    public Long id;
    @Column()
    public String status;

    public BookingStatus() {
    }

    public Boolean scheduled() {
        return this.id == SCHEDULED;
    }

    public Boolean rescheduled() {
        return this.id == RESCHEDULED;
    }

    public Boolean canceled() {
        return this.id == CANCELED;
    }

    public Boolean free() {
        return this.id == FREE;
    }

    public Boolean isValid() {
        return BasicFunctions.isValid(this.id);
    }

    public void setScheduled() {
        this.id = SCHEDULED;
    }

    public void setRescheduled() {
        this.id = RESCHEDULED;
    }

    public void setCanceled() {
        this.id = CANCELED;
    }

    public void setFree() {
        this.id = FREE;
    }
}
