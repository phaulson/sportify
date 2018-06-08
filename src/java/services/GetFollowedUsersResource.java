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
import java.util.ArrayList;
import java.util.Collection;
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
@Path("getFollowedUsers")
public class GetFollowedUsersResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GetFollowedUsersResource
     */
    public GetFollowedUsersResource() {
    }

    /**
     * Retrieves representation of an instance of services.GetFollowedUsersResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollowedUsers(String content) {
        Collection<User> users;
        Response r;
        try{
            int userID = new Gson().fromJson(content, Integer.class);
            Manager m = Manager.newInstance();
            users = m.getFollowedUsers(userID);
            r = Response.status(Response.Status.OK).entity(new Gson().toJson(new Gson().toJson(users))).build();
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
