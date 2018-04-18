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
    public Collection<User> searchUsers(String content) {
        Collection<User> users = new ArrayList<>();
        try{
            handleObjectSearchUsers o = new Gson().fromJson(content, handleObjectSearchUsers.class);
            Manager m = Manager.newInstance();
            users = m.searchUsers(o.name, o.isPro);
        }
        catch(SQLException ex){
            
        }
        catch(Exception ex){
            
        }
    return users;
    }
}
class handleObjectSearchUsers{
    String name;
    boolean isPro;

    public handleObjectSearchUsers(String name, boolean isPro) {
        this.name = name;
        this.isPro = isPro;
    }
    
}
