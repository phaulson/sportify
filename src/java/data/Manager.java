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
import java.sql.Timestamp;
import java.time.ZoneId;
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
        changeDescription.executeQuery();
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
     * @param lastPostId
     * @param numberOfPosts
     * @return 
     * @throws java.sql.SQLException 
     */
    public Collection<Post> getPostsByCreator(int idCreator, int lastPostId, int numberOfPosts) throws SQLException, Exception{
        if(lastPostId == 0){
        PreparedStatement getPostsByCreator = conn.prepareStatement("select * from sp_revPost where idCreator = ? and rownum <= ?");
        getPostsByCreator.setInt(0, idCreator);
        getPostsByCreator.setInt(1, numberOfPosts);
        ResultSet result = getPostsByCreator.executeQuery();
        Collection<Post> posts = new ArrayList();
        while(result.next()){
              posts.add(new Post(result.getInt(1), result.getInt(2), result.getString(3), result.getDate(4).toLocalDate()));
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
                posts.add(new Post(result.getInt(1), result.getInt(2), result.getString(3), result.getDate(4).toLocalDate()));
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
    public int addLocation(int idUser, Coordinate coordinates, String name, LocationType type, LocalDate startDate, LocalDate endDate) throws SQLException{
        PreparedStatement addLocation = conn.prepareStatement("insert into sp_location values(seq_location.nextval, ?, ?, ?, ?, ?, ?, ?)");
        addLocation.setInt(0, idUser);
        addLocation.setString(1, name);
        addLocation.setDouble(2,coordinates.getLat());
        addLocation.setDouble(3,coordinates.getLng());
        addLocation.setString(4, type.toString());
        addLocation.setTimestamp(5, Timestamp.valueOf(startDate.atStartOfDay()));
        addLocation.setTimestamp(6, Timestamp.valueOf(endDate.atStartOfDay()));
        addLocation.executeQuery();
        PreparedStatement getLocationId = conn.prepareStatement("select seq_location.currval from dual");
        ResultSet result = getLocationId.executeQuery();   
        return result.getInt(1);
        
    }
    /**
     * Erstellt einen neuen Trainingsplan und gibt dessen ID zurück. (-1 falls nicht erfolgreich).  
     * @param idCreator
     * @param name
     * @return 
     */
    public int addPlan(int idCreator, String name) throws SQLException{
        PreparedStatement addPlan = conn.prepareStatement("insert into plan values(seq_plan.nextval, ?, ?)");
        addPlan.setInt(0, idCreator);
        addPlan.setString(1, name);
        PreparedStatement getPlanId = conn.prepareStatement("select seq_plan.currval from dual");
        ResultSet result = getPlanId.executeQuery();
        return result.getInt(1);
    }
    /**
     * Verknüpft DailyWorkouts mit einem (evtl. neu erstellten) Plan. 
     * @param planId
     * @param dailyWorkouts
     * @return 
     * @throws java.sql.SQLException 
     */
    public boolean linkDailyWorkouts(int planId, Collection<Integer> dailyWorkouts) throws SQLException{
        for(int workoutId : dailyWorkouts){
        PreparedStatement linkDailyWorkouts = conn.prepareStatement("insert into sp_containsPD values(?, ?)");
        linkDailyWorkouts.setInt(0, planId);
        linkDailyWorkouts.setInt(1,workoutId);
        linkDailyWorkouts.executeQuery();
        }
        return true;
    }
    /**
     * Fügt ein neues DailyWorkout hinzu und gibt dessen ID zurück. (-1 falls nicht erfolgreich) 
     * @param creatorId
     * @param name
     * @return 
     * @throws java.sql.SQLException 
     */
    public int addDailyWorkout(int creatorId, String name) throws SQLException{
        PreparedStatement addDailyWorkout = conn.prepareStatement("insert into dailyworkout values(seq_dailyworkout.nextval, ?, ?)");
        addDailyWorkout.setInt(0, creatorId);
        addDailyWorkout.setString(1, name);
        addDailyWorkout.executeQuery();
        PreparedStatement getWorkoutId = conn.prepareStatement("select seq_dailyworkout.currval from dual");
        ResultSet result = getWorkoutId.executeQuery();
        return result.getInt(1);
    }
    /**
     * Verknüpft Workouts mit einem (evtl. neu erstellten) Daily Workout.
     * @param planId
     * @param workouts
     * @return 
     */
    public boolean linkWorkouts(int dailyWorkoutId, Collection<Integer> workouts) throws SQLException{
        for(int workoutId : workouts){
       PreparedStatement linkWorkouts = conn.prepareStatement("insert into sp_containsDW values(?, ?)");
            linkWorkouts.setInt(0, dailyWorkoutId);
            linkWorkouts.setInt(1, workoutId);
            linkWorkouts.executeQuery();
        }
        return true;
    }
    /**
     * Fügt ein neues workout hinzu und gibt dessen ID zurück. (-1 falls nicht erfolgreich) 
     * @param creatorId
     * @param name
     * @return 
     */
    public int addWorkout(int creatorId, String name) throws SQLException{
        PreparedStatement addWorkout = conn.prepareStatement("insert into workout values(seq_workout.nextval, {creatorID}, '{name}')");
        addWorkout.setInt(0, creatorId);
        addWorkout.setString(1, name);
        addWorkout.executeQuery();
        PreparedStatement getWorkoutId = conn.prepareStatement("select seq_workout.currval from dual");
        ResultSet result = getWorkoutId.executeQuery();
        return result.getInt(1);
    }
    /**
     * Verknüpft Exercises mit einem (evtl. neu erstellten) Workout.
     * @param workoutId
     * @param exercises
     * @return 
     */
    public boolean linkExercises(int workoutId, Collection<Integer> exercises) throws SQLException{
        PreparedStatement linkExercises = conn.prepareStatement("insert into sp_containsDE values(?, ?)");
        for(int exerciseId : exercises){
            linkExercises.setInt(0, workoutId);
            linkExercises.setInt(1, exerciseId);
            linkExercises.executeQuery();
        }
        return true;
        
    }
    
    /**
     * adds a new Exercise nd returns its id
     * @param name
     * @param description
     * @param creatorId
     * @return exerciseID if successful, else -1
     * @throws java.sql.SQLException
     */
    public int addExercise(String name, String description, int creatorId) throws SQLException {
        PreparedStatement addExercise = conn.prepareStatement("insert into sp_exercise values(seq_exercise.nextval, ?, ?, ?)");
        addExercise.setInt(0, creatorId);
        addExercise.setString(1, name);
        addExercise.setString(2, description);
        addExercise.executeQuery();
        PreparedStatement getExerciseId = conn.prepareStatement("select seq_exercise.currval from dual");
        ResultSet result = getExerciseId.executeQuery();
        return result.getInt(1);
    }
   /**
     * get all DailyWorkouts with that creatorID and that contain that name
     * @param creatorID
     * @param name
     * @return a collection of DailyWorkouts
     */
    public Collection<DailyWorkout> searchDailyWorkouts(int creatorID, String name) throws SQLException, Exception {
        PreparedStatement searchDailyWorkouts;
        Collection<DailyWorkout> dailyWorkouts = new ArrayList<>();
        if(creatorID<0 && name == null) {
            searchDailyWorkouts = conn.prepareStatement("select * from sp_dailyworkout");
            ResultSet result = searchDailyWorkouts.executeQuery();
            while(result.next()){
                dailyWorkouts.add(new DailyWorkout(result.getInt(1), result.getInt(2),result.getString(3)));
            }
        }
        else if(creatorID>=0 && name == null){
            searchDailyWorkouts = conn.prepareStatement("select * from sp_dailyworkout where idCreator = ?");
            searchDailyWorkouts.setInt(0, creatorID);
            ResultSet result = searchDailyWorkouts.executeQuery();
            while(result.next()){
                dailyWorkouts.add(new DailyWorkout(result.getInt(1), result.getInt(2),result.getString(3)));
            }
        }
        else if(creatorID<0 && name != null){
            searchDailyWorkouts = conn.prepareStatement("select * from sp_dailyworkout where name like %?%");
            searchDailyWorkouts.setString(0, name);
            ResultSet result = searchDailyWorkouts.executeQuery();
            while(result.next()){
                dailyWorkouts.add(new DailyWorkout(result.getInt(1), result.getInt(2),result.getString(3)));
            }
        }
        else if(creatorID>= 0 && name != null){
            searchDailyWorkouts = conn.prepareStatement("select * from sp_dailyworkout where idCreator = ? and name like %?%");
            searchDailyWorkouts.setInt(0, creatorID);
            searchDailyWorkouts.setString(1, name);
            ResultSet result = searchDailyWorkouts.executeQuery();
            while(result.next()){
                dailyWorkouts.add(new DailyWorkout(result.getInt(1), result.getInt(2),result.getString(3)));
            }
        }
        else{
            throw new Exception("Error checkin parameters");
        }
        return dailyWorkouts;
         }
    
    /**
     * get all Workouts
     * @return a collection of Workouts
     */
    public Collection<Workout> searchWorkouts(int creatorID, String name) throws SQLException {
        PreparedStatement searchWorkouts;
        Collection<Workout> workouts = new ArrayList<>();
        if(creatorID<0 && name == null){
            searchWorkouts = conn.prepareStatement("select * from sp_workout");
            ResultSet result = searchWorkouts.executeQuery();
            
        }
        return null;
    }

    
}
