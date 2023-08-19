package app.quarkus.controller.address;

import app.core.model.DTO.Responses;
import app.core.utils.BasicFunctions;
import app.core.utils.Context;
import app.quarkus.model.address.Address;
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
public class AddressController {

    @javax.ws.rs.core.Context
    SecurityContext context;
    private Address address = new Address();
    private Responses responses;
    private User userAuth;

    public Response addAdress(@NotNull Address pAddress) {

        responses = new Responses();
        responses.messages = new ArrayList<>();
        userAuth = Context.getContextUser(context);

        address = new Address();

        if (BasicFunctions.isNotEmpty(pAddress.zipCode)) {
            address.zipCode = pAddress.zipCode;
        }
        if (BasicFunctions.isNotEmpty(pAddress.publicPlace)) {
            address.publicPlace = pAddress.publicPlace;
        }
        if (BasicFunctions.isNotEmpty(pAddress.number)) {
            address.number = pAddress.number;
        }
        if (BasicFunctions.isNotEmpty(pAddress.complement)) {
            address.complement = pAddress.complement;
        }
        if (BasicFunctions.isNotEmpty(pAddress.city)) {
            address.city = pAddress.city;
        }
        if (BasicFunctions.isNotEmpty(pAddress.state)) {
            address.state = pAddress.state;
        }
        address.user = userAuth;
        address.updatedBy = userAuth;
        address.active = Boolean.TRUE;
        address.updatedAt = LocalDateTime.now();
        address.persist();

        responses.status = 201;
        responses.data = address;
        responses.messages.add("Address registered successfully!");
        return Response.ok(responses).status(Response.Status.ACCEPTED).build();
    }

    public Response updateAdress(@NotNull Address pAddress) throws BadRequestException {

        userAuth = Context.getContextUser(context);

        responses = new Responses();
        responses.messages = new ArrayList<>();

        try {

            if (pAddress.isValid()) {
                address = Address.findById(pAddress.id);
            }

            if (!address.isValid()) {
                responses.messages.add("The address which you want to change the data was not found!");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            } else {
                if (BasicFunctions.isNotEmpty(pAddress.number)) {
                    if (!address.number.equals(pAddress.number)) {
                        address.number = pAddress.number;
                    }
                }
                if (BasicFunctions.isNotEmpty(pAddress.city)) {
                    if (!address.city.equals(pAddress.city)) {
                        address.city = pAddress.city;
                    }
                }

                if (BasicFunctions.isNotEmpty(pAddress.zipCode)) {
                    if (!address.zipCode.equals(pAddress.zipCode)) {
                        address.zipCode = pAddress.zipCode;
                    }
                }

                if (BasicFunctions.isNotEmpty(pAddress.state)) {
                    if (!address.state.equals(pAddress.state)) {
                        address.state = pAddress.state;
                    }
                }
                if (BasicFunctions.isNotEmpty(pAddress.complement)) {
                    if (BasicFunctions.isNotEmpty(address.complement)
                            && !address.complement.equals(pAddress.complement)) {
                        address.complement = pAddress.complement;
                    }
                }
                if (BasicFunctions.isNotEmpty(pAddress.publicPlace)) {
                    if (!address.publicPlace.equals(pAddress.publicPlace)) {
                        address.publicPlace = pAddress.publicPlace;
                    }
                }
                address.updatedBy = userAuth;
                address.updatedAt = LocalDateTime.now();
                address.persist();

                responses.status = 200;
                responses.data = address;
                responses.messages.add("Address updated successfully!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            responses.data = address;
            responses.messages.add("cannot update o Address.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response deleteAdress(@NotNull List<Long> pListAdress) {

        List<Address> addressses;
        List<Address> addresssAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);
        addressses = Address.list("id in ?1 and active = true", pListAdress);
        int count = addressses.size();

        if (addressses.isEmpty()) {
            responses.status = 400;
            responses.messages.add("Persons not located or already excluded.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }

        try {

            addressses.forEach((address) -> {

                address.updatedBy = userAuth;
                address.active = Boolean.FALSE;
                address.updatedAt = LocalDateTime.now();
                address.deletedAt = LocalDateTime.now();
                address.persist();
                addresssAux.add(address);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = address;
                responses.messages.add("Address successfully deleted!");
            } else {
                responses.datas = Collections.singletonList(addresssAux);
                responses.messages.add(count + " Address successfully deleted!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = address;
                responses.messages.add("Address not found or already deleted.");
            } else {
                responses.datas = Collections.singletonList(addressses);
                responses.messages.add("Address not found or already deleted.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response reactivateAdress(@NotNull List<Long> pListaIdAdress) {

        List<Address> addressses;
        List<Address> addresssAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);
        addressses = Address.list("id in ?1 and active = false", pListaIdAdress);
        int count = addressses.size();

        if (addressses.isEmpty()) {
            responses.status = 400;
            responses.messages.add("Address not found or already reactivate.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }

        try {
            addressses.forEach((address) -> {

                address.updatedBy = userAuth;
                address.active = Boolean.TRUE;
                address.updatedAt = LocalDateTime.now();
                address.deletedAt = LocalDateTime.now();
                address.persist();
                addresssAux.add(address);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = address;
                responses.messages.add("Address reactived with sucessful!");
            } else {
                responses.datas = Collections.singletonList(addresssAux);
                responses.messages.add(count + " Address reactived with sucessful!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = address;
                responses.messages.add("Address not located or already reactivated.");
            } else {
                responses.datas = Collections.singletonList(addressses);
                responses.messages.add("Address not located or already reactivated.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
