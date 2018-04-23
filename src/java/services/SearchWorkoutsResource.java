/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.DailyWorkout;
import data.Manager;
import data.Workout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Martin
 */
@Path("searchWorkouts")
public class SearchWorkoutsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SearchWorkoutsResource
     */
    public SearchWorkoutsResource() {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchWorkouts(String content) {
        Collection<Workout> workouts = new ArrayList<>();
        Response r;
        try{
            handleObjectSearchWorkouts o = new Gson().fromJson(content, handleObjectSearchWorkouts.class);
            Manager m = Manager.newInstance();
            workouts = m.searchWorkouts(o.creatorID, o.name);
                       
            r = Response.status(Response.Status.OK).entity(new Gson().toJson(workouts)).build();
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
class handleObjectSearchWorkouts{   
    int creatorID;
    String name;
    
    public handleObjectSearchWorkouts(int creatorID, String name) {
        this.creatorID = creatorID;
        this.name = name;
    }
}
