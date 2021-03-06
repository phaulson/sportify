/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.CustomException;
import data.Manager;
import java.sql.SQLException;
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
@Path("linkExercises")
public class LinkExercisesResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of LinkExercisesResource
     */
    public LinkExercisesResource() {
    }

    /**
     * Retrieves representation of an instance of services.LinkExercisesResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response linkExercises(String content) {
        boolean result;
        Response r;
        try{
            handleObjectLinkExercisest o = new Gson().fromJson(content, handleObjectLinkExercisest.class);
            Manager m = Manager.newInstance();
            result = m.linkExercises(o.workoutID, o.exercises);
                       
            r = Response.status(Response.Status.OK).entity(String.valueOf(result)).build();
        }
        catch(SQLException ex){
            try{
            if(ex.getMessage().contains("übergeordneter Schlüssel nicht gefunden"))
                throw new CustomException("exercise or workout not valid");
            else if(ex.getMessage().contains("Unique Constraint (D4A13.PK_CONTAINSPD) verletzt"))
                throw new CustomException("exercise already linked with this workout");
            
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("sql error occured: " + ex.getMessage()).build();
            }
            catch(CustomException custEx){
            r = Response.status(Response.Status.NOT_FOUND).entity(custEx.getMessage()).build();
        }
        }
        catch(Exception ex){
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("internal server error: " + ex.getMessage()).build();
        }
        return r;        
}
}
class handleObjectLinkExercisest{
    int workoutID;
    Collection<Integer> exercises;

        public handleObjectLinkExercisest(int workoutID, Collection<Integer> exercises) {
            this.workoutID = workoutID;
            this.exercises = exercises;
        }

    
    
}