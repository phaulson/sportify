/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import data.DailyWorkout;
import data.Manager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Martin
 */
@Path("getDailyWorkouts")
public class GetDailyWorkoutsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GetDailyWorkoutsResource
     */
    public GetDailyWorkoutsResource() {
    }

    /**
     * Retrieves representation of an instance of services.GetDailyWorkoutsResource
     * @param planID
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{planID}")
    public Collection<DailyWorkout> getDailyWorkouts(@PathParam("planID") int planID) {
        Collection<DailyWorkout> dailyWorkouts = new ArrayList<>();
        try{
            Manager m = Manager.newInstance();
            dailyWorkouts = m.getDailyWorkouts(planID);
        }
        catch(SQLException ex){
            
        }
        catch(Exception ex){
            
        }
        return dailyWorkouts;
    }

 
}
