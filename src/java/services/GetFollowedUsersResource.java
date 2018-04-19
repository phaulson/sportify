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
import javax.ws.rs.core.MediaType;

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
    public Collection<User> getFollowedUsers(String content) {
        Collection<User> users = new ArrayList<>();
        try{
            int userID = new Gson().fromJson(content, Integer.class);
            Manager m = Manager.newInstance();
            users = m.getFollowedUsers(userID);
        }
        catch(SQLException ex){
            
        }
        catch(Exception ex){
            
        }
        return users;
    }
}
