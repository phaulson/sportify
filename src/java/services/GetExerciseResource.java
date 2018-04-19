/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.Exercise;
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
    public Exercise getExercise(String content) {
        Exercise exercise = null;
        try{
            int exerciseID = new Gson().fromJson(content, Integer.class);
            Manager m = Manager.newInstance();
            exercise = m.getExercise(exerciseID);
        }
        catch(SQLException ex){
            
        }
        catch(Exception ex){
            
        }
        return exercise;
    }
}
