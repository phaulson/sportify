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
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

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
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean changeDescription(String content) {
        boolean result = false;
        try{
        handlerObjectChangeDescription o = new Gson().fromJson(content, handlerObjectChangeDescription.class);
        Manager m = Manager.newInstance();
        result = m.changeDescription(o.userID, o.Description);
        }
        catch(SQLException ex){
            return false;
        }
        catch(Exception ex){
            return false;
        }
        return result;
    }
}

class handlerObjectChangeDescription{
    int userID;
    String Description;
    public handlerObjectChangeDescription(int _userId, String _Description){
        userID = _userId;
        Description = _Description;      
    }
}
