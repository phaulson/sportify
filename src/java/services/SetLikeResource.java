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
@Path("setLike")
public class SetLikeResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SetLikeResource
     */
    public SetLikeResource() {
    }

    /**
     * Retrieves representation of an instance of services.SetLikeResource
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean setLikes(String content) {
        try{
            handleObjectSetLikes o = new Gson().fromJson(content, handleObjectSetLikes.class);
            Manager m = Manager.newInstance();
            m.setLike(o.userID, o.postID, o.likes);
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
class handleObjectSetLikes{
    int userID;
    int postID;
    boolean likes;

    public handleObjectSetLikes(int userID, int postID, boolean likes) {
        this.userID = userID;
        this.postID = postID;
        this.likes = likes;
    }
            
}
