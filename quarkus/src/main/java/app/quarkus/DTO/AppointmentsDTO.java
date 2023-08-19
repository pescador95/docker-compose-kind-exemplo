package app.quarkus.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentsDTO {

    public Long idAgendamento;

    public String serviceType;
    public String cliente;
    public String profissional;

    public String organization;

    public String address;
    public String cellphoneOrganization;
    public String telephoneCliente;
    public String cellphoneCliente;

    public LocalDate appointmentDate;

    public LocalTime appointmentTime;

    public String status;

    public Long organizationId;

    public Long professionalId;

    public AppointmentsDTO() {

    }

    public AppointmentsDTO(Long idAgendamento,
                           String serviceType,
                           String cliente,
                           String profissional,
                           String organization,
                           String address,
                           String cellphoneOrganization,
                           String telephoneCliente,
                           LocalDate appointmentDate,
                           LocalTime appointmentTime,
                           String status,
                           String cellphoneCliente,
                           Long organizationId,
                           Long professionalId) {
        this.idAgendamento = idAgendamento;
        this.serviceType = serviceType;
        this.cliente = cliente;
        this.profissional = profissional;
        this.organization = organization;
        this.address = address;
        this.cellphoneOrganization = cellphoneOrganization;
        this.telephoneCliente = telephoneCliente;
        this.cellphoneCliente = cellphoneCliente;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.organizationId = organizationId;
        this.professionalId = professionalId;

    }
}
