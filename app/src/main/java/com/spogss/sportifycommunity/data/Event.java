package com.spogss.sportifycommunity.data;

import java.io.Serializable;
import java.time.LocalDate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author schueler
 */
public class Event extends Location implements Serializable{
    private LocalDate startDateTime;
    private LocalDate endDateTime;

    public Event(int idLocation, Coordinate c, int idPage, String name, LocationType type, LocalDate startDateTime, LocalDate endDateTime) {
        super(idLocation, c, idPage, name, type);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;        
    }

    public LocalDate getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDate startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDate getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDate endDateTime) {
        this.endDateTime = endDateTime;
    }
    
}