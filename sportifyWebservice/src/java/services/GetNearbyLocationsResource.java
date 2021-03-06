/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.Coordinate;
import data.CustomException;
import data.Location;
import data.LocationType;
import data.Manager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Martin
 */
@Path("getNearbyLocations")
public class GetNearbyLocationsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GetNearbyLocationsResource
     */
    public GetNearbyLocationsResource() {
    }

    /**
     * Retrieves representation of an instance of services.GetNearbyLocationsResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNearbyLocations(String content) {
        Collection<Location> locations;
        Response r;
        try{
            handleObjectGetNearbyLocations o = new Gson().fromJson(content, handleObjectGetNearbyLocations.class);
            Manager m = Manager.newInstance();
            locations = m.getNearbyLocations(o.coordinates, o.radius, o.types);
            r = Response.status(Response.Status.OK).entity(new Gson().toJson(new Gson().toJson(locations))).build();
        }
        catch(SQLException ex){
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("sql error occured: " + ex.getMessage()).build();
        }
        catch(Exception ex){
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("internal server error: " + ex.getMessage()).build();
        }
        return r;        
    }
}
class handleObjectGetNearbyLocations{
    Coordinate coordinates;
    double radius;
    ArrayList<LocationType> types;

    public handleObjectGetNearbyLocations(Coordinate coordinates, double radius, ArrayList<LocationType> types) {
        this.coordinates = coordinates;
        this.radius = radius;
        this.types = types;
    }
    
}
