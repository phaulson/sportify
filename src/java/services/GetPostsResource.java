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
    public Collection<Post> getPosts(String content) {
        Collection<Post> posts = new ArrayList<>();
        try{
            handlerObjectGetPosts o = new Gson().fromJson(content, handlerObjectGetPosts.class);
            Manager m = Manager.newInstance();
            posts = m.getPosts(o.userID, o.postID, o.numberOfPosts);
        }
        catch(SQLException ex){
            
        }
        catch(Exception ex){
            
        }
        return posts;
    }
}
 class handlerObjectGetPosts{
     int userID;
     int postID;
     int numberOfPosts;
    public handlerObjectGetPosts(int userID, int postID, int numberOfPosts) {
        this.userID = userID;
        this.postID = postID;
        this.numberOfPosts = numberOfPosts;
    }
     
 }