/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.Exercise;
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
@Path("searchExercises")
public class SearchExercisesResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SearchExercisesResource
     */
    public SearchExercisesResource() {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchWorkouts(String content) {
        Collection<Exercise> exercises;
        Response r;
        try{
            handleObjectSearchExercises o = new Gson().fromJson(content, handleObjectSearchExercises.class);
            Manager m = Manager.newInstance();
            exercises = m.searchExercises(o.creatorID, o.name, o.lastExerciseId, o.numberOfExercises); 
                       
            r = Response.status(Response.Status.OK).entity(new Gson().toJson(exercises)).build();
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
class handleObjectSearchExercises{   
    int creatorID;
    String name;
    int lastExerciseId;
    int numberOfExercises;
    
    public handleObjectSearchExercises(int creatorID, String name, int lastExerciseId, int numberOfExercises) {
        this.creatorID = creatorID;
        this.name = name;
        this.lastExerciseId = lastExerciseId;
        this.numberOfExercises = numberOfExercises;
    }
}