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
@Path("addPlan")
public class AddPlanResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AddPlanResource
     */
    public AddPlanResource() {
    }

    /**
     * Retrieves representation of an instance of services.AddPlanResource
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public int getJson(String content) {
        int planID;
        try{
            Manager m = Manager.newInstance();
            handlerObjectAddPlanResource o = new Gson().fromJson(content, handlerObjectAddPlanResource.class);
            planID = m.addPlan(o.creatorID, o.name);
        }
        catch(SQLException ex){
            return -200;
        }
        catch(Exception ex){
            return -10;
        }
        return planID;
    }

}
class handlerObjectAddPlanResource{
    int creatorID;
    String name;
    public handlerObjectAddPlanResource(int _creatorID, String _name){
        creatorID = _creatorID;
        name = _name;
    }
}
