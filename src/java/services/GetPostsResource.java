/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.Manager;
import data.Post;
import java.sql.SQLException;
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
@Path("getPosts")
public class GetPostsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GetPostsResource
     */
    public GetPostsResource() {
    }

    /**
     * Retrieves representation of an instance of services.GetPostsResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosts(String content) {
        Collection<Post> posts;
        Response r;
        try{
            handlerObjectGetPosts o = new Gson().fromJson(content, handlerObjectGetPosts.class);
            Manager m = Manager.newInstance();
            posts = m.getPosts(o.userID, o.lastPostID, o.numberOfPosts);

            r = Response.status(Response.Status.OK).entity(new Gson().toJson(posts)).build();
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
 class handlerObjectGetPosts{
     int userID;
     int lastPostID;
     int numberOfPosts;
    public handlerObjectGetPosts(int userID, int postID, int numberOfPosts) {
        this.userID = userID;
        this.lastPostID = postID;
        this.numberOfPosts = numberOfPosts;
    }
     
 }