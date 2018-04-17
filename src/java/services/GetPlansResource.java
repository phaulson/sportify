/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.Manager;
import data.Plan;
import data.User;
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
@Path("getPlans")
public class GetPlansResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GetPlansResource
     */
    public GetPlansResource() {
    }

    /**
     * Retrieves representation of an instance of services.GetPlansResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{userID}")
   public Collection<Plan> getPlans(@PathParam("userID") String id) {
        Collection<Plan> plans = new ArrayList<>();
        try{
            int Userid = new Gson().fromJson(id, int.class);
            Manager m = Manager.newInstance();
            plans = m.getPlans(Userid);
        }
        catch(SQLException ex){
            
        }
        catch(Exception ex){
            
        }
        return plans;
    }

  
}
