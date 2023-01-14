package rest;

import com.google.gson.*;
import dtos.UserDTO;
import errorhandling.API_Exception;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

@Path("user")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final UserFacade FACADE =  UserFacade.getUserFacade(EMF);

//    private static UserResource getUserFacade(EntityManagerFactory emf) {
//        return null;
//    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    SecurityContext securityContext;

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllUsers() throws EntityNotFoundException {
        return Response.ok().entity(GSON.toJson(FACADE.getAllUsers())).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Response updatePassword(String jsonString) throws API_Exception {
        UserDTO userDTO;
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            String userName = securityContext.getUserPrincipal().getName();
            String userPass = json.get("password").getAsString();
            userDTO = FACADE.updateUserPassword(userName, userPass);
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Suplied",400,e);
        }
        return Response.ok().entity(GSON.toJson(userDTO)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Response addUser(String jsonString) throws API_Exception {
        UserDTO userDTO;
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            String userName = json.get("name").getAsString();
            String userPass = json.get("password").getAsString();
            List<String> roles = new ArrayList<>();
            JsonArray jsonArray = json.get("roles").getAsJsonArray();
            for(JsonElement ja : jsonArray){
                roles.add(ja.getAsString());
            }
            userDTO = new UserDTO(userName, userPass, roles);
            FACADE.createUser(userDTO);
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Suplied",400,e);
        }
        return Response.ok().entity(GSON.toJson(userDTO)).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Response deleteUser(String jsonString) throws API_Exception {
        String userName;
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            userName = json.get("name").getAsString();
            FACADE.deleteUser(userName);
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Suplied",400,e);
        }
        return Response.ok().entity(GSON.toJson(userName)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("verify")
    @RolesAllowed("admin")
    public Response getUser() {
        String username = securityContext.getUserPrincipal().getName();
        UserDTO userDTO = FACADE.findUserFromUsername(username);
        return Response.ok().entity(GSON.toJson(userDTO)).build();
    }
}
