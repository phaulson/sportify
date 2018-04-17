/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.Manager;
import data.User;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
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
@Path("getProfile")
public class GetProfileResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GetProfileResource
     */
    public GetProfileResource() {
    }

    /**
     * Retrieves representation of an instance of services.GetProfileResource
     * @param id
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{userId}")
    public User getJson(@PathParam("userId") String id) {
        User user = null;
        try{
            Manager m = Manager.newInstance();
            user = m.getProfile(Integer.parseInt(id));
        }
        catch(SQLException ex){
           
        }
        catch(Exception ex){
            
        }
        if(user == null)
        return user;
        
        return user;
    }
}
 

 


