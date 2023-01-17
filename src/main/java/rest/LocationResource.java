package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.LocationDTO;
import dtos.MatchDTO;
import entities.Location;
import errorhandling.API_Exception;
import facades.LocationFacade;
import facades.MatchFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("location")
public class LocationResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final LocationFacade FACADE =  LocationFacade.getLocationFacade(EMF);

//    private static UserResource getUserFacade(EntityManagerFactory emf) {
//        return null;
//    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Response addLocation(String jsonString) throws API_Exception {
        LocationDTO locationDTO;
        try {

            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            String address = json.get("address").getAsString();
            String city = json.get("city").getAsString();
            locationDTO = new LocationDTO(address, city);
            FACADE.createLocation(locationDTO);
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Suplied",400,e);
        }
        return Response.ok().entity(GSON.toJson(locationDTO)).build();
    }
}
