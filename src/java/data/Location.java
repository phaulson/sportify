/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author Martin
 */
public class Location {
    private int idLocation;
    private double lat;
    private double lng;
    private int idPage;
    private String name;
    private LocationType type;

    public Location(int idLocation, double lat, double lng, int idPage, String name, LocationType type) {
        this.idLocation = idLocation;
        this.lat = lat;
        this.lng = lng;
        this.idPage = idPage;
        this.name = name;
        this.type = type;
    }

    public int getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(int idLocation) {
        this.idLocation = idLocation;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getIdPage() {
        return idPage;
    }

    public void setIdPage(int idPage) {
        this.idPage = idPage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Location{" + "idLocation=" + idLocation + ", lat=" + lat + ", lng=" + lng + ", idPage=" + idPage + ", name=" + name + ", type=" + type + '}';
    }


    
}