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
@Path("searchUsers")
public class SearchUsersResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SearchUsersResource
     */
    public SearchUsersResource() {
    }

    /**
     * Retrieves representation of an instance of services.SearchUsersResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUsers(String content) {
        Collection<User> users;
        Response r;
        try{
            handleObjectSearchUsers o = new Gson().fromJson(content, handleObjectSearchUsers.class);
            Manager m = Manager.newInstance();
            users = m.searchUsers(o.name, o.isPro, o.lastUserId, o.numberOfUsers);
                       
            r = Response.status(Response.Status.OK).entity(new Gson().toJson(users)).build();
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
class handleObjectSearchUsers{
    String name;
    boolean isPro;
    int lastUserId;
    int numberOfUsers;

    public handleObjectSearchUsers(String name, boolean isPro, int lastUserId, int numberOfUsers) {
        this.name = name;
        this.isPro = isPro;
        this.lastUserId = lastUserId;
        this.numberOfUsers = numberOfUsers;
    }
    
}
