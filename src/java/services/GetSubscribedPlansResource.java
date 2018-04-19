/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.Manager;
import data.Plan;
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
@Path("getSubscribedPlans")
public class GetSubscribedPlansResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GetSubscribedPlansResource
     */
    public GetSubscribedPlansResource() {
    }

    /**
     * Retrieves representation of an instance of services.GetSubscribedPlansResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Plan> getJson(String content) {
        Collection<Plan> plans = new ArrayList<>();
        try{
            int userID = new Gson().fromJson(content, Integer.class);
            Manager m = Manager.newInstance();
            m.getSubscribedPlans(userID);
        }
        catch(SQLException ex){
            
        }
        catch(Exception ex){
            
        }
        return plans;
    }
}
