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
@Path("isLiked")
public class IsLikedResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of IsLikedResource
     */
    public IsLikedResource() {
    }

    /**
     * Retrieves representation of an instance of services.IsLikedResource
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson(String content) {
        boolean result;
        Response r;
        try{
            handleObjectIsLiked o = new Gson().fromJson(content, handleObjectIsLiked.class);
            Manager m = Manager.newInstance();
            result = m.isLiked(o.userID, o.postID);
           
            
            r = Response.status(Response.Status.OK).entity(String.valueOf(result)).build();
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
class handleObjectIsLiked{
    int userID;
    int postID;

    public handleObjectIsLiked(int userID, int postID) {
        this.userID = userID;
        this.postID = postID;
    }
    
}