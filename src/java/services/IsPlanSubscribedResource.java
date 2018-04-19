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
@Path("isPlanSubscribed")
public class IsPlanSubscribedResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of IsPlanSubscribedResource
     */
    public IsPlanSubscribedResource() {
    }

    /**
     * Retrieves representation of an instance of services.IsPlanSubscribedResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean isPlanSubscribed(String content) {
         try{
            handleObjectIsPlanSubscribed o = new Gson().fromJson(content, handleObjectIsPlanSubscribed.class);
            Manager m = Manager.newInstance();
            m.isPlanSubscribed(o.userID, o.planID);
        }
        catch(SQLException ex){
            return false;
        }
        catch(Exception ex){
         return false;   
        }
         return true;
    }
}
class handleObjectIsPlanSubscribed{
    int userID;
    int planID;

    public handleObjectIsPlanSubscribed(int userID, int planID) {
        this.userID = userID;
        this.planID = planID;
    }
    
}
