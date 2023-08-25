
package app.core.services.auth;

import app.quarkus.model.person.User;
import app.core.controller.contract.ContractController;
import app.core.model.DTO.Responses;
import app.core.model.contract.TypeContract;
import app.core.utils.BasicFunctions;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.redis.client.RedisAPI;
import io.vertx.mutiny.redis.client.Response;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.Collections;

@ApplicationScoped
public class RedisService {

    private final RedisAPI redisAPI;

    @Context
    SecurityContext secContext;

    @Inject
    app.core.utils.Context context;

    @Inject
    public RedisService(RedisAPI redisAPI) {
        this.redisAPI = redisAPI;
    }

    public Uni<Response> get(String key) {
        return redisAPI.get(key);
    }

    public void setex(String key, String seconds, String value, TypeContract typeContract) {

        if (typeContract.sharedSession()) {
            key = key + value;
        }

        redisAPI.setex(key, seconds, value).await().indefinitely();
    }

    public javax.ws.rs.core.Response del() {

        Responses responses = new Responses();
        responses.messages = new ArrayList<>();

        try {

            User user = app.core.utils.Context.getContextUser(secContext);

            String key = makeSessionKeyPattern(user);

            redisAPI.del(Collections.singletonList(key)).await().indefinitely();

            countActiveSessionsForUserAndOrganization(user, true);
            responses.status = 200;
            responses.messages.add("User Session " + user.login +
                    " closed with success!");
            System.out.println("User Session " + user.login +
                    " closed with success!");
            return javax.ws.rs.core.Response.ok(responses).status(javax.ws.rs.core.Response.Status.ACCEPTED).build();

        } catch (Exception e) {
            responses.status = 400;
            responses.messages.add("Error at close Redis Session!");
            return javax.ws.rs.core.Response.ok(responses).status(javax.ws.rs.core.Response.Status.BAD_REQUEST).build();

        }

    }

    public int countActiveSessionsForUserAndOrganization(User user, Boolean firstTime) {

        TypeContract typeContract = ContractController.getTypeContractByUserOrganizationDefault(user);

        String key = getSessionDefaultPattern(user);

        Uni<Integer> activeSessionsCount = redisAPI.keys(key)
                .onItem().transform(Response::size);

        Integer count = activeSessionsCount.await().indefinitely();

        logSessoesAtivas(typeContract, user, firstTime, count);

        return count;
    }

    public void logSessoesAtivas(TypeContract typeContract, User user,
            Boolean firstTime, Integer count) {

        String pattern = getSessionDefaultPattern(user);

        String patternName = "";

        if (firstTime) {
            System.out.println("Number of active Sessions: " + count + " for the User: "
                    + user.login);
            if (typeContract.sharedSession()) {
                patternName = "Shared Session with the pattern " + pattern;
            }
            if (typeContract.uniqueSession()) {
                patternName = "Unique Session with the pattern " + pattern;
            }
            System.out.println(patternName);
        }

    }

    public String makeSessionKeyPattern(User user) {

        String accessToken = app.core.utils.Context.getContextUserKey(secContext);

        TypeContract typeContract = ContractController.getTypeContractByUserOrganizationDefault(user);

        if (BasicFunctions.isNotEmpty(typeContract)) {
            if (typeContract.sharedSession()) {
                return "shared_" + user.login + "_organizationId_" +
                        user.organizationDefault.id + "_accessToken_" + accessToken;
            } else {
                return "unique_" + user.login + "_organizationId_" +
                        user.organizationDefault.id;
            }
        }
        return "";
    }

    public String getSessionDefaultPattern(User user) {

        TypeContract typeContract = ContractController.getTypeContractByUserOrganizationDefault(user);

        if (BasicFunctions.isNotEmpty(typeContract)) {
            if (typeContract.sharedSession()) {
                return "shared_" + user.login + "_organizationId_" +
                        user.organizationDefault.id + "_accessToken_" + "";
            } else {
                return "unique_" + user.login + "_organizationId_" +
                        user.organizationDefault.id;
            }
        }
        return "";
    }

    public void setex(User user, String expiration, String accessToken,
            TypeContract typeContract) {

        setex(makeSessionKeyPattern(user), expiration, accessToken,
                typeContract);

        countActiveSessionsForUserAndOrganization(user, Boolean.TRUE);

    }

    public javax.ws.rs.core.Response flushRedis() {
        Responses responses = new Responses();
        responses.messages = new ArrayList<>();
        try {
            redisAPI.flushdbAndAwait(Collections.emptyList());
            responses.status = 200;
            responses.messages.add("Redis Sessions closed with success!");
            System.out.println("Redis database flushed!");
            return javax.ws.rs.core.Response.ok(responses).status(javax.ws.rs.core.Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            responses.messages.add("Error aa close the Redis Session!");
            return javax.ws.rs.core.Response.ok(responses).status(javax.ws.rs.core.Response.Status.BAD_REQUEST).build();

        }
    }
}
