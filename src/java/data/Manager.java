/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.TreeSet;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 *
 * @author Martin
 */
public class Manager {
    Manager db = new Manager();
    private static String connString;
    private static final String USER = "d4a13";
    private static final String PASSWORD = "d4a";
    
    Connection conn;
    public Manager newInstance(){       
        return db;
    }
    private Manager(){
        try{
       conn = establishConnection();
        }
        catch(Exception e){
            
        }
    }
    /**
     * überprüft ob login erfolgreich war
     * @param username
     * @param password
     * @return bei erfolg -> userId sonst -1
     * @throws java.sql.SQLException
     */
    private Connection establishConnection()throws Exception{
        if(conn!=null){
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            Connection connection = DriverManager.getConnection(connString, USER, PASSWORD);
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            return connection;
        }
        else{
            return null;
        }
    }
    public int login(String username, String password)throws SQLException{
        
        PreparedStatement selectUserId = null;
        String select ="select idUser from sp_user where username like '"+username+"' and password like '"+password+"'"; 
        conn.setAutoCommit(false);
        selectUserId = conn.prepareStatement(select);
        return 0;
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
    public User getProfile(int idUser){
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
    public TreeSet<Plan> getPlans(int idUser){
        return null;
    }
    /**
     * Liefert alle DailyWorkouts eines Trainingsplans. 
     * @param idPlan
     * @return 
     */
    public TreeSet<DailyWorkout> getDailyWorkouts(int idPlan){
        return null;
    }
    /**
     * Liefert alle Workouts eines DailyWorkouts
     * @param idDailyWorkout
     * @return 
     */
    public TreeSet<Workout> getWorkouts(int idDailyWorkout){
        return null;
    }
    /**
     * iefert alle Exercises eines Workouts.
     * @param idWorkout
     * @return 
     */
    public TreeSet<Exercise> getExercises(int idWorkout){
        return null;
    }
    /**
     * Liefert alle von einem Pro-User erstellte Locations (und somit auch Events).
     * @param idUser
     * @return 
     */
    public TreeSet<Location> getLocations(int idUser){
        return null;
    }
    /**
     * Gibt die nächsten n (numberOfPosts) Posts zurück, die vom angegebenen User erstellt wurden. Die Posts werden ausgehend von lastPostId zurückgegeben. Wenn lastPostId nicht angegeben wird, werden die neuesten n Posts zurückgegeben. 
     * @param idCreator
     * @param [optional] lastPostId
     * @param numberOfPosts
     * @return 
     */
    public TreeSet<Post> getPostsByCreator(int idCreator, int lastPostId, int numberOfPosts){
        return null;
    }
    public TreeSet<Post> getPostsByCreator(int idCreator, int numberOfPosts){
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
    public int addLocation(int idUser, Coordinate coordinates, String name, LocationType type, LocalDate startDate, LocalDate endDate){
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
    /**
     * Verknüpft DailyWorkouts mit einem (evtl. neu erstellten) Plan. 
     * @param planId
     * @param dailyWorkouts
     * @return 
     */
    public boolean linkDailyWorkouts(int planId, Collection<Integer> dailyWorkouts){
        return false;
    }
    /**
     * Fügt ein neues DailyWorkout hinzu und gibt dessen ID zurück. (-1 falls nicht erfolgreich) 
     * @param creatorId
     * @param name
     * @return 
     */
    public int addDailyWorkout(int creatorId, String name){
        return 0;
    }
    /**
     * Verknüpft Workouts mit einem (evtl. neu erstellten) Daily Workout.
     * @param planId
     * @param workouts
     * @return 
     */
    public boolean linkWorkouts(int planId, Collection<Integer> workouts){
        return false;
    }
    /**
     * Fügt ein neues workout hinzu und gibt dessen ID zurück. (-1 falls nicht erfolgreich) 
     * @param creatorId
     * @param name
     * @return 
     */
    public int addWorkout(int creatorId, String name){
        return 0;
    }
    /**
     * Verknüpft Exercises mit einem (evtl. neu erstellten) Workout.
     * @param workoutId
     * @param exercises
     * @return 
     */
    public boolean linkExercises(int workoutId, Collection<Integer> exercises){
        return false;
    }
    
    
}
