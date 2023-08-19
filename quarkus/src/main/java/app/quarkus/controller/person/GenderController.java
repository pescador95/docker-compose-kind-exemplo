package app.quarkus.controller.person;

import app.core.model.DTO.Responses;
import app.core.utils.BasicFunctions;
import app.quarkus.model.person.Gender;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@Transactional
public class GenderController {

    private Gender gender = new Gender();

    private Responses responses;

    public Response addGenero(@NotNull Gender pGender) {

        responses = new Responses();
        responses.messages = new ArrayList<>();

        if (BasicFunctions.isNotEmpty(pGender.gender)) {
            gender = Gender.find("gender = ?1 ", pGender.gender).firstResult();
        }

        if (BasicFunctions.isEmpty(gender)) {
            gender = new Gender();

            if (BasicFunctions.isNotEmpty(pGender.gender)) {
                gender.gender = pGender.gender;
            } else {
                responses.messages.add("Informe o Gênero a cadastrar.");
            }
            if (!responses.hasMessages()) {
                gender.persist();

                responses.status = 201;
                responses.data = gender;
                responses.messages.add("Gênero registered successfully!");

            } else {
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } else {
            responses.status = 400;
            responses.data = gender;
            responses.messages.add("Gênero already registered!");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response updateGenero(@NotNull Gender pGender) {

        responses = new Responses();
        responses.messages = new ArrayList<>();

        try {

            if (pGender.isValid()) {
                gender = Gender.findById(pGender.id);
            }
            if (!pGender.isValid() && BasicFunctions.isEmpty(pGender.gender)) {
                throw new BadRequestException("Enter data for update the registration of Gênero.");
            } else {
                if (BasicFunctions.isNotEmpty(pGender.gender)) {
                    gender.gender = pGender.gender;
                }
                gender.persist();
                responses.status = 200;
                responses.data = gender;
                responses.messages.add("Gênero updated successfully!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            responses.data = gender;
            responses.messages.add("cannot update o cadastro de Gênero.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response deleteGenero(@NotNull List<Long> pListIdGenero) {

        List<Gender> genders;
        List<Gender> generosAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        genders = Gender.list("id in ?1", pListIdGenero);
        int count = genders.size();

        try {

            if (genders.isEmpty()) {
                responses.status = 400;
                responses.messages.add("Gêneros not found or already deleted.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }

            genders.forEach((genero) -> {
                genero.delete();
                generosAux.add(genero);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = gender;
                responses.messages.add("Gênero successfully deleted!");
            } else {
                responses.datas = Collections.singletonList(generosAux);
                responses.messages.add(count + " Gêneros successfully deleted!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = gender;
                responses.messages.add("Gênero not found or already deleted.");
            } else {
                responses.datas = Collections.singletonList(genders);
                responses.messages.add("Gêneros not found or already deleted.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
