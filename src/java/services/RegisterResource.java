/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
    public int register(String content)throws Exception{
        int userID;
        try{
        handlerObjectRegister o = new Gson().fromJson(content, handlerObjectRegister.class);
        Manager m = Manager.newInstance();
        userID = m.register(o.username, o.password, o.isPro);       
        }
        catch(SQLException ex){
            return -200;
        }
        catch(JsonSyntaxException ex){
            return -100;
        }
        catch(Exception ex){
            return -10;
        }
        return userID;
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
   


