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
@Path("addDailyWorkout")
public class AddDailyWorkoutResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AddDailyWorkoutResource
     */
    public AddDailyWorkoutResource() {
    }

    /**
     * Retrieves representation of an instance of services.AddDailyWorkoutResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public int addDailyWorkout(String content) {
        int dailyWorkoutID;
        try{
            handleObjectAddDailyWorkout o = new Gson().fromJson(content, handleObjectAddDailyWorkout.class);
            Manager m = Manager.newInstance();
            dailyWorkoutID = m.addDailyWorkout(o.creatorID, o.name);
        }
        catch(SQLException ex){
            return -200;
        }
        catch(Exception ex){
            return -10;
        }
        return dailyWorkoutID;
    }
}
class handleObjectAddDailyWorkout{
    int creatorID;
    String name;

    public handleObjectAddDailyWorkout(int creatorID, String name) {
        this.creatorID = creatorID;
        this.name = name;
    }
    
    
}
