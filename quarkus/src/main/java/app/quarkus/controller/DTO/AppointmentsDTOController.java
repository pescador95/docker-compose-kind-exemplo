package app.quarkus.controller.DTO;

import app.core.utils.StringBuilder;
import app.quarkus.DTO.AppointmentsDTO;
import app.quarkus.model.appointments.Appointments;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsDTOController {

    public static List<AppointmentsDTO> makeListAgendamentoDTO(List<Appointments> pSchedules) {

        List<AppointmentsDTO> listAppointmentsDTO = new ArrayList<>();

        pSchedules.forEach(appointments -> {
            AppointmentsDTO appointmentsDTO = new AppointmentsDTO();
            appointmentsDTO.idAgendamento = appointments.id;
            appointmentsDTO.serviceType = appointments.serviceType.serviceType;
            appointmentsDTO.cliente = appointments.personAppointments.name;
            appointmentsDTO.profissional = appointments.professionalAppointments.professionalName;
            appointmentsDTO.status = appointments.bookingStatus.status;
            appointmentsDTO.organization = appointments.organizationAppointments.name;
            appointmentsDTO.telephoneCliente = appointments.personAppointments.telephone;
            appointmentsDTO.cellphoneCliente = appointments.personAppointments.cellphone;
            appointmentsDTO.cellphoneOrganization = appointments.organizationAppointments.cellphone;
            appointmentsDTO.address = StringBuilder.makeAddressString(appointments.organizationAppointments.address);
            appointmentsDTO.appointmentDate = appointments.appointmentDate;
            appointmentsDTO.appointmentTime = appointments.appointmentTime;
            appointmentsDTO.professionalId = appointments.professionalAppointments.id;
            appointmentsDTO.organizationId = appointments.organizationAppointments.id;
            listAppointmentsDTO.add(appointmentsDTO);
        });
        return listAppointmentsDTO;
    }
}
