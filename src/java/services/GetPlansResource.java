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
import data.User;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
   public Response getPlans(String content) {
        Collection<Plan> plans = new ArrayList<>();
        Response r;
        try{
            int userid = new Gson().fromJson(content, Integer.class);
            Manager m = Manager.newInstance();
            plans = m.getPlans(userid);
            
            if(plans.size() == 0)
                throw new CustomException("no plans found");

            r = Response.status(Response.Status.OK).entity(new Gson().toJson(new Gson().toJson(plans))).build();
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
