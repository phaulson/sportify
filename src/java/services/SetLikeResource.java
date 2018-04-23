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
import javax.ws.rs.core.Response;

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
    public Response setLikes(String content) {
        Response r;
        try{
            handleObjectSetLikes o = new Gson().fromJson(content, handleObjectSetLikes.class);
            Manager m = Manager.newInstance();
            boolean result = m.setLike(o.userID, o.postID, o.likes);
                       
            r = Response.status(Response.Status.OK).entity(new Gson().toJson(result)).build();
        }
        catch(SQLException ex){
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("sql error occured: " + ex.getMessage()).build();
            if(ex.getMessage().contains("übergeordneter Schlüssel nicht gefunden"))
                r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("user or post not valid").build();
            else if(ex.getMessage().contains("Unique Constraint (D4A13.PK_LIKE) verletzt"))
                r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("post hat bereits diesen state lol").build();                        
        }
        catch(Exception ex){
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("internal server error: " + ex.getMessage()).build();
        }
        return r;         
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
