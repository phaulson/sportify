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
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Martin
 */
@Path("changeDescription")
public class ChangeDescriptionResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ChangeDescriptionResource
     */
    public ChangeDescriptionResource() {
    }

    /**
     * PUT method for updating or creating an instance of ChangeDescriptionResource
     * @param content representation for the resource
     * @return 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeDescription(String content) {
        boolean result = false;
        Response r;
        try{
            handlerObjectChangeDescription o = new Gson().fromJson(content, handlerObjectChangeDescription.class);
            Manager m = Manager.newInstance();
            result = m.changeDescription(o.userID, o.description);
            String resultString = String.valueOf(result);
            r = Response.status(Response.Status.OK).entity(resultString).build();
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

class handlerObjectChangeDescription{
    int userID;
    String description;
    public handlerObjectChangeDescription(int _userId, String _Description){
        userID = _userId;
        description = _Description;      
    }
}
