package app.core.controller.auth;

import app.core.model.DTO.Responses;
import app.core.model.contract.Contract;
import app.core.model.contract.TypeContract;
import app.core.utils.BasicFunctions;
import app.quarkus.model.organization.Organization;
import app.quarkus.model.person.User;
import jakarta.ws.rs.core.Context;
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
public class ContractController {

    @Context
    SecurityContext context;
    private Contract contract = new Contract();
    private Responses responses;
    private User userAuth;

    public static TypeContract getTypeContractByUserOrganizationDefault(User pUser) {

        Organization organizationDefault = pUser.organizationDefault;

        TypeContract typeContract = new TypeContract();

        Contract contract = Contract.find("organizationContract = ?1", organizationDefault).firstResult();

        if (BasicFunctions.isNotEmpty(contract)) {
            typeContract = contract.typeContract;
        }
        return typeContract;
    }

    public Response addContract(@NotNull Contract pContract) {

        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = app.core.utils.Context.getContextUser(context);

        Organization organization = new Organization();

        if (BasicFunctions.isNotEmpty(pContract.organizationContract) && pContract.organizationContract.isValid()) {
            contract = Contract.find("organizationContract = ?1 and active = true", pContract.organizationContract)
                    .firstResult();

            organization = Organization.findById(pContract.organizationContract.id);
        }

        if (BasicFunctions.isEmpty(contract.responsibleContract)) {
            contract = new Contract();

            if (BasicFunctions.isNotEmpty(pContract.responsibleContract)) {
                contract.responsibleContract = pContract.responsibleContract;
            }
            if (BasicFunctions.isNotEmpty(organization)) {
                contract.organizationContract = organization;
            }
            if (BasicFunctions.isValid(pContract.maximumSessions)) {
                contract.maximumSessions = pContract.maximumSessions;
            } else {
                responses.status = 400;
                responses.data = contract;
                responses.messages.add("Put me Maxinum Session number!");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }
            if (BasicFunctions.isNotEmpty(pContract.considerations)) {
                contract.considerations = pContract.considerations;
            }
            if (BasicFunctions.isValid(pContract.dataContract)) {
                contract.dataContract = pContract.dataContract;
            }
            if (BasicFunctions.isNotEmpty(pContract.typeContract) && pContract.typeContract.isValid()) {
                contract.typeContract.id = pContract.typeContract.id;
            } else {
                contract.typeContract.setTipoSessaoUnica();
            }
            if (!responses.hasMessages()) {
                contract.user = userAuth;
                contract.updatedBy = userAuth;
                contract.active = Boolean.TRUE;
                contract.updatedAt = LocalDateTime.now();
                contract.persist();

                responses.status = 201;
                responses.data = contract;
                responses.messages.add("Contract registered successfully!");

            } else {
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } else {

            responses.status = 400;
            responses.data = contract;
            responses.messages.add("Contract already registered!");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response updateContract(@NotNull Contract pContract) {

        userAuth = app.core.utils.Context.getContextUser(context);

        responses = new Responses();
        responses.messages = new ArrayList<>();

        try {

            if (pContract.isValid()) {
                contract = Contract.findById(pContract.id);
            }
            if (!pContract.isValid() && BasicFunctions.isEmpty(pContract.responsibleContract)
                    && BasicFunctions.isEmpty(pContract.organizationContract)) {
                throw new BadRequestException("Put the infos to update the Contract.");
            } else {
                if (BasicFunctions.isNotEmpty(pContract.responsibleContract)) {
                    contract.responsibleContract = pContract.responsibleContract;
                }
                if (BasicFunctions.isNotEmpty(pContract.organizationContract)) {
                    contract.organizationContract = pContract.organizationContract;
                }
                if (BasicFunctions.isNotEmpty(pContract.maximumSessions)) {
                    contract.maximumSessions = pContract.maximumSessions;
                }
                if (BasicFunctions.isNotEmpty(pContract.considerations)) {
                    contract.considerations = pContract.considerations;
                }
                if (BasicFunctions.isNotEmpty(pContract.dataContract)) {
                    contract.dataContract = pContract.dataContract;
                }
                if (BasicFunctions.isNotEmpty(pContract.typeContract) && pContract.typeContract.isValid()) {
                    contract.typeContract.id = pContract.typeContract.id;
                } else {
                    contract.typeContract.setTipoSessaoUnica();
                }
                contract.updatedBy = userAuth;
                contract.updatedAt = LocalDateTime.now();
                contract.persist();

                responses.status = 200;
                responses.data = contract;
                responses.messages.add("Contract successfully updated!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {

            responses.status = 400;
            responses.data = contract;
            responses.messages.add("Cannot update the Contract.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response deleteContract(@NotNull List<Long> pListIdContract) {

        List<Contract> contracts;
        List<Contract> contractsAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = app.core.utils.Context.getContextUser(context);

        contracts = Contract.list("id in ?1 and active = true", pListIdContract);
        int count = contracts.size();

        try {

            if (contracts.isEmpty()) {

                responses.status = 400;
                responses.messages.add("Contracts not found or already excluded.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }

            contracts.forEach((contract) -> {

                contract.updatedBy = userAuth;
                contract.active = Boolean.FALSE;
                contract.updatedAt = LocalDateTime.now();
                contract.deletedAt = LocalDateTime.now();
                contract.persist();
                contractsAux.add(contract);
            });

            responses.status = 200;
            if (count <= 1) {
                responses.data = contract;
                responses.messages.add("Contract deleted successfully!");
            } else {
                responses.datas = Collections.singletonList(contractsAux);
                responses.messages.add(count + " Contracts deleted successfully!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {

            responses.status = 400;
            if (count <= 1) {
                responses.data = contract;
                responses.messages.add("Contract not found or already reactivated.");
            } else {
                responses.datas = Collections.singletonList(contracts);
                responses.messages.add("Contracts not found or already reactivated.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response reactivateContract(@NotNull List<Long> pListIdContract) {

        List<Contract> contracts;
        List<Contract> contractsAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        User userAuth = app.core.utils.Context.getContextUser(context);
        contracts = Contract.list("id in ?1 and active = false", pListIdContract);
        int count = contracts.size();

        try {

            if (contracts.isEmpty()) {

                responses.status = 400;
                responses.messages.add("Contracts not found or already reactivated.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }

            contracts.forEach((contract) -> {

                contract.updatedBy = userAuth;
                contract.active = Boolean.TRUE;
                contract.updatedAt = LocalDateTime.now();
                contract.deletedAt = LocalDateTime.now();
                contract.persist();
                contractsAux.add(contract);
            });

            responses.status = 200;
            if (count <= 1) {
                responses.data = contract;
                responses.messages.add("Contract successfully reactivated!");
            } else {
                responses.datas = Collections.singletonList(contractsAux);
                responses.messages.add(count + " Contracts successfully reactivated!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {

            responses.status = 400;
            if (count <= 1) {
                responses.data = contract;
                responses.messages.add("Contract not found or already reactivated.");
            } else {
                responses.datas = Collections.singletonList(contracts);
                responses.messages.add("Contracts not found or already reactivated.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
