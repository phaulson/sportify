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
@Path("getNumberOfLikes")
public class GetNumberOfLikesResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GetNumberOfLikesResource
     */
    public GetNumberOfLikesResource() {
    }

    /**
     * Retrieves representation of an instance of services.GetNumberOfLikesResource
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public int getNumberOfLikes(String content) {
        int numberOfLikes = 0;
        try{
            int postID = new Gson().fromJson(content, Integer.class);
            Manager m = Manager.newInstance();
            numberOfLikes = m.getNumberOfLikes(postID);
        }
        catch(SQLException ex){
            
        }
        catch(Exception ex){
            
        }
        return numberOfLikes;
    }
}
