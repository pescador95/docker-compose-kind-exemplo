package app.quarkus.controller.appointments;

import app.core.model.DTO.Responses;
import app.core.utils.BasicFunctions;
import app.core.utils.Context;
import app.quarkus.model.appointments.Task;
import app.quarkus.model.person.PersonalActivity;
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
public class PersonalActivityController {

    @javax.ws.rs.core.Context
    SecurityContext context;
    private Task task = new Task();
    private Responses responses;
    private User userAuth;
    private PersonalActivity personalActivity;

    public Response addTask(@NotNull Task pTask) {

        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);

        if (BasicFunctions.isNotEmpty(pTask) && pTask.personalActivity.isValid()
                && BasicFunctions.isValid(pTask.taskDate)) {
            task = Task
                    .find("personalactivityId = ?1 and taskDate = ?2 and active = true",
                            pTask.personalActivity.id,
                            pTask.taskDate)
                    .firstResult();
        }

        if (BasicFunctions.isNotEmpty(pTask) && pTask.personalActivity.isValid()) {
            personalActivity = PersonalActivity
                    .find("id = ?1 and active = true", pTask.personalActivity.id)
                    .firstResult();
        }

        if (BasicFunctions.isEmpty(task)) {
            task = new Task();

            if (BasicFunctions.isNotEmpty(task) && BasicFunctions.isNotEmpty(personalActivity)) {
                task.personalActivity = personalActivity;
            }
            if (BasicFunctions.isNotEmpty(pTask.avaliation)) {
                task.avaliation = pTask.avaliation;
            }
            if (BasicFunctions.isNotEmpty(pTask.taskDate)) {
                task.taskDate = pTask.taskDate;
            }
            if (BasicFunctions.isNotEmpty(pTask.task)) {
                task.task = pTask.task;
            }
            if (BasicFunctions.isNotEmpty(pTask.summary)) {
                task.summary = pTask.summary;
            }
            if (!responses.hasMessages()) {
                task.user = userAuth;
                task.updatedBy = userAuth;
                task.active = Boolean.TRUE;
                task.updatedAt = LocalDateTime.now();
                task.persist();

                responses.status = 201;
                responses.data = task;
                responses.messages.add("Tasks registered successfully!");

            } else {
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } else {
            responses.status = 400;
            responses.data = task;
            responses.messages.add("Tasks already registered!");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response updateTask(@NotNull Task pTask) {

        userAuth = Context.getContextUser(context);

        responses = new Responses();
        responses.messages = new ArrayList<>();

        try {

            if (pTask.isValid()) {
                task = Task.find("id = ?1 and active = true", pTask.id).firstResult();
            }

            if (BasicFunctions.isEmpty(pTask)
                    || !pTask.isValid() && !pTask.personalActivity.isValid()
                            && BasicFunctions.isEmpty(pTask.avaliation)
                            && BasicFunctions.isInvalid(pTask.taskDate)
                            && BasicFunctions.isEmpty(pTask.summary)) {
                throw new BadRequestException("Enter data for update the Tasks.");
            } else {
                if (BasicFunctions.isNotEmpty(pTask.personalActivity)) {
                    task.personalActivity = pTask.personalActivity;
                }
                if (BasicFunctions.isNotEmpty(pTask.taskDate)) {
                    task.taskDate = pTask.taskDate;
                }
                if (BasicFunctions.isNotEmpty(pTask.avaliation)) {
                    task.avaliation = pTask.avaliation;
                }
                if (BasicFunctions.isNotEmpty(pTask.task)) {
                    task.task = pTask.task;
                }
                if (BasicFunctions.isNotEmpty(pTask.summary)) {
                    task.summary = pTask.summary;
                }
                task.updatedBy = userAuth;
                task.updatedAt = LocalDateTime.now();
                task.persist();

                responses.status = 200;
                responses.data = task;
                responses.messages.add("Tasks updated successfully!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            responses.data = task;
            responses.messages.add("cannot update the Tasks.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response deleteTask(@NotNull List<Long> pListIdTask) {

        List<Task> tasks;
        List<Task> tasksAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);
        tasks = Task.list("id in ?1 and active = true", pListIdTask);
        int count = tasks.size();

        try {

            if (tasks.isEmpty()) {
                responses.status = 400;
                responses.messages.add("Tasks not found or already deleted.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }

            tasks.forEach((task) -> {

                task.updatedBy = userAuth;
                task.active = Boolean.FALSE;
                task.updatedAt = LocalDateTime.now();
                task.deletedAt = LocalDateTime.now();
                task.persist();
                tasksAux.add(task);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = task;
                responses.messages.add("Tasks successfully deleted!");
            } else {
                responses.datas = Collections.singletonList(tasksAux);
                responses.messages.add(count + " Tasks successfully deleted!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = task;
                responses.messages.add("Tasks not found or already deleted.");
            } else {
                responses.datas = Collections.singletonList(tasks);
                responses.messages.add("Tasks not found or already deleted.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response reactivateTask(@NotNull List<Long> pListIdTask) {

        List<Task> tasks;
        List<Task> tasksAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        User userAuth = Context.getContextUser(context);
        tasks = Task.list("id in ?1 and active = false", pListIdTask);
        int count = tasks.size();

        try {

            if (tasks.isEmpty()) {
                responses.status = 400;
                responses.messages.add("Tasks not located or already reactivated.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }

            tasks.forEach((task) -> {

                task.updatedBy = userAuth;
                task.active = Boolean.TRUE;
                task.updatedAt = LocalDateTime.now();
                task.deletedAt = LocalDateTime.now();
                task.persist();
                tasksAux.add(task);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = task;
                responses.messages.add("Tasks reactived with sucessful!");
            } else {
                responses.datas = Collections.singletonList(tasksAux);
                responses.messages.add(count + " Tasks reactived with sucessful!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = task;
                responses.messages.add("Tasks not located or already reactivated.");
            } else {
                responses.datas = Collections.singletonList(tasks);
                responses.messages.add("Tasks not located or already reactivated.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
