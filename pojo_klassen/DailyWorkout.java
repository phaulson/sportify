package pkgData;

import java.util.TreeSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author schueler
 */
public class DailyWorkout {
    private int idDailyWorkout;
    private int idCreator;
    private int day;
    private int name;
    private TreeSet<Workout> workouts = new TreeSet<Workout>();

    public DailyWorkout(int idDailyWorkout, int idCreator, int day, int name) {
        this.idDailyWorkout = idDailyWorkout;
        this.idCreator = idCreator;
        this.day = day;
        this.name = name;
    }

    public int getIdDailyWorkout() {
        return idDailyWorkout;
    }

    public void setIdDailyWorkout(int idDailyWorkout) {
        this.idDailyWorkout = idDailyWorkout;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(int idCreator) {
        this.idCreator = idCreator;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DailyWorkout{" + "idDailyWorkout=" + idDailyWorkout + ", idCreator=" + idCreator + ", day=" + day + ", name=" + name + '}';
    }


}
