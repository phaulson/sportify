/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
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
    public int login(String content)throws Exception{
        int userID = -1;        
        try{
        User user = new Gson().fromJson(content, User.class);
        Manager manager = Manager.newInstance();
        userID = manager.login(user.getUsername(), user.getPassword());
        }
        catch(SQLException ex){
            return -200;
        }
        catch(Exception ex){
            return -10;
        }
        return userID;
    }
}
