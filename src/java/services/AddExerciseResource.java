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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Martin
 */
@Path("addExercise")
public class AddExerciseResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AddExerciseResource
     */
    public AddExerciseResource() {
    }

    /**
     * Retrieves representation of an instance of services.AddExerciseResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addExercise(String content) {
        int result;
        Response r;
        try{
            handleObjectAddExercise o = new Gson().fromJson(content, handleObjectAddExercise.class);
            Manager m = Manager.newInstance();
            result = m.addExercise(o.name, o.description, o.creatorID);

            r = Response.status(Response.Status.OK).entity(result).build();
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

class handleObjectAddExercise{
    int creatorID;
    String name;
    String description;

    public handleObjectAddExercise(int creatorID, String name, String description) {
        this.creatorID = creatorID;
        this.name = name;
        this.description = description;
    }

      

    
    
}