/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.Manager;
import java.sql.SQLException;
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
@Path("addWorkout")
public class AddWorkoutResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AddWorkoutResource
     */
    public AddWorkoutResource() {
    }

    /**
     * Retrieves representation of an instance of services.AddWorkoutResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addWorkout(String content) {
        int workoutID;
        Response r;
        try{
            handleObjectAddWorkout o = new Gson().fromJson(content, handleObjectAddWorkout.class);
            Manager m = Manager.newInstance();
            workoutID = m.addWorkout(o.creatorID, o.name);

            r = Response.status(Response.Status.OK).entity(workoutID).build();
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
class handleObjectAddWorkout{
    int creatorID;
    String name;

    public handleObjectAddWorkout(int _creatorID, String _name) {
        this.creatorID = _creatorID;
        this.name = _name;
    }
    
    
}
