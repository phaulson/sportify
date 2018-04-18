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
    public Collection<Workout> searchWrokouts(String content) {
        Collection<Workout> workouts = new ArrayList<>();
        try{
        handleObjectSearchWorkouts o = new Gson().fromJson(content, handleObjectSearchWorkouts.class);
        Manager m = Manager.newInstance();
        workouts = m.searchWorkouts(o.creatorID, o.name);
        }
        catch(SQLException ex){
            
        }
        catch(Exception ex){
            
        }
        return workouts;
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
