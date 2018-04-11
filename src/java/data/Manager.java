/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.TreeSet;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
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
    public Manager(){
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
        if(conn==null){
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            conn = DriverManager.getConnection(connString, USER, PASSWORD);
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            return conn;
        }
        else{
            return conn;
        }
    }
    public int login(String username, String password)throws SQLException{
        int UserId = -1;
        PreparedStatement selectUserId;
        String selectString ="select idUser from sp_user where username like ? and password like ?"; 
        selectUserId = conn.prepareStatement(selectString);
        selectUserId.setString(0, username);
        selectUserId.setString(1,password);
        ResultSet result = selectUserId.executeQuery();
        while(result.next()){
            UserId = result.getInt(1);
        }
        return UserId;
    }
    /**
     * Überprüft ob der Username schon vorhanden ist und gibt die interne UserID zurück, wenn erfolgreich, sonst –1. Wenn erfolgreich wird der neue User hinzugefügt. 
     * @param username
     * @param password
     * @param isPro
     * @return 
     * @throws java.sql.SQLException 
     */
    public int register(String username, String password, boolean isPro) throws SQLException{
        int UserId = -1;
        PreparedStatement insertNewUser = conn.prepareStatement("insert into sp_user values(seq_user.nextval, ?, ?, ?, ?)");
        insertNewUser.setString(0,username);
        insertNewUser.setString(1,password);
        insertNewUser.setString(2,"");
        insertNewUser.setBoolean(3,isPro);
        insertNewUser.executeQuery();
        PreparedStatement selectNewUserId = conn.prepareStatement("select idUser from sp_user where username like ?");
        selectNewUserId.setString(0, username);
        ResultSet result = selectNewUserId.executeQuery();
        while(result.next()){
            UserId = result.getInt(1);
        }
        return UserId;
    }
    /**
     * Liefert das Profil zur UserID. 
     * @param idUser
     * @return 
     * @throws java.sql.SQLException 
     */
    public User getProfile(int idUser) throws SQLException{
        PreparedStatement selectProfile = conn.prepareStatement("select * from sp_user where idUser = ?");
        selectProfile.setInt(0, idUser);
        ResultSet result = selectProfile.executeQuery();
        User user;
        if(result.getBoolean(5))
             user = new ProUser(result.getInt(1), result.getString(2), result.getString(3), result.getString(4));
        else
            user = new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4));       
        return user;
    }
    /**
     * Ändert die Biographie eines Users zur UserID. 
     * @param idUser
     * @param newDescription
     * @return bei erfolg true, sonst false
     * @throws java.sql.SQLException
     */
    public boolean changeDescription(int idUser, String newDescription) throws SQLException{
        try{
        PreparedStatement changeDescription = conn.prepareStatement("update sp_user set biographie = ? where idUser = ?");
        changeDescription.setString(0, newDescription);
        changeDescription.setInt(1, idUser);
        }
        catch(SQLException ex){
            return false;
        }
        return true;
        
    }
    /**
     * Liefert alle von einem Pro-User erstellte Trainingspläne.
     * @param idUser
     * @return 
     * @throws java.sql.SQLException 
     */
    public Collection<Plan> getPlans(int idUser) throws SQLException{
        PreparedStatement getPlans = conn.prepareStatement("select idPlan, name from sp_plan inner join sp_user on sp_plan.idcreator= sp_user.iduser where idUser = ?");
        getPlans.setInt(0, idUser);
        ResultSet result = getPlans.executeQuery();
        Collection<Plan> plans = new ArrayList<>();
        while(result.next()){
            plans.add(new Plan(result.getInt(1), result.getString(2), idUser));
        }
        return plans;
    }
    /**
     * Liefert alle DailyWorkouts eines Trainingsplans. 
     * @param idPlan
     * @return 
     * @throws java.sql.SQLException 
     */
    public Collection<DailyWorkout> getDailyWorkouts(int idPlan) throws SQLException{
        PreparedStatement getDailyWorkouts = conn.prepareStatement("select sp_dailyworkout.idDailyworkout, sp_dailyworkout.idCreator, sp_dailyworkout.name from sp_dailyworkout inner join sp_containsPD on sp_containsPD.iddailyworkout = sp_dailyworkout.idDailyworkout inner join sp_plan on sp_plan.idplan = sp_containsPD.idplan where sp_plan.idplan = ?");
        getDailyWorkouts.setInt(0, idPlan);
        ResultSet result = getDailyWorkouts.executeQuery();
        Collection<DailyWorkout> dailyworkouts = new ArrayList<>();
        while(result.next()){
            dailyworkouts.add(new DailyWorkout(result.getInt(1), result.getInt(2), result.getString(3)));
        }
        return dailyworkouts;
    }
    /**
     * Liefert alle Workouts eines DailyWorkouts
     * @param idDailyWorkout
     * @return 
     * @throws java.sql.SQLException 
     */
    public Collection<Workout> getWorkouts(int idDailyWorkout) throws SQLException{
        PreparedStatement getWorkouts = conn.prepareStatement("select sp_workout.idworkout, sp_workout.idCreator, sp_workout.name from sp_workout inner join sp_containsDW on sp_containsDW.idworkout = sp_workout.idworkout inner join sp_dailyworkout on sp_dailyworkout.iddailyworkout = sp_containsDW.iddailyworkout where sp_dailyworkout.iddailyworkout = ?");
        getWorkouts.setInt(0, idDailyWorkout);
        ResultSet result = getWorkouts.executeQuery();
        Collection<Workout> workouts = new ArrayList<>();
        while(result.next()){
            workouts.add(new Workout(result.getInt(1), result.getInt(2), result.getString(3)));
        }
        return workouts;
    }
    /**
     * iefert alle Exercises eines Workouts.
     * @param idWorkout
     * @return 
     * @throws java.sql.SQLException 
     */
    public Collection<Exercise> getExercises(int idWorkout) throws SQLException{
        PreparedStatement getExercises = conn.prepareStatement("select sp_exercise.idexercise,sp_exercise.description , sp_exercise.idCreator, sp_exercise.name  from sp_exercise inner join sp_containsWE on sp_containsWE.idexercise = sp_exercise.idexercise inner join sp_workout on sp_workout.idworkout = sp_containsWE.idworkout where sp_workout.idworkout = ?");
        getExercises.setInt(0, idWorkout);
        ResultSet result = getExercises.executeQuery();
        Collection<Exercise> exercises = new ArrayList();
        while(result.next()){
            exercises.add(new Exercise(result.getInt(1), result.getString(2), result.getInt(3), result.getString(4)));
        }
        return exercises;
    }
    /**
     * Liefert alle von einem Pro-User erstellte Locations (und somit auch Events).
     * @param idUser
     * @return 
     * @throws java.sql.SQLException 
     */
    public Collection<Location> getLocations(int idUser) throws SQLException{
        PreparedStatement getLocations = conn.prepareStatement("select * from sp_location where idUser = ?");
        getLocations.setInt(0, idUser);
        ResultSet result = getLocations.executeQuery();
        Collection<Location> locations = new ArrayList();
        while(result.next()){
            Date starttime = result.getDate(7);
            Date endtime = result.getDate(8);
            if(starttime == null && endtime ==null){
                locations.add(new Location(result.getInt(1), result.getInt(2), result.getString(3), new Coordinate(result.getInt(4), result.getInt(5)), LocationType.valueOf(result.getString(6))));
            }
            else{
                locations.add(new Event(result.getInt(1), result.getInt(2), result.getString(3), new Coordinate(result.getInt(4), result.getInt(5)), LocationType.valueOf(result.getString(6)), starttime.toLocalDate(), endtime.toLocalDate()));
            }
        }
        return locations;
    }
    /**
     * Gibt die nächsten n (numberOfPosts) Posts zurück, die vom angegebenen User erstellt wurden. Die Posts werden ausgehend von lastPostId zurückgegeben. Wenn lastPostId nicht angegeben wird, werden die neuesten n Posts zurückgegeben. 
     * @param idCreator
     * @param [optional] lastPostId
     * @param numberOfPosts
     * @return 
     */
    public Collection<Post> getPostsByCreator(int idCreator, int lastPostId, int numberOfPosts) throws SQLException, Exception{
        if(lastPostId == 0){
        PreparedStatement getPostsByCreator = conn.prepareStatement("select * from sp_revPost where idCreator = ? and rownum <= ?");
        getPostsByCreator.setInt(0, idCreator);
        getPostsByCreator.setInt(1, numberOfPosts);
        ResultSet result = getPostsByCreator.executeQuery();
        Collection<Post> posts = new ArrayList();
        while(result.next()){
            
        }
        return posts;
        }
        else if(lastPostId>0){
            PreparedStatement getPostsByCreator = conn.prepareStatement("select * from sp_revPost where idCreator = ? and idPost < ? and rownum <= ?");
            getPostsByCreator.setInt(0, idCreator);
            getPostsByCreator.setInt(1, lastPostId);
            getPostsByCreator.setInt(2, numberOfPosts);
            ResultSet result = getPostsByCreator.executeQuery();
            Collection<Post> posts = new ArrayList();
            while(result.next()){
                
            }
            return posts;
        }
        else{
            throw new Exception("Invalid LastPostId");
        }
        
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
