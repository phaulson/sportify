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
public class Plan {
    private int idPlan;
    private String name;
    private int idCreator;
    private TreeSet<DailyWorkout> dailyWorkouts = new TreeSet<DailyWorkout>();

    public Plan(int idPlan, String name, int idCreator) {
        this.idPlan = idPlan;
        this.name = name;
        this.idCreator = idCreator;
    }

    public int getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(int idPlan) {
        this.idPlan = idPlan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(int idCreator) {
        this.idCreator = idCreator;
    }


}