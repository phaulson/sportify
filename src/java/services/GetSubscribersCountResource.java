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
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Martin
 */
@Path("getSubscribersCount")
public class GetSubscribersCountResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GetSubscribersCountResource
     */
    public GetSubscribersCountResource() {
    }

    public Response getCount(String content){
        Response r;
        try{
            int idPlan = new Gson().fromJson(content, Integer.class);
            Manager m = Manager.newInstance();
            int count = m.getSubscribersCount(idPlan);
            r = Response.status(Response.Status.OK).entity(count).build();
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
