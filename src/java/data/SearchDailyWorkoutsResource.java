/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import com.google.gson.Gson;
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
@Path("searchDailyWorkouts")
public class SearchDailyWorkoutsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SearchDailyWorkoutsResource
     */
    public SearchDailyWorkoutsResource() {
    }

    /**
     * Retrieves representation of an instance of data.SearchDailyWorkoutsResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Collection<DailyWorkout> getJson(String content) {
        Collection<DailyWorkout> dailyWorkouts = new ArrayList<>();
        try{
        handleObjectSearchDailyWorkouts o = new Gson().fromJson(content, handleObjectSearchDailyWorkouts.class);
        Manager m = Manager.newInstance();
        dailyWorkouts = m.searchDailyWorkouts(o.creatorID, o.name);
        }
        catch(SQLException ex){
            
        }
        catch(Exception ex){
            
        }
        return dailyWorkouts;
    }
}
class handleObjectSearchDailyWorkouts{   
    int creatorID;
    String name;
    
    public handleObjectSearchDailyWorkouts(int creatorID, String name) {
        this.creatorID = creatorID;
        this.name = name;
    }
}
