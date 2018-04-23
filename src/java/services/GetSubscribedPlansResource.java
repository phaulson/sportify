/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.CustomException;
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
import javax.ws.rs.core.Response;

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
    public Response getJson(String content) {
        Collection<Plan> plans = new ArrayList<>();
        Response r;
        try{
            int userID = new Gson().fromJson(content, Integer.class);
            Manager m = Manager.newInstance();
            plans = m.getSubscribedPlans(userID);

            if(plans.isEmpty())
                throw new CustomException("no plans found");
            
            r = Response.status(Response.Status.OK).entity(new Gson().toJson(plans)).build();
        }
        catch(CustomException ex){
            r = Response.status(Response.Status.NO_CONTENT).entity(ex.getMessage()).build();
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
