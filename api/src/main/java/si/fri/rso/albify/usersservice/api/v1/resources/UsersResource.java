package si.fri.rso.albify.usersservice.api.v1.resources;

import si.fri.rso.albify.usersservice.lib.AuthResponse;
import si.fri.rso.albify.usersservice.lib.GoogleToken;
import si.fri.rso.albify.usersservice.lib.JWTService;
import si.fri.rso.albify.usersservice.models.converters.UserConverter;
import si.fri.rso.albify.usersservice.models.entities.UserEntity;
import si.fri.rso.albify.usersservice.services.beans.UsersBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    private Logger log = Logger.getLogger(UsersResource.class.getName());

    @Inject
    private UsersBean usersBean;

    @Context
    protected UriInfo uriInfo;

    @POST
    @Path("/login")
    public Response loginUser(GoogleToken googleToken) {
        if (googleToken == null || googleToken.getToken() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        UserEntity entity = usersBean.authenticateUser(googleToken.getToken());
        if (entity == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = JWTService.generateJWT(entity.getId().toString());
        if (token == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.status(Response.Status.OK).entity(new AuthResponse(UserConverter.toDto(entity), token)).build();
    }
}
