/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.Exercise;
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
    public Collection<Exercise> searchWrokouts(String content) {
        Collection<Exercise> exercises = new ArrayList<>();
        try{
        handleObjectSearchExercises o = new Gson().fromJson(content, handleObjectSearchExercises.class);
        Manager m = Manager.newInstance();
        exercises = m.searchExercises(o.creatorID, o.name);
        }
        catch(SQLException ex){
            
        }
        catch(Exception ex){
            
        }
        return exercises;
    }
}
class handleObjectSearchExercises{   
    int creatorID;
    String name;
    
    public handleObjectSearchExercises(int creatorID, String name) {
        this.creatorID = creatorID;
        this.name = name;
    }
}