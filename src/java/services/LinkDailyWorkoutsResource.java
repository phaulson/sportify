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
@Path("linkDailyWorkouts")
public class LinkDailyWorkoutsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of LinkDailyWorkoutsResource
     */
    public LinkDailyWorkoutsResource() {
    }

    /**
     * Retrieves representation of an instance of services.LinkDailyWorkoutsResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean linkDailyWorkouts(String content) {
        boolean result = false;
        try{
            handlerObjectLinkDailyWorkouts o = new Gson().fromJson(content, handlerObjectLinkDailyWorkouts.class);
            Manager m = Manager.newInstance();
            result = m.linkDailyWorkouts(o.planID, o.dailyWorkouts);
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
class handlerObjectLinkDailyWorkouts{
    int planID;
    Collection<Integer> dailyWorkouts;
    public handlerObjectLinkDailyWorkouts(int planID, Collection<Integer> dailyWorkouts) {
        this.planID = planID;
        this.dailyWorkouts = dailyWorkouts;
    }
}