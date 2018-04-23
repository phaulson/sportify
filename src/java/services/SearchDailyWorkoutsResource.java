/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.CustomException;
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
    public Response getJson(String content) {
        Collection<DailyWorkout> dailyWorkouts = new ArrayList<>();
        Response r;
        try{
            handleObjectSearchDailyWorkouts o = new Gson().fromJson(content, handleObjectSearchDailyWorkouts.class);
            Manager m = Manager.newInstance();
            dailyWorkouts = m.searchDailyWorkouts(o.creatorID, o.name); 
                       
            r = Response.status(Response.Status.OK).entity(new Gson().toJson(dailyWorkouts)).build();
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
class handleObjectSearchDailyWorkouts{   
    int creatorID;
    String name;
    
    public handleObjectSearchDailyWorkouts(int creatorID, String name) {
        this.creatorID = creatorID;
        this.name = name;
    }
}
