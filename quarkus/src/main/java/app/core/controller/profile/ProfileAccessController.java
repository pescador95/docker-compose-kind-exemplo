package app.core.controller.profile;

import app.core.model.DTO.Responses;
import app.core.model.profile.ProfileAccess;
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

@ApplicationScoped
@Transactional
public class ProfileAccessController {

    @javax.ws.rs.core.Context
    SecurityContext context;
    private ProfileAccess profileAccess = new ProfileAccess();
    private Responses responses;
    private User userAuth;

    public Response addProfileAccess(@NotNull ProfileAccess pProfileAccess) {

        responses = new Responses();
        responses.messages = new ArrayList<>();
        userAuth = Context.getContextUser(context);

        profileAccess = new ProfileAccess();

        List<Routine> routines = new ArrayList<>();

        List<Long> routinesId = new ArrayList<>();

        if (pProfileAccess.hasRoutines()) {
            pProfileAccess.routines.forEach(serviceType -> routinesId.add(serviceType.id));
            routines = Routine.list("id in ?1", routinesId);
        }

        if (BasicFunctions.isNotEmpty(pProfileAccess.name)) {
            profileAccess.name = pProfileAccess.name;
        }
        if (BasicFunctions.isNotEmpty(pProfileAccess.create)) {
            profileAccess.create = pProfileAccess.create;
        }
        if (BasicFunctions.isNotEmpty(pProfileAccess.read)) {
            profileAccess.read = pProfileAccess.read;
        }
        if (BasicFunctions.isNotEmpty(pProfileAccess.update)) {
            profileAccess.update = pProfileAccess.update;
        }
        if (BasicFunctions.isNotEmpty(pProfileAccess.delete)) {
            profileAccess.delete = pProfileAccess.delete;
        }
        if (BasicFunctions.isNotEmpty(pProfileAccess.routines)) {
            profileAccess.routines = pProfileAccess.routines;
        }
        if (BasicFunctions.isNotEmpty(routines)) {
            profileAccess.routines = new ArrayList<>();
            profileAccess.routines.addAll(routines);
        }
        profileAccess.user = userAuth;
        profileAccess.updatedBy = userAuth;
        profileAccess.updatedAt = LocalDateTime.now();
        profileAccess.persist();

        responses.status = 201;
        responses.data = profileAccess;
        responses.messages.add("Profile Access registered successfully!");
        return Response.ok(responses).status(Response.Status.ACCEPTED).build();
    }

    public Response updateProfileAccess(@NotNull ProfileAccess pProfileAccess) throws BadRequestException {

        userAuth = Context.getContextUser(context);

        responses = new Responses();
        responses.messages = new ArrayList<>();

        List<Routine> routines = new ArrayList<>();

        List<Long> routinesId = new ArrayList<>();

        if (pProfileAccess.hasRoutines()) {
            pProfileAccess.routines.forEach(serviceType -> routinesId.add(serviceType.id));
            routines = Routine.list("id in ?1", routinesId);
        }

        try {

            if (BasicFunctions.isNotEmpty(pProfileAccess)) {
                profileAccess = ProfileAccess.findById(pProfileAccess.id);
            }

            if (BasicFunctions.isEmpty(profileAccess)) {
                responses.messages.add("The profile access which you want to change the data was not found!");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            } else {
                if (BasicFunctions.isNotEmpty(pProfileAccess.read)) {
                    if (!profileAccess.read.equals(pProfileAccess.read)) {
                        profileAccess.read = pProfileAccess.read;
                    }
                }

                if (BasicFunctions.isNotEmpty(pProfileAccess.name)) {
                    if (!profileAccess.name.equals(pProfileAccess.name)) {
                        profileAccess.name = pProfileAccess.name;
                    }
                }

                if (BasicFunctions.isNotEmpty(pProfileAccess.update)) {
                    if (BasicFunctions.isNotEmpty(profileAccess.update)
                            && !profileAccess.update.equals(pProfileAccess.update)) {
                        profileAccess.update = pProfileAccess.update;
                    }
                }
                if (BasicFunctions.isNotEmpty(pProfileAccess.create)) {
                    if (!profileAccess.create.equals(pProfileAccess.create)) {
                        profileAccess.create = pProfileAccess.create;
                    }
                }
                if (BasicFunctions.isNotEmpty(pProfileAccess.delete)) {
                    profileAccess.delete = pProfileAccess.delete;
                }
                if (pProfileAccess.hasRoutines()) {
                    profileAccess.routines = pProfileAccess.routines;
                }
                if (BasicFunctions.isNotEmpty(routines)) {
                    profileAccess.routines = new ArrayList<>();
                    profileAccess.routines.addAll(routines);
                }
                profileAccess.updatedBy = userAuth;
                profileAccess.updatedAt = LocalDateTime.now();
                profileAccess.persist();

                responses.status = 200;
                responses.data = profileAccess;
                responses.messages.add("Profile Access updated successfully!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            responses.data = profileAccess;
            responses.messages.add("cannot update o Profile Access.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response deleteProfileAccess(@NotNull List<Long> pListProfileAccess) {

        List<ProfileAccess> profileAccesss;
        List<ProfileAccess> profileAccesssAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        profileAccesss = ProfileAccess.list("id in ?1", pListProfileAccess);
        int count = profileAccesss.size();

        if (profileAccesss.isEmpty()) {
            responses.status = 400;
            responses.messages.add("Profile Access not located or already excluded.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }

        try {

            profileAccesss.forEach((profileAccess) -> {
                profileAccesssAux.add(profileAccess);
                profileAccess.delete();

            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = profileAccess;
                responses.messages.add("Profile Access successfully deleted!");
            } else {
                responses.datas = Collections.singletonList(profileAccesssAux);
                responses.messages.add(count + " Profile Access successfully deleted!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = profileAccess;
                responses.messages.add("Profile Access not found or already deleted.");
            } else {
                responses.datas = Collections.singletonList(profileAccesss);
                responses.messages.add("Profile Access not found or already deleted.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
