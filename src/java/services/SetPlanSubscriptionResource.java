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
import javax.ws.rs.core.MediaType;

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
    public boolean setPlanSub(String content) {
        try{
            handleObjectsetPlanSubscription o = new Gson().fromJson(content, handleObjectsetPlanSubscription.class);
            Manager m = Manager.newInstance();
            m.setPlanSubscription(o.userID, o.planID, o.subscribe);
        }
        catch(SQLException ex){
            
        }
        catch(Exception ex){
            
        }
        return true;
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
