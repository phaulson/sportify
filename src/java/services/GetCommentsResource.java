/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.Comment;
import data.Manager;
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
@Path("getComments")
public class GetCommentsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GetCommentsResource
     */
    public GetCommentsResource() {
    }

    /**
     * PUT method for updating or creating an instance of GetCommentsResource
     * @param content representation for the resource
     * @return 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Comment> getComments(String content) {
        Collection<Comment> comments = new ArrayList<>();
        try{
            handleObjectGetComments o = new Gson().fromJson(content, handleObjectGetComments.class);
            Manager m = Manager.newInstance();
            comments = m.getComments(o.postID, o.lastCommentID, o.numberOfComments);
        }
        catch(SQLException ex){
            
        }
        catch(Exception ex){
            
        }
        return comments;
    }
}
class handleObjectGetComments{
    int postID;
    int lastCommentID;
    int numberOfComments;

    public handleObjectGetComments(int postID, int lastCommentID, int numberOfComments) {
        this.postID = postID;
        this.lastCommentID = lastCommentID;
        this.numberOfComments = numberOfComments;
    }
}
