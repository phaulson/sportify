/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.Manager;
import java.sql.SQLException;
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
    public int addExercise(String content) {
        int result = -1;
        try{
            handleObjectAddExercise o = new Gson().fromJson(content, handleObjectAddExercise.class);
            Manager m = Manager.newInstance();
            result = m.addExercise(o.name, o.description, o.creatorID);
        }
        catch(SQLException ex){
            return -200;
        }
        catch(Exception ex){
            return -10;
        }
        return  result;
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