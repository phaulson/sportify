package data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author schueler
 */
@XmlRootElement
public class Event extends Location implements Serializable{
    private Date startDateTime;
    private Date endDateTime;

    public Event(){
        super();
    }
    
    public Event(int idLocation,  int idPage, String name,Coordinate c, LocationType type, Date startDateTime, Date endDateTime) {
        super(idLocation,idPage,name, c,  type);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;        
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }
    
}