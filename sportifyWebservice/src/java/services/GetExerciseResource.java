/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.CustomException;
import data.Exercise;
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
@Path("getExercise")
public class GetExerciseResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GetExerciseResource
     */
    public GetExerciseResource() {
    }

    /**
     * Retrieves representation of an instance of services.GetExerciseResource
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getExercise(String content) {
        Exercise exercise;
        Response r;
        try{
            int exerciseID = new Gson().fromJson(content, Integer.class);
            Manager m = Manager.newInstance();
            exercise = m.getExercise(exerciseID);

            r = Response.status(Response.Status.OK).entity(exercise).build();
        }
        catch(SQLException ex){
            try{
            if(ex.getMessage().equals("Erschöpfte Ergebnismenge"))
                throw new CustomException("No Excercise with this id");
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("sql error occured: " + ex.getMessage()).build();  
            }
            catch(CustomException custEx){
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(custEx.getMessage()).build();
        }
        }
        catch(Exception ex){
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("internal server error: " + ex.getMessage()).build();
        }
        return r;        
    }
}
