/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.ArrayList;
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
@Path("searchPlans")
public class SearchPlansResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SearchPlansResource
     */
    public SearchPlansResource() {
    }

    /**
     * Retrieves representation of an instance of data.SearchPlansResource
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Plan> searchPlans(String content) {
        Collection<Plan> plans = new ArrayList<>();
        try{
            handleObjectSearchPlans o = new Gson().fromJson(content, handleObjectSearchPlans.class);
            Manager m = Manager.newInstance();
            plans = m.searchPlans(o.creatorID, o.name);           
        }
        catch(SQLException ex){
            
        }
        catch(Exception ex){
            
        }
        return plans;
    }

}
class handleObjectSearchPlans{
    int creatorID;
    String name;

    public handleObjectSearchPlans(int creatorID, String name) {
        this.creatorID = creatorID;
        this.name = name;
    }
}
