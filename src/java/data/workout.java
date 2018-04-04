/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.TreeSet;

/**
 *
 * @author Martin
 */
public class workout {
    private int idWorkout;
    private int idCreator;
    private String name;
    private TreeSet<exercise> exercises = new TreeSet<exercise>();
    //collection exercises

    public workout(int idWorkout, int idCreator, String name) {
        this.idWorkout = idWorkout;
        this.idCreator = idCreator;
        this.name = name;
    }

    public int getIdWorkout() {
        return idWorkout;
    }

    public void setIdWorkout(int idWorkout) {
        this.idWorkout = idWorkout;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(int idCreator) {
        this.idCreator = idCreator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

}