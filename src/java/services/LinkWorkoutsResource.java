/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.Manager;
import java.sql.SQLException;
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
@Path("linkWorkouts")
public class LinkWorkoutsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of LinkWorkoutsResource
     */
    public LinkWorkoutsResource() {
    }

    /**
     * Retrieves representation of an instance of services.LinkWorkoutsResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean linkWorkouts(String content) {
        boolean result;
        try{
            handleObjectLinkWorkouts o = new Gson().fromJson(content, handleObjectLinkWorkouts.class);
            Manager m = Manager.newInstance();
            result = m.linkWorkouts(o.dailyWorkoutID, o.workouts);
        }
        catch(SQLException ex){
            return false;
        }
        catch(Exception ex){
            return false;
        }
        return result;
    }
}
class handleObjectLinkWorkouts{
    int dailyWorkoutID;
    Collection<Integer> workouts;

    public handleObjectLinkWorkouts(int _dailyWorkoutID, Collection<Integer> _workouts) {
        this.dailyWorkoutID = _dailyWorkoutID;
        this.workouts = _workouts;
    }
    
    
}
