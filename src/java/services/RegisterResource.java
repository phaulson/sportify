/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import data.CustomException;
import data.Manager;
import data.User;
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
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Martin
 */
@Path("register")
public class RegisterResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RegisterResource
     */
    public RegisterResource() {
    }

    /**
     * Retrieves representation of an instance of services.RegisterResource
     * @param content
     * @return an instance of java.lang.String
     * @throws java.lang.Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(String content)throws Exception{
        User user;
        Response r;
        try{
            handlerObjectRegister o = new Gson().fromJson(content, handlerObjectRegister.class);
            Manager m = Manager.newInstance();
            user = m.register(o.username, o.password, o.isPro); 
                       
            r = Response.status(Response.Status.OK).entity(user).build();
        }
        catch(SQLException ex){
            try{
            if(ex.getMessage().contains("Unique Constraint (D4A13.SYS_C0055735) verletzt"))
                throw new CustomException("register failed");
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
    class handlerObjectRegister{
        String username;
        String password;
        boolean isPro;
        
        public handlerObjectRegister(String _name, String _password, boolean _isPro) {
            username = _name;
            password = _password;
            isPro = _isPro;
        }
    }
   


