package app.core.controller.profile;

import app.core.model.DTO.Responses;
import app.core.model.profile.Routine;
import app.core.utils.BasicFunctions;
import app.core.utils.Context;
import app.quarkus.model.person.User;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
@Transactional
public class RoutineController {

    @javax.ws.rs.core.Context
    SecurityContext context;
    private Routine routine = new Routine();
    private Responses responses;
    private User userAuth;

    public Response addRoutine(@NotNull Routine pRoutine) {

        responses = new Responses();
        responses.messages = new ArrayList<>();
        userAuth = Context.getContextUser(context);

        routine = new Routine();

        if (BasicFunctions.isNotEmpty(pRoutine.name)) {
            routine.name = pRoutine.name;
        }
        if (BasicFunctions.isNotEmpty(pRoutine.icon)) {
            routine.icon = pRoutine.icon;
        }
        if (BasicFunctions.isNotEmpty(pRoutine.path)) {
            routine.path = pRoutine.path;
        }
        if (BasicFunctions.isNotEmpty(pRoutine.title)) {
            routine.title = pRoutine.title;
        }
        routine.user = userAuth;
        routine.updatedBy = userAuth;
        routine.updatedAt = LocalDateTime.now();
        routine.persist();

        responses.status = 201;
        responses.data = routine;
        responses.messages.add("Routine registered successfully!");
        return Response.ok(responses).status(Response.Status.ACCEPTED).build();
    }

    public Response updateRoutine(@NotNull Routine pRoutine) throws BadRequestException {

        userAuth = Context.getContextUser(context);

        responses = new Responses();
        responses.messages = new ArrayList<>();

        try {

            if (BasicFunctions.isValid(pRoutine.id)) {
                routine = Routine.findById(pRoutine.id);
            }

            if (BasicFunctions.isEmpty(routine)) {
                responses.messages.add("The routine which you want to change the data was not found!");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            } else {
                if (BasicFunctions.isNotEmpty(pRoutine.path)) {
                    if (!routine.path.equals(pRoutine.path)) {
                        routine.path = pRoutine.path;
                    }
                }

                if (BasicFunctions.isNotEmpty(pRoutine.name)) {
                    if (!Objects.equals(routine.name, pRoutine.name)) {
                        routine.name = pRoutine.name;
                    }
                }

                if (BasicFunctions.isNotEmpty(pRoutine.title)) {
                    if (BasicFunctions.isNotEmpty(routine.title) && !routine.title.equals(pRoutine.title)) {
                        routine.title = pRoutine.title;
                    }
                }
                if (BasicFunctions.isNotEmpty(pRoutine.icon)) {
                    if (!routine.icon.equals(pRoutine.icon)) {
                        routine.icon = pRoutine.icon;
                    }
                }

                routine.persist();
                routine.updatedBy = userAuth;
                routine.updatedAt = LocalDateTime.now();
                responses.status = 200;
                responses.data = routine;
                responses.messages.add("Routine updated successfully!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            responses.data = routine;
            responses.messages.add("cannot update a Routine.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response deleteRoutine(@NotNull List<Long> pListRoutine) {

        List<Routine> routines;
        List<Routine> routinesAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);
        routines = Routine.list("id in ?1", pListRoutine);
        int count = routines.size();

        if (BasicFunctions.isEmpty(routines)) {
            responses.status = 400;
            responses.messages.add("Routines not located or already excluded.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }

        try {

            routines.forEach((routine) -> {
                routinesAux.add(routine);
                routine.delete();

            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = routine;
                responses.messages.add("Routine successfully deleted!");
            } else {
                responses.datas = Collections.singletonList(routinesAux);
                responses.messages.add(count + " Routines successfully deleted!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = routine;
                responses.messages.add("Routine not found or already deleted.");
            } else {
                responses.datas = Collections.singletonList(routines);
                responses.messages.add("Routines not found or already deleted.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
