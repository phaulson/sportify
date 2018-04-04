/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.time.LocalDate;
import java.util.TreeSet;

/**
 *
 * @author Martin
 */
public class Database {
    Database db = new Database();
    
    public Database newInstance(){
        return db;
    }
    /**
     * überprüft ob login erfolgreich war
     * @param username
     * @param password
     * @return bei erfolg -> userId sonst -1
     */
    public int login(String username, String password){
        int retval = -2;
        
        return retval;
    }
    /**
     * Überprüft ob der Username schon vorhanden ist und gibt die interne UserID zurück, wenn erfolgreich, sonst –1. Wenn erfolgreich wird der neue User hinzugefügt. 
     * @param username
     * @param password
     */
    public int register(String username, String password){
        int retval = -1;
        
        return retval;
    }
    /**
     * Liefert das Profil zur UserID. 
     * @param idUser
     * @return 
     */
    public user getProfile(int idUser){
        return null;
    }
    /**
     * Ändert die Biographie eines Users zur UserID. 
     * @param idUser
     * @param newDescription
     * @return bei erfolg true, sonst false
     */
    public boolean changeDescription(int idUser, String newDescription){
        return false;
    }
    /**
     * Liefert alle von einem Pro-User erstellte Trainingspläne.
     * @param idUser
     * @return 
     */
    public TreeSet<plan> getPlans(int idUser){
        return null;
    }
    /**
     * Liefert alle DailyWorkouts eines Trainingsplans. 
     * @param idPlan
     * @return 
     */
    public TreeSet<dailyWorkout> getDailyWorkouts(int idPlan){
        return null;
    }
    /**
     * Liefert alle Workouts eines DailyWorkouts
     * @param idDailyWorkout
     * @return 
     */
    public TreeSet<workout> getWorkouts(int idDailyWorkout){
        return null;
    }
    /**
     * iefert alle Exercises eines Workouts.
     * @param idWorkout
     * @return 
     */
    public TreeSet<exercise> getExercises(int idWorkout){
        return null;
    }
    /**
     * Liefert alle von einem Pro-User erstellte Locations (und somit auch Events).
     * @param idUser
     * @return 
     */
    public TreeSet<location> getLocations(int idUser){
        return null;
    }
    /**
     * Gibt die nächsten n (numberOfPosts) Posts zurück, die vom angegebenen User erstellt wurden. Die Posts werden ausgehend von lastPostId zurückgegeben. Wenn lastPostId nicht angegeben wird, werden die neuesten n Posts zurückgegeben. 
     * @param idCreator
     * @param [optional] lastPostId
     * @param numberOfPosts
     * @return 
     */
    public TreeSet<post> getPostsByCreator(int idCreator, int lastPostId, int numberOfPosts){
        return null;
    }
    public TreeSet<post> getPostsByCreator(int idCreator, int numberOfPosts){
        return getPostsByCreator(idCreator, 0, numberOfPosts);
    }
    /**
     * Fügt eine neue Location hinzu und gibt deren ID zurück. (-1 falls nicht erfolgreich) 
Wenn startdate und enddate nicht NULL sind, handelt es sich um ein Event und der Typ muss EVENT sein. Wenn startdate und enddate NULL sind, darf der Typ nicht 'EVENT' sein. 
     * @param idUser
     * @param coordinates
     * @param name
     * @param type
     * @param startDate
     * @param endDate
     * @return 
     */
    public int addLocation(int idUser, Coordinates coordinates, String name, locationType type, LocalDate startDate, LocalDate endDate){
        int retval = -1;
        
        return retval;
    }
    /**
     * Erstellt einen neuen Trainingsplan und gibt dessen ID zurück. (-1 falls nicht erfolgreich).  
     * @param idCreator
     * @param name
     * @return 
     */
    public int addPlan(int idCreator, String name){
        int retval = -1;
        
        return retval;
    }
    /////////////////////////////////////////////////////
    
}
