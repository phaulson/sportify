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
@Path("isFollowing")
public class IsFollowingResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of IsFollowingResource
     */
    public IsFollowingResource() {
    }

    /**
     * Retrieves representation of an instance of services.IsFollowingResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isFollowing(String content) {
       try{
           handleObjectIsFollowing o = new Gson().fromJson(content, handleObjectIsFollowing.class);
           Manager m = Manager.newInstance();
           m.isFollowing(o.followerID, o.followsID);
       }
       catch(SQLException ex){
        
        }
       catch(Exception ex){
           
       }
       return true;
    }
}
class handleObjectIsFollowing{
    int followerID;
    int followsID;

    public handleObjectIsFollowing(int followerID, int followsID) {
        this.followerID = followerID;
        this.followsID = followsID;
    }
    
}
