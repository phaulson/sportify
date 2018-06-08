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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    @Produces(MediaType.APPLICATION_JSON)
    public Response isPlanSubscribed(String content) {
        Response r;
        try{
            handleObjectIsPlanSubscribed o = new Gson().fromJson(content, handleObjectIsPlanSubscribed.class);
            Manager m = Manager.newInstance();
            boolean result = m.isPlanSubscribed(o.userID, o.planID);
           
            
            r = Response.status(Response.Status.OK).entity(String.valueOf(result)).build();
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
class handleObjectIsPlanSubscribed{
    int userID;
    int planID;

    public handleObjectIsPlanSubscribed(int userID, int planID) {
        this.userID = userID;
        this.planID = planID;
    }
    
}
