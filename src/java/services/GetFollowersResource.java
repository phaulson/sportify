/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.CustomException;
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
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Martin
 */
@Path("getFollowers")
public class GetFollowersResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GetFollowersResource
     */
    public GetFollowersResource() {
    }

    /**
     * Retrieves representation of an instance of services.GetFollowersResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollowers(String content) {
        Collection<User> users = new ArrayList<>();
        Response r;
        try{
            handleObjectGetFollowers o = new Gson().fromJson(content, handleObjectGetFollowers.class);
            Manager m = Manager.newInstance();
            users = m.getFollowers(o.userId, o.lastFollowerId, o.numberOfFollower);
            if(users.size() == 0)
                throw new CustomException("no follower found");

            r = Response.status(Response.Status.OK).entity(new Gson().toJson(new Gson().toJson(users))).build();
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
class handleObjectGetFollowers{
    int userId;
    int lastFollowerId;
    int numberOfFollower;

    public handleObjectGetFollowers(int userId, int lastFollowerId, int numberOfFollower) {
        this.userId = userId;
        this.lastFollowerId = lastFollowerId;
        this.numberOfFollower = numberOfFollower;
    }
    
}