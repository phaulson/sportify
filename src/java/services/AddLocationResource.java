/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.Coordinate;
import data.LocationType;
import data.Manager;
import java.sql.SQLException;
import java.time.LocalDate;
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
@Path("addLocation")
public class AddLocationResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AddLocationResource
     */
    public AddLocationResource() {
    }

    /**
     * Retrieves representation of an instance of services.AddLocationResource
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public int addLocation(String content) {
        int locationID = 0;
        try{
            handlerObjectGetLocation o = new Gson().fromJson(content, handlerObjectGetLocation.class);
            Manager m = Manager.newInstance();
            if(o.startdate.equals("1970-01-01") || o.enddate.equals("1970-01-01")){
                locationID = m.addLocation(o.userID, o.coordinates, o.name, o.type,null, null);

            }
            else{
            locationID = m.addLocation(o.userID, o.coordinates, o.name, o.type,LocalDate.parse(o.startdate), LocalDate.parse(o.enddate));
            }
        }
        catch(SQLException ex){
            return -200;
        }
        catch(Exception ex){
            return -10;
        }
        return locationID;
}
}
    class handlerObjectGetLocation{
        int userID;
        Coordinate coordinates;
        String name;
        LocationType type;
        String startdate;
        String enddate;

        public handlerObjectGetLocation(int userID, Coordinate coordinates, String name, LocationType type, String startdate, String enddate) {
            this.userID = userID;
            this.coordinates = coordinates;
            this.name = name;
            this.type = type;
            this.startdate = startdate;
            this.enddate = enddate;
        }
        
        
    }
