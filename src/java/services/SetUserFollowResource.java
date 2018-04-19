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
@Path("setUserFollow")
public class SetUserFollowResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SetUserFollowResource
     */
    public SetUserFollowResource() {
    }

    /**
     * Retrieves representation of an instance of services.SetUserFollowResource
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean setLike(String content) {
        try{
            handleObjetSetUserFollow o = new Gson().fromJson(content, handleObjetSetUserFollow.class);
            Manager m = Manager.newInstance();
            m.setUserFollow(o.followerID,o.followsID,o.follow);
        }
        catch(SQLException ex){
            return false;
        }
        catch(Exception ex){
            return false;
        }
        return true;
    }
}
class handleObjetSetUserFollow
{
    int followerID;
    int followsID;
    boolean follow;

    public handleObjetSetUserFollow(int followerID, int followsID, boolean follow) {
        this.followerID = followerID;
        this.followsID = followsID;
        this.follow = follow;
    }
    
}
