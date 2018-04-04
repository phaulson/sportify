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
public class exercise {
    private int idExercise;
    private String description;
    private int idCreator;
    private String name;

    public exercise(int idExercise, String description, int idCreator, String name) {
        this.idExercise = idExercise;
        this.description = description;
        this.idCreator = idCreator;
        this.name = name;
    }

    public int getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(int idExercise) {
        this.idExercise = idExercise;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return "Exercise{" + "idExercise=" + idExercise + ", description=" + description + ", idCreator=" + idCreator + ", name=" + name + '}';
    }
}
