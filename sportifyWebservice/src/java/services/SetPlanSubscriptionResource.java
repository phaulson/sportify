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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Martin
 */
@Path("setPlanSubscription")
public class SetPlanSubscriptionResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SetPlanSubscriptionResource
     */
    public SetPlanSubscriptionResource() {
    }

    /**
     * Retrieves representation of an instance of services.SetPlanSubscriptionResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPlanSub(String content) {
        Response r;
        try{
            handleObjectsetPlanSubscription o = new Gson().fromJson(content, handleObjectsetPlanSubscription.class);
            Manager m = Manager.newInstance();
            boolean result = m.setPlanSubscription(o.userID, o.planID, o.subscribe);
                       
            r = Response.status(Response.Status.OK).entity(new Gson().toJson(result)).build();
        }
        catch(SQLException ex){
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("sql error occured: " + ex.getMessage()).build();  
            if(ex.getMessage().contains("übergeordneter Schlüssel nicht gefunden"))
                r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("user or plan not valid").build();
            else if(ex.getMessage().contains("Unique Constraint (D4A13.PK_SUBSCRIPTION) verletzt"))
                r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("plan hat bereits diesen state lol").build();                      
        }
        catch(Exception ex){
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("internal server error: " + ex.getMessage()).build();
        }
        return r;        
    }
}
class handleObjectsetPlanSubscription{
    int userID;
    int planID;
    boolean subscribe;

    public handleObjectsetPlanSubscription(int userID, int planID, boolean subscribe) {
        this.userID = userID;
        this.planID = planID;
        this.subscribe = subscribe;
    }
    
}
