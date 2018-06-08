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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)

    public Response getNumberOfLikes(String content) {
        int numberOfLikes;
        Response r;
        try{
            int postID = new Gson().fromJson(content, Integer.class);
            Manager m = Manager.newInstance();
            numberOfLikes = m.getNumberOfLikes(postID);

            r = Response.status(Response.Status.OK).entity(numberOfLikes).build();
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
