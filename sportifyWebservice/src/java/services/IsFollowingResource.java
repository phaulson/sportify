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
    public Response isFollowing(String content) {
        Response r;
        try{
            handleObjectIsFollowing o = new Gson().fromJson(content, handleObjectIsFollowing.class);
            Manager m = Manager.newInstance();
            boolean isFollowing = m.isFollowing(o.followerID, o.followsID);
           
            
            r = Response.status(Response.Status.OK).entity(String.valueOf(isFollowing)).build();
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
class handleObjectIsFollowing{
    int followerID;
    int followsID;

    public handleObjectIsFollowing(int followerID, int followsID) {
        this.followerID = followerID;
        this.followsID = followsID;
    }
    
}
