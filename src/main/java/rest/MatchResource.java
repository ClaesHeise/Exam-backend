package rest;

import com.google.gson.*;
import dtos.MatchDTO;
import dtos.UserDTO;
import errorhandling.API_Exception;
import facades.MatchFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

@Path("match")
public class MatchResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final MatchFacade FACADE =  MatchFacade.getMatchFacade(EMF);

//    private static UserResource getUserFacade(EntityManagerFactory emf) {
//        return null;
//    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    SecurityContext securityContext;

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMatchesPlayer() throws API_Exception {
        List<MatchDTO> matches;
        try {
            matches = FACADE.getAllMatches();
        } catch (Exception exception) {
            throw new API_Exception("Something went wrong", 500, exception);
        }
        return Response.ok().entity(GSON.toJson(matches)).build();
    }

    @GET
    @Path("all/player/{playername}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("player")
    public Response getAllMatchesPlayer(@PathParam("playername") String playerName) throws API_Exception {
        List<MatchDTO> matches;
        try {
            matches = FACADE.getAllMatchesForPlayer(playerName);
        } catch (Exception exception) {
            throw new API_Exception("Something went wrong", 500, exception);
        }
        return Response.ok().entity(GSON.toJson(matches)).build();
    }

    @GET
    @Path("all/location/{locationname}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMatchesLocation(@PathParam("locationname") String locationName) throws API_Exception {
        List<MatchDTO> matches;
        try {
            matches = FACADE.getAllMatchesForLocation(locationName);
        } catch (Exception exception) {
            throw new API_Exception("Something went wrong", 500, exception);
        }
        return Response.ok().entity(GSON.toJson(matches)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Response addMatch(String jsonString) throws API_Exception {
        MatchDTO matchDTO;
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            String opponentTeam = json.get("opponentTeam").getAsString();
            String judge = json.get("judge").getAsString();
            String type = json.get("type").getAsString();
            Boolean inDoors = json.get("inDoors").getAsBoolean();
            matchDTO = new MatchDTO(opponentTeam, judge, type, inDoors, null, null);
            FACADE.createMatch(matchDTO);
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Suplied",400,e);
        }
        return Response.ok().entity(GSON.toJson(matchDTO)).build();
    }
}
