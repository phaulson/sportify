/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.CustomException;
import data.Manager;
import data.User;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Martin
 */
@Path("login")
public class LoginResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of LoginResource
     */
    public LoginResource() {
    }

    /**
     * Retrieves representation of an instance of services.LoginResource
     * @param content
     * @param username
     * @param password
     * @return an instance of java.lang.String
     * @throws java.lang.Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(String content)throws Exception{
        int userID = -1;
        Response r;
        try{
            User user = new Gson().fromJson(content, User.class);
            Manager manager = Manager.newInstance();
            user = manager.login(user.getUsername(), user.getPassword());
                       
            r = Response.status(Response.Status.OK).entity(user).build();
        }
        catch(SQLException ex){
            try{
            if(ex.getMessage().contains("Erschöpfte Ergebnismenge"))
                throw new CustomException("login failed");
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("sql error occured: " + ex.getMessage()).build();
            }
            catch(CustomException custEx){
                r = Response.status(Response.Status.NOT_FOUND).entity(custEx.getMessage()).build();
            }
        }
        catch(Exception ex){
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("internal server error: " + ex.getMessage()).build();
        }
        return r;                
    }
}
