/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.CustomException;
import data.Manager;
import data.ProUser;
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
     * @return an instance of java.lang.String
     * @throws java.lang.Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String content)throws Exception{
        Response r;
        User user;
        try{
            handleObjectlogin credentials = new Gson().fromJson(content, handleObjectlogin.class);
            Manager manager = Manager.newInstance();
            user = manager.login(credentials.getUsername(), credentials.getPassword());
            if(user instanceof ProUser && !credentials.isPro){
                throw new CustomException("Pro User needs Pro App");
            }
            else if(user.getClass().equals(User.class)&& credentials.isPro){
                throw new CustomException("Community User needs Community App");
            }
            String userString = new Gson().toJson(user);
            r = Response.status(Response.Status.OK).entity(userString).build();
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
class handleObjectlogin{
    String username;
    String password;
    boolean isPro;

    public handleObjectlogin(String username, String password, boolean isPro) {
        this.username = username;
        this.password = password;
        this.isPro = isPro;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isIsPro() {
        return isPro;
    }


    
}