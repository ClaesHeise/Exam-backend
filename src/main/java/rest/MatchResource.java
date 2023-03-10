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

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    SecurityContext securityContext;

    // User story 1 - get all matches
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMatches() throws API_Exception {
        List<MatchDTO> matches;
        try {
            matches = FACADE.getAllMatches();
        } catch (Exception exception) {
            throw new API_Exception("Something went wrong", 500, exception);
        }
        return Response.ok().entity(GSON.toJson(matches)).build();
    }

    // User story 2 - get all matches, as a player
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

    // User story 3 - get all matches, on a specific location
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @RolesAllowed("admin")
    public Response getMatch(@PathParam("id") Long id) {
        MatchDTO matchDTO = FACADE.findMatchFromId(id);
        return Response.ok().entity(GSON.toJson(matchDTO)).build();
    }

    // User story 4 - add new: Match, User & Location
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

    // User story 5 & 6 - Change match / Aswell as add relations between match and User + Location
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Response updateMatch(String jsonString) throws API_Exception {
        MatchDTO matchDTO;
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            Long id = json.get("id").getAsLong();
            String opponentTeam = json.get("opponentTeam").getAsString();
            String judge = json.get("judge").getAsString();
            String type = json.get("type").getAsString();
            Boolean inDoors = json.get("inDoors").getAsBoolean();
            List<Long> userIds = new ArrayList<>();
            JsonArray jsonArray = json.get("users").getAsJsonArray();
            for(JsonElement ja : jsonArray){
                userIds.add(ja.getAsLong());
            }
            Long localtionId = json.get("location").getAsLong();
            matchDTO = FACADE.updateMatch(new MatchDTO(id, opponentTeam, judge, type, inDoors, userIds, localtionId));
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Suplied",400,e);
        }
        return Response.ok().entity(GSON.toJson(matchDTO)).build();
    }
}
