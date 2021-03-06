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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author Martin
 */
public class Manager {

    static Manager db = new Manager();
    //connection from inside the htl network
    private static final String CONNSTRING = "jdbc:oracle:thin:@192.168.128.152:1521:ora11g";
    //connection from outside the htl network
    private static final String ALTERNATIVE_CONNSTRING = "jdbc:oracle:thin:@212.152.179.117:1521:ora11g";
    //user credentials
    private static final String USER = "d4a13";
    private static final String PASSWORD = "d4a";
    Connection conn;

    //singleton implementation
    public static Manager newInstance() {
        return db;
    }

    /**
     * Constructor
     */
    private Manager() {
        try {
            conn = establishConnection();
        } catch (Exception e) {
            System.out.println("Error while establishing connection!\n" + e.getMessage());
        }
    }

    /**
     * establishes the connection to the db
     *
     * @return
     * @throws Exception
     */
    private Connection establishConnection() throws Exception {

        if (conn == null) {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            conn = DriverManager.getConnection(CONNSTRING, USER, PASSWORD);
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        } else {
            return conn;
        }

        return conn;
    }

    /**
     * überprüft ob login erfolgreich war
     *
     * @param username
     * @param password
     * @return bei erfolg -> userId sonst -1
     * @throws java.sql.SQLException
     */
    public User login(String username, String password) throws SQLException {
        PreparedStatement selectUserId;
        String selectString = "select idUser, username, password, biographie, isPro from sp_user where lower(username) like ? and password like ?";
        selectUserId = conn.prepareStatement(selectString);
        selectUserId.setString(1, username);
        selectUserId.setString(2, password);

        ResultSet result = selectUserId.executeQuery();
        result.next();

        User u = null;
        if (result.getInt(5) == 1) {
            u = new ProUser(result.getInt(1), result.getString(2), result.getString(3), result.getString(4));
        } else {
            u = new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4));
        }
        
        selectUserId.close();
        result.close();
        return u;
    }

    /**
     * Überprüft ob der Username schon vorhanden ist und gibt die interne UserID
     * zurück, wenn erfolgreich, sonst –1. Wenn erfolgreich wird der neue User
     * hinzugefügt.
     *
     * @param username
     * @param password
     * @param isPro
     * @return
     * @throws java.sql.SQLException
     */
    public User register(String username, String password, boolean isPro) throws SQLException {
        PreparedStatement insertNewUser = conn.prepareStatement("insert into sp_user values(seq_user.nextval, ?, ?, ?, ?)");
        insertNewUser.setString(1, username);
        insertNewUser.setString(2, password);
        insertNewUser.setString(3, "");
        insertNewUser.setInt(4, (isPro ? 1 : 0));
        insertNewUser.executeQuery();
        PreparedStatement selectNewUserId = conn.prepareStatement("select * from sp_user where username like ?");
        selectNewUserId.setString(1, username);
        ResultSet result = selectNewUserId.executeQuery();
        result.next();

        User u = null;
        if (isPro) {
            u = new ProUser(result.getInt(1), result.getString(2), result.getString(3), result.getString(4));
        } else {
            u = new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4));
        }

        insertNewUser.close();
        selectNewUserId.close();
        result.close();
        return u;
    }

    /**
     * Liefert das Profil zur UserID.
     *
     * @param idUser
     * @return
     * @throws java.sql.SQLException
     */
    public User getProfile(int idUser) throws SQLException {
        User user;
        PreparedStatement selectProfile = conn.prepareStatement("select * from sp_user where iduser =?");
        selectProfile.setDouble(1, idUser);
        ResultSet result = selectProfile.executeQuery();
        result.next();
        if (result.getInt(5) == 1) {
            user = new ProUser(result.getInt(1), result.getString(2), null, result.getString(4));
        } else {
            user = new User(result.getInt(1), result.getString(2), null, result.getString(4));
        }
        
        selectProfile.close();
        result.close();
        return user;
    }

    /**
     *
     * @param idUser
     * @return
     * @throws SQLException
     */
    public boolean isPro(int idUser) throws SQLException {
        PreparedStatement selectIsPro = conn.prepareStatement("select isPro from sp_user where iduser =?");
        selectIsPro.setDouble(1, idUser);
        ResultSet result = selectIsPro.executeQuery();
        result.next();

        boolean pro = result.getInt(1) == 1;
        selectIsPro.close();
        result.close();
        return pro;
    }

    /**
     * Ändert die Biographie eines Users zur UserID.
     *
     * @param idUser
     * @return bei erfolg true, sonst false
     * @throws java.sql.SQLException
     */
    public int getFollowedUsersCount(int idUser) throws SQLException {
        int count;

        PreparedStatement getCount = conn.prepareStatement("select COUNT(*) from sp_user where idUser in (select idOl from sp_follow where idFollower = ?)");
        getCount.setInt(1, idUser);
        ResultSet result = getCount.executeQuery();
        result.next();
        count = result.getInt(1);
        
        getCount.close();
        result.close();
        return count;
    }

    public int getFollowersCount(int idUser) throws SQLException {
        int count;

        PreparedStatement getCount = conn.prepareStatement("select COUNT(*) from sp_user where idUser in (select idfollower from sp_follow where idol = ?)");
        getCount.setInt(1, idUser);
        ResultSet result = getCount.executeQuery();
        result.next();
        count = result.getInt(1);
        getCount.close();
        result.close();
        return count;
    }

    public int getSubscribersCount(int idPlan) throws SQLException {
        int count;
        PreparedStatement getCount = conn.prepareStatement("select COUNT(*) from sp_user where iduser in (select idUser from sp_subscription where idPlan = ?)");
        getCount.setInt(1, idPlan);
        ResultSet result = getCount.executeQuery();
        result.next();
        count = result.getInt(1);
        
        getCount.close();
        result.close();
        return count;
    }

    public boolean changeDescription(int idUser, String newDescription) throws SQLException {
        try {
            PreparedStatement changeDescription = conn.prepareStatement("update sp_user set biographie = ? where idUser = ?");
            changeDescription.setString(1, newDescription);
            changeDescription.setInt(2, idUser);
            changeDescription.executeQuery();
            changeDescription.close();
        } catch (SQLException ex) {
            return false;
        }
        return true;

    }

    /**
     * Liefert alle von einem Pro-User erstellte Trainingspläne.
     *
     * @param idUser
     * @return
     * @throws java.sql.SQLException
     */
    public Collection<Plan> getPlans(int idUser) throws SQLException {
        PreparedStatement getPlans = conn.prepareStatement("select idPlan, name from sp_plan inner join sp_user on sp_plan.idcreator= sp_user.iduser where idUser = ?");
        getPlans.setInt(1, idUser);
        ResultSet result = getPlans.executeQuery();
        Collection<Plan> plans = new ArrayList<>();
        while (result.next()) {
            plans.add(new Plan(result.getInt(1), idUser, result.getString(2)));
        }
        getPlans.close();
        result.close();
        return plans;
    }

    /**
     * Liefert alle DailyWorkouts eines Trainingsplans.
     *
     * @param idPlan
     * @return
     * @throws java.sql.SQLException
     */
    public Collection<DailyWorkout> getDailyWorkouts(int idPlan) throws SQLException {
        PreparedStatement getDailyWorkouts = conn.prepareStatement("select sp_dailyworkout.idDailyworkout, sp_dailyworkout.idCreator, sp_dailyworkout.name from sp_dailyworkout inner join sp_containsPD on sp_containsPD.iddailyworkout = sp_dailyworkout.idDailyworkout inner join sp_plan on sp_plan.idplan = sp_containsPD.idplan where sp_plan.idplan = ?");
        getDailyWorkouts.setInt(1, idPlan);
        ResultSet result = getDailyWorkouts.executeQuery();
        Collection<DailyWorkout> dailyworkouts = new ArrayList<>();
        while (result.next()) {
            dailyworkouts.add(new DailyWorkout(result.getInt(1), result.getInt(2), result.getString(3)));
        }
        getDailyWorkouts.close();
        result.close();
        return dailyworkouts;
    }

    /**
     * Liefert alle Workouts eines DailyWorkouts
     *
     * @param idDailyWorkout
     * @return
     * @throws java.sql.SQLException
     */
    public Collection<Workout> getWorkouts(int idDailyWorkout) throws SQLException {
        PreparedStatement getWorkouts = conn.prepareStatement("select sp_workout.idworkout, sp_workout.idCreator, sp_workout.name from sp_workout inner join sp_containsDW on sp_containsDW.idworkout = sp_workout.idworkout inner join sp_dailyworkout on sp_dailyworkout.iddailyworkout = sp_containsDW.iddailyworkout where sp_dailyworkout.iddailyworkout = ?");
        getWorkouts.setInt(1, idDailyWorkout);
        ResultSet result = getWorkouts.executeQuery();
        Collection<Workout> workouts = new ArrayList<>();
        while (result.next()) {
            workouts.add(new Workout(result.getInt(1), result.getInt(2), result.getString(3)));
        }
        getWorkouts.close();
        result.close();
        return workouts;
    }

    /**
     * iefert alle Exercises eines Workouts.
     *
     * @param idWorkout
     * @return
     * @throws java.sql.SQLException
     */
    public Collection<Exercise> getExercises(int idWorkout) throws SQLException {
        PreparedStatement getExercises = conn.prepareStatement("select sp_exercise.idexercise, sp_exercise.idCreator , sp_exercise.name, sp_exercise.description  from sp_exercise inner join sp_containsWE on sp_containsWE.idexercise = sp_exercise.idexercise inner join sp_workout on sp_workout.idworkout = sp_containsWE.idworkout where sp_workout.idworkout =?");
        getExercises.setInt(1, idWorkout);
        ResultSet result = getExercises.executeQuery();
        Collection<Exercise> exercises = new ArrayList();
        while (result.next()) {
            exercises.add(new Exercise(result.getInt(1), result.getInt(2), result.getString(3), result.getString(4)));
        }
        getExercises.close();
        result.close();
        return exercises;
    }

    /**
     * Liefert alle von einem Pro-User erstellte Locations (und somit auch
     * Events).
     *
     * @param idUser
     * @return
     * @throws java.sql.SQLException
     */
    public Collection<Location> getLocations(int idUser) throws SQLException {
        PreparedStatement getLocations = conn.prepareStatement("select * from sp_location where idUser = ?");
        getLocations.setInt(1, idUser);
        ResultSet result = getLocations.executeQuery();
        Collection<Location> locations = new ArrayList();
        while (result.next()) {
            Date starttime = result.getDate(7);
            Date endtime = result.getDate(8);
            if (starttime == null && endtime == null) {
                locations.add(new Location(result.getInt(1), result.getInt(2), result.getString(3), new Coordinate(result.getInt(4), result.getInt(5)), LocationType.valueOf(result.getString(6))));
            } else if (starttime != null && endtime != null) {
                locations.add(new Event(result.getInt(1), result.getInt(2), result.getString(3), new Coordinate(result.getInt(4), result.getInt(5)), LocationType.valueOf(result.getString(6)), starttime, endtime));
            }
        }
        getLocations.close();
        result.close();
        return locations;
    }

    /**
     * Gibt die nächsten n (numberOfPosts) Posts zurück, die vom angegebenen
     * User erstellt wurden. Die Posts werden ausgehend von lastPostId
     * zurückgegeben. Wenn lastPostId nicht angegeben wird, werden die neuesten
     * n Posts zurückgegeben.
     *
     * @param idCreator
     * @param lastPostId
     * @param numberOfPosts
     * @return
     * @throws java.sql.SQLException
     */
    public Collection<Post> getPostsByCreator(int idCreator, int lastPostId, int numberOfPosts) throws SQLException, Exception {
        if (lastPostId <= 0) {
            PreparedStatement getPostsByCreator = conn.prepareStatement("select * from sp_revPost where idCreator = ? and rownum <= ?");
            getPostsByCreator.setInt(1, idCreator);
            getPostsByCreator.setInt(2, numberOfPosts);
            ResultSet result = getPostsByCreator.executeQuery();
            Collection<Post> posts = new ArrayList();
            while (result.next()) {
                posts.add(new Post(result.getInt(1), result.getInt(2), result.getString(3), result.getTimestamp(4)));
            }
            getPostsByCreator.close();
            result.close();
            return posts;
        } else if (lastPostId > 0) {
            PreparedStatement getPostsByCreator = conn.prepareStatement("select * from sp_revPost where idCreator = ? and idPost < ? and rownum <= ?");
            getPostsByCreator.setInt(1, idCreator);
            getPostsByCreator.setInt(2, lastPostId);
            getPostsByCreator.setInt(3, numberOfPosts);
            ResultSet result = getPostsByCreator.executeQuery();
            Collection<Post> posts = new ArrayList();
            while (result.next()) {
                posts.add(new Post(result.getInt(1), result.getInt(2), result.getString(3), result.getTimestamp(4)));
            }
            getPostsByCreator.close();
            result.close();
            return posts;
        } else {
            throw new Exception("Invalid LastPostId");
        }

    }

    /**
     * Fügt eine neue Location hinzu und gibt deren ID zurück. (-1 falls nicht
     * erfolgreich) Wenn startdate und enddate nicht NULL sind, handelt es sich
     * um ein Event und der Typ muss EVENT sein. Wenn startdate und enddate NULL
     * sind, darf der Typ nicht 'EVENT' sein.
     *
     * @param idUser
     * @param coordinates
     * @param name
     * @param type
     * @param startDate
     * @param endDate
     * @return
     * @throws java.sql.SQLException
     */
    public int addLocation(int idUser, Coordinate coordinates, String name, LocationType type, LocalDate startDate, LocalDate endDate) throws SQLException {
        PreparedStatement addLocation = conn.prepareStatement("insert into sp_location values(seq_location.nextval, ?, ?, ?, ?, ?, ?, ?)");
        addLocation.setInt(1, idUser);
        addLocation.setString(2, name);
        addLocation.setDouble(3, coordinates.getLat());
        addLocation.setDouble(4, coordinates.getLng());
        addLocation.setString(5, type.toString());
        if (startDate == null || endDate == null) {
            addLocation.setNull(6, Types.VARCHAR);
            addLocation.setNull(7, Types.VARCHAR);
        } else {
            addLocation.setTimestamp(6, Timestamp.valueOf(startDate.atStartOfDay()));
            addLocation.setTimestamp(7, Timestamp.valueOf(endDate.atStartOfDay()));
        }
        addLocation.executeQuery();
        PreparedStatement getLocationId = conn.prepareStatement("select seq_location.currval from dual");
        ResultSet result = getLocationId.executeQuery();
        result.next();
        
        int id = result.getInt(1);
        addLocation.close();
        getLocationId.close();
        result.close();
        return id;
    }

    /**
     * Erstellt einen neuen Trainingsplan und gibt dessen ID zurück. (-1 falls
     * nicht erfolgreich).
     *
     * @param idCreator
     * @param name
     * @return
     * @throws java.sql.SQLException
     */
    public int addPlan(int idCreator, String name) throws SQLException {
        PreparedStatement addPlan = conn.prepareStatement("insert into sp_plan values(seq_plan.nextval, ?, ?)");
        addPlan.setInt(1, idCreator);
        addPlan.setString(2, name);
        addPlan.executeQuery();
        PreparedStatement getPlanId = conn.prepareStatement("select seq_plan.currval from dual");
        ResultSet result = getPlanId.executeQuery();
        result.next();
        
        int id = result.getInt(1);
        addPlan.close();
        getPlanId.close();
        result.close();
        return id;
    }

    /**
     * Verknüpft DailyWorkouts mit einem (evtl. neu erstellten) Plan.
     *
     * @param planId
     * @param dailyWorkouts
     * @return
     * @throws java.sql.SQLException
     */
    public boolean linkDailyWorkouts(int planId, Collection<Integer> dailyWorkouts) throws SQLException {
        for (int workoutId : dailyWorkouts) {
            PreparedStatement linkDailyWorkouts = conn.prepareStatement("insert into sp_containsPD values(?, ?)");
            linkDailyWorkouts.setInt(1, planId);
            linkDailyWorkouts.setInt(2, workoutId);
            linkDailyWorkouts.executeQuery();
            linkDailyWorkouts.close();
        }
        return true;
    }

    /**
     * Fügt ein neues DailyWorkout hinzu und gibt dessen ID zurück. (-1 falls
     * nicht erfolgreich)
     *
     * @param creatorId
     * @param name
     * @return
     * @throws java.sql.SQLException
     */
    public int addDailyWorkout(int creatorId, String name) throws SQLException {
        PreparedStatement addDailyWorkout = conn.prepareStatement("insert into sp_dailyworkout values(seq_dailyworkout.nextval, ?, ?)");
        addDailyWorkout.setInt(1, creatorId);
        addDailyWorkout.setString(2, name);
        addDailyWorkout.executeQuery();
        PreparedStatement getWorkoutId = conn.prepareStatement("select seq_dailyworkout.currval from dual");
        ResultSet result = getWorkoutId.executeQuery();
        result.next();
        
        int id = result.getInt(1);
        addDailyWorkout.close();
        getWorkoutId.close();
        result.close();
        return id;
    }

    /**
     * Verknüpft Workouts mit einem (evtl. neu erstellten) Daily Workout.
     *
     * @param dailyWorkoutId
     * @param workouts
     * @return
     * @throws java.sql.SQLException
     */
    public boolean linkWorkouts(int dailyWorkoutId, Collection<Integer> workouts) throws SQLException {
        for (int workoutId : workouts) {
            PreparedStatement linkWorkouts = conn.prepareStatement("insert into sp_containsDW values(?, ?)");
            linkWorkouts.setInt(1, dailyWorkoutId);
            linkWorkouts.setInt(2, workoutId);
            linkWorkouts.executeQuery();
            linkWorkouts.close();
        }
        return true;
    }

    /**
     * Fügt ein neues workout hinzu und gibt dessen ID zurück. (-1 falls nicht
     * erfolgreich)
     *
     * @param creatorId
     * @param name
     * @return
     * @throws java.sql.SQLException
     */
    public int addWorkout(int creatorId, String name) throws SQLException {
        PreparedStatement addWorkout = conn.prepareStatement("insert into sp_workout values(seq_workout.nextval, ?, ?)");
        addWorkout.setInt(1, creatorId);
        addWorkout.setString(2, name);
        addWorkout.executeQuery();
        PreparedStatement getWorkoutId = conn.prepareStatement("select seq_workout.currval from dual");
        ResultSet result = getWorkoutId.executeQuery();
        result.next();
        
        int id = result.getInt(1);
        addWorkout.close();
        getWorkoutId.close();
        result.close();
        return id;
    }

    /**
     * Verknüpft Exercises mit einem (evtl. neu erstellten) Workout.
     *
     * @param workoutId
     * @param exercises
     * @return
     * @throws java.sql.SQLException
     */
    public boolean linkExercises(int workoutId, Collection<Integer> exercises) throws SQLException {
        PreparedStatement linkExercises = conn.prepareStatement("insert into sp_containsWE values(?, ?)");
        for (int exerciseId : exercises) {
            linkExercises.setInt(1, workoutId);
            linkExercises.setInt(2, exerciseId);
            linkExercises.executeQuery();
            linkExercises.close();
        }
        return true;

    }

    /**
     * adds a new Exercise nd returns its id
     *
     * @param name
     * @param description
     * @param creatorId
     * @return exerciseID if successful, else -1
     * @throws java.sql.SQLException
     */
    public int addExercise(String name, String description, int creatorId) throws SQLException {
        PreparedStatement addExercise = conn.prepareStatement("insert into sp_exercise values(seq_exercise.nextval, ?, ?, ?)");
        addExercise.setInt(1, creatorId);
        addExercise.setString(2, name);
        addExercise.setString(3, description);
        addExercise.executeQuery();
        PreparedStatement getExerciseId = conn.prepareStatement("select seq_exercise.currval from dual");
        ResultSet result = getExerciseId.executeQuery();
        result.next();
        
        int id = result.getInt(1);
        addExercise.close();
        getExerciseId.close();
        result.close();
        return id;
    }

    /**
     * get all DailyWorkouts with that creatorID and that contain that name
     *
     * @param creatorID
     * @param name
     * @param lastDailyWorkoutId
     * @param numberOfDailyWorkouts
     * @return a collection of DailyWorkouts
     * @throws java.sql.SQLException
     */
    public Collection<DailyWorkout> searchDailyWorkouts(int creatorID, String name, int lastDailyWorkoutId, int numberOfDailyWorkouts) throws SQLException, Exception {
        PreparedStatement searchDailyWorkouts;
        Collection<DailyWorkout> dailyWorkouts = new ArrayList<>();
        if (creatorID < 0 && name == null) {
            searchDailyWorkouts = conn.prepareStatement("select * from sp_dailyworkout where iddailyworkout > ? and rownum <= ?");
            searchDailyWorkouts.setInt(1, lastDailyWorkoutId);
            searchDailyWorkouts.setInt(2, numberOfDailyWorkouts);
            ResultSet result = searchDailyWorkouts.executeQuery();
            while (result.next()) {
                dailyWorkouts.add(new DailyWorkout(result.getInt(1), result.getInt(2), result.getString(3)));
            }
            result.close();
        } else if (creatorID >= 0 && name == null) {
            searchDailyWorkouts = conn.prepareStatement("select * from sp_dailyworkout where idCreator = ? and iddailyworkout > ? and rownum <= ?");
            searchDailyWorkouts.setInt(1, creatorID);
            searchDailyWorkouts.setInt(2, lastDailyWorkoutId);
            searchDailyWorkouts.setInt(3, numberOfDailyWorkouts);
            ResultSet result = searchDailyWorkouts.executeQuery();
            while (result.next()) {
                dailyWorkouts.add(new DailyWorkout(result.getInt(1), result.getInt(2), result.getString(3)));
            }
        } else if (creatorID < 0 && name != null) {
            searchDailyWorkouts = conn.prepareStatement("select * from sp_dailyworkout where lower(name) like ? and iddailyworkout > ? and rownum <= ?");
            searchDailyWorkouts.setString(1, "%" + name + "%");
            searchDailyWorkouts.setInt(2, lastDailyWorkoutId);
            searchDailyWorkouts.setInt(3, numberOfDailyWorkouts);
            ResultSet result = searchDailyWorkouts.executeQuery();
            while (result.next()) {
                dailyWorkouts.add(new DailyWorkout(result.getInt(1), result.getInt(2), result.getString(3)));
            }
            result.close();
        } else if (creatorID >= 0 && name != null) {
            searchDailyWorkouts = conn.prepareStatement("select * from sp_dailyworkout where idCreator = ? and lower(name) like ? and iddailyworkout > ? and rownum <= ?");
            searchDailyWorkouts.setInt(1, creatorID);
            searchDailyWorkouts.setString(2, "%" + name + "%");
            searchDailyWorkouts.setInt(3, lastDailyWorkoutId);
            searchDailyWorkouts.setInt(4, numberOfDailyWorkouts);
            ResultSet result = searchDailyWorkouts.executeQuery();
            while (result.next()) {
                dailyWorkouts.add(new DailyWorkout(result.getInt(1), result.getInt(2), result.getString(3)));
            }
            result.close();
        } else {
            throw new Exception("Error checkin parameters");
        }
        searchDailyWorkouts.close();
        return dailyWorkouts;
    }

    /**
     * get all Workouts
     *
     * @param creatorID
     * @param name
     * @param lastWorkoutId
     * @param numberOfWorkouts
     * @return a collection of Workouts
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    public Collection<Workout> searchWorkouts(int creatorID, String name, int lastWorkoutId, int numberOfWorkouts) throws SQLException, Exception {
        PreparedStatement searchWorkouts;
        Collection<Workout> workouts = new ArrayList<>();
        if (creatorID <= 0 && name == null) {
            searchWorkouts = conn.prepareStatement("select * from sp_workout where idworkout > ? and rownum <= ?");
            searchWorkouts.setInt(1, lastWorkoutId);
            searchWorkouts.setInt(2, numberOfWorkouts);
            ResultSet result = searchWorkouts.executeQuery();
            while (result.next()) {
                workouts.add(new Workout(result.getInt(1), result.getInt(2), result.getString(3)));
            }
            result.close();
        } else if (creatorID > 0 && name == null) {
            searchWorkouts = conn.prepareStatement("select * from sp_workout where idCreator = ? and idworkout > ? and rownum <= ?");
            searchWorkouts.setInt(1, creatorID);
            searchWorkouts.setInt(2, lastWorkoutId);
            searchWorkouts.setInt(3, numberOfWorkouts);
            ResultSet result = searchWorkouts.executeQuery();
            while (result.next()) {
                workouts.add(new Workout(result.getInt(1), result.getInt(2), result.getString(3)));
            }
            result.close();
        } else if (creatorID <= 0 && name != null) {
            searchWorkouts = conn.prepareStatement("select * from sp_workout where lower(name) like ? and idworkout > ? and rownum <= ?");
            searchWorkouts.setString(1, "%" + name + "%");
            searchWorkouts.setInt(2, lastWorkoutId);
            searchWorkouts.setInt(3, numberOfWorkouts);
            ResultSet result = searchWorkouts.executeQuery();
            while (result.next()) {
                workouts.add(new Workout(result.getInt(1), result.getInt(2), result.getString(3)));
            }
            result.close();
        } else if (creatorID > 0 && name != null) {
            searchWorkouts = conn.prepareStatement("select * from sp_workout where idCreator = ? and lower(name) like ? and idworkout > ? and rownum <= ?");
            searchWorkouts.setInt(1, creatorID);
            searchWorkouts.setString(2, "%" + name + "%");
            searchWorkouts.setInt(3, lastWorkoutId);
            searchWorkouts.setInt(4, numberOfWorkouts);
            ResultSet result = searchWorkouts.executeQuery();
            while (result.next()) {
                workouts.add(new Workout(result.getInt(1), result.getInt(2), result.getString(3)));
            }
            result.close();
        } else {
            throw new Exception("Unknown Error");
        }
        searchWorkouts.close();
        return workouts;
    }

    /**
     * get all Exercises
     *
     * @param creatorID
     * @param name
     * @param lastExerciseId
     * @param numberOfExercises
     * @return a collection of Exercises
     * @throws java.sql.SQLException
     */
    public Collection<Exercise> searchExercises(int creatorID, String name, int lastExerciseId, int numberOfExercises) throws SQLException, Exception {
        PreparedStatement searchExercises;
        Collection<Exercise> exercises = new ArrayList<>();
        if (creatorID <= 0 && name == null) {
            searchExercises = conn.prepareStatement("select * from sp_exercise where idexercise > ? and rownum <= ?");
            searchExercises.setInt(1, lastExerciseId);
            searchExercises.setInt(2, numberOfExercises);
            ResultSet result = searchExercises.executeQuery();
            while (result.next()) {
                exercises.add(new Exercise(result.getInt(1), result.getInt(2), result.getString(3), result.getString(4)));
            }
            result.close();
        } else if (creatorID > 0 && name == null) {
            searchExercises = conn.prepareStatement("select * from sp_exercise where idCreator = ? and idexercise > ? and rownum <= ?");
            searchExercises.setInt(1, creatorID);
            searchExercises.setInt(2, lastExerciseId);
            searchExercises.setInt(3, numberOfExercises);
            ResultSet result = searchExercises.executeQuery();
            while (result.next()) {
                exercises.add(new Exercise(result.getInt(1), result.getInt(2), result.getString(3), result.getString(4)));
            }
            result.close();
        } else if (creatorID > 0 && name != null) {
            searchExercises = conn.prepareStatement("select * from sp_exercise where lower(name) like ? and idexercise > ? and rownum <= ?");
            searchExercises.setString(1, "%" + name + "%");
            searchExercises.setInt(2, lastExerciseId);
            searchExercises.setInt(3, numberOfExercises);
            ResultSet result = searchExercises.executeQuery();
            while (result.next()) {
                exercises.add(new Exercise(result.getInt(1), result.getInt(2), result.getString(3), result.getString(4)));
            }
            result.close();
        } else if (creatorID > 0 && name == null) {
            searchExercises = conn.prepareStatement("select * from sp_exerice where idCreator = ? and lower(name) like ? and idexercise > ? and rownum <= ?");
            searchExercises.setInt(1, creatorID);
            searchExercises.setString(2, "%" + name + "%");
            searchExercises.setInt(3, lastExerciseId);
            searchExercises.setInt(4, numberOfExercises);
            ResultSet result = searchExercises.executeQuery();
            while (result.next()) {
                exercises.add(new Exercise(result.getInt(1), result.getInt(2), result.getString(3), result.getString(4)));
            }
            result.close();
        } else {
            throw new Exception("Unknonw Error");
        }
        searchExercises.close();
        return exercises;
    }

    /**
     * add a new Post and return its ID
     *
     * @param creatorId
     * @param caption
     * @return postID if successful, else -1
     * @throws java.sql.SQLException
     */
    public int addPost(int creatorId, String caption) throws SQLException {
        PreparedStatement addPost = conn.prepareStatement("insert into sp_post values(seq_post.nextval, ?, ?, systimestamp)");
        addPost.setInt(1, creatorId);
        addPost.setString(2, caption);
        addPost.executeQuery();
        PreparedStatement getPostId = conn.prepareStatement("select seq_post.currval from dual");
        ResultSet result = getPostId.executeQuery();
        result.next();
        
        int id = result.getInt(1);
        addPost.close();
        getPostId.close();
        result.close();
        return id;
    }

    /**
     * gets the next n number of Posts that creators are subscribed by the
     * specified user
     *
     * @param userId
     * @param lastPostId
     * @param numberOfPosts
     * @return a collection of Posts
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    public Collection<Post> getPosts(int userId, int lastPostId, int numberOfPosts) throws SQLException, Exception {
        Collection<Post> posts = new ArrayList<>();
        if (lastPostId <= 0) {
            PreparedStatement getPosts = conn.prepareStatement("select * from sp_revPost where (idCreator in (select idOl from sp_follow where idFollower = ?) or idCreator = ?) and rownum <= ?");
            getPosts.setInt(1, userId);
            getPosts.setInt(2, userId);
            getPosts.setInt(3, numberOfPosts);
            ResultSet result = getPosts.executeQuery();
            while (result.next()) {
                posts.add(new Post(result.getInt(1), result.getInt(2), result.getString(3), result.getTimestamp(4)));
            }
            getPosts.close();
            result.close();
        } else if (lastPostId > 0) {
            PreparedStatement getPosts = conn.prepareStatement("select * from sp_revPost where (idCreator in (select idOl from sp_follow where idFollower = ?) or idCreator = ?) and idPost < ? and rownum <= ?");
            getPosts.setInt(1, userId);
            getPosts.setInt(2, userId);
            getPosts.setInt(3, lastPostId);
            getPosts.setInt(4, numberOfPosts);
            ResultSet result = getPosts.executeQuery();
            while (result.next()) {
                posts.add(new Post(result.getInt(1), result.getInt(2), result.getString(3), result.getTimestamp(4)));
            }
            getPosts.close();
            result.close();
        } else {
            throw new Exception("Unknown Error");
        }

        return posts;
    }

    /**
     * gets all Users that contain that name and their pro-attribute is
     * equivalent to the pro-parameter
     *
     * @param name
     * @param isPro
     * @param lastUserId
     * @param numberOfUsers
     * @return a collection of Users
     * @throws java.sql.SQLException
     */
    public Collection<User> searchUsers(String name, boolean isPro, int lastUserId, int numberOfUsers) throws SQLException {
        int ISPRO = isPro ? 1 : 0;
        Collection<User> users = new ArrayList<>();
        PreparedStatement searchUsers = conn.prepareStatement("select * from (select *  from sp_user where ispro = ? order by iduser) where lower(username) like ? and idUser > ? and rownum <= ?");
        searchUsers.setInt(1, ISPRO);
        searchUsers.setString(2, "%" + name + "%");
        searchUsers.setInt(3, lastUserId);
        searchUsers.setInt(4, numberOfUsers);
        ResultSet result = searchUsers.executeQuery();
        if (isPro) {
            while (result.next()) {
                users.add(new ProUser(result.getInt(1), result.getString(2), "", result.getString(4)));
            }
        } else {
            while (result.next()) {
                users.add(new User(result.getInt(1), result.getString(2), "", result.getString(4)));
            }
        }
        searchUsers.close();
        result.close();
        return users;
    }

    /**
     * get all Plans
     *
     * @param creatorID
     * @param name
     * @param lastPlanId
     * @param numberOfPlans
     * @return a collection of Plans
     * @throws java.sql.SQLException
     */
    public Collection<Plan> searchPlans(int creatorID, String name, int lastPlanId, int numberOfPlans) throws SQLException, Exception {
        PreparedStatement searchPlans;
        Collection<Plan> plans = new ArrayList<>();
        ResultSet result;
        if (creatorID <= 0 && name == null) {
            searchPlans = conn.prepareStatement("select * from sp_plan where idplan > ? and rownum <= ? order by idplan");
            searchPlans.setInt(1, lastPlanId);
            searchPlans.setInt(2, numberOfPlans);
            result = searchPlans.executeQuery();
            while (result.next()) {
                plans.add(new Plan(result.getInt(1), result.getInt(2), result.getString(3)));
            }
            result.close();
        } else if (creatorID > 0 && name == null) {
            searchPlans = conn.prepareStatement("select * from sp_plan where idCreator = ? and idplan > ? and rownum <= ? order by idplan");
            searchPlans.setInt(1, creatorID);
            searchPlans.setInt(2, lastPlanId);
            searchPlans.setInt(3, numberOfPlans);
            result = searchPlans.executeQuery();
            while (result.next()) {
                plans.add(new Plan(result.getInt(1), result.getInt(2), result.getString(3)));
            }
            result.close();
        } else if (creatorID <= 0 && name != null) {
            searchPlans = conn.prepareStatement("select * from sp_plan where lower(name) like ? and idplan > ? and rownum <= ? order by idplan");
            searchPlans.setString(1, "%" + name + "%");
            searchPlans.setInt(2, lastPlanId);
            searchPlans.setInt(3, numberOfPlans);
            result = searchPlans.executeQuery();
            while (result.next()) {
                plans.add(new Plan(result.getInt(1), result.getInt(2), result.getString(3)));
            }
            result.close();
        } else if (creatorID > 0 && name != null) {
            searchPlans = conn.prepareStatement("select * from sp_plan where idCreator = ? and lower(name) like ? and idplan > ? and rownum <= ? order by idplan");
            searchPlans.setInt(1, creatorID);
            searchPlans.setString(2, "%" + name + "%");
            searchPlans.setInt(3, lastPlanId);
            searchPlans.setInt(4, numberOfPlans);
            result = searchPlans.executeQuery();
            while (result.next()) {
                plans.add(new Plan(result.getInt(1), result.getInt(2), result.getString(3)));
            }
            result.close();
        } else {
            throw new Exception("Unknown Error");
        }
        searchPlans.close();
        return plans;
    }

    /**
     * gets the number of likes of a post
     *
     * @param postId
     * @return number of likes if successful, else -1
     * @throws java.sql.SQLException
     */
    public int getNumberOfLikes(int postId) throws SQLException {
        PreparedStatement getNumberOfLikes = conn.prepareStatement("select count(*) as numberOfLikes from sp_like where idpost = ?");
        getNumberOfLikes.setInt(1, postId);
        ResultSet result = getNumberOfLikes.executeQuery();
        result.next();
        
        int id = result.getInt(1);
        getNumberOfLikes.close();
        result.close();
        return id;
    }

    /**
     * returns if the specified User liked the specified Post
     *
     * @param userId
     * @param postId
     * @return true if the Post is liked by that User, else if not
     * @throws java.sql.SQLException
     */
    public boolean isLiked(int userId, int postId) throws SQLException {
        PreparedStatement isLiked = conn.prepareStatement("select case when ((select count(*) from sp_like where idPost = ? and idUser = ?) = 1) then 'true' else 'false' end as isLiked from dual");
        isLiked.setInt(1, postId);
        isLiked.setInt(2, userId);
        ResultSet result = isLiked.executeQuery();
        result.next();
        
        boolean liked = Boolean.valueOf(result.getString(1));
        isLiked.close();
        result.close();
        return liked;
    }

    /**
     * gets the next n number of Comments of a Post
     *
     * @param postID
     * @param lastCommentID
     * @param numberOfComments
     * @return a collection of Comments
     * @throws java.sql.SQLException
     */
    public Collection<Comment> getComments(int postID, int lastCommentID, int numberOfComments) throws SQLException, Exception {
        PreparedStatement getComments;
        Collection<Comment> comments = new ArrayList<>();
        if (lastCommentID < 0) {
            getComments = conn.prepareStatement("select * from sp_revComment where idpost = ? and rownum <= ?");
            getComments.setInt(1, postID);
            getComments.setInt(2, numberOfComments);
            ResultSet result = getComments.executeQuery();
            while (result.next()) {
                comments.add(new Comment(result.getInt(1), result.getInt(2), result.getInt(3), result.getString(4), result.getTimestamp(5).toLocalDateTime().toLocalDate()));
            }
            result.close();
        } else if (lastCommentID >= 0) {
            getComments = conn.prepareStatement("select * from sp_revComment where idpost = ? and idComment < ? and rownum <= ?");
            getComments.setInt(1, postID);
            getComments.setInt(2, lastCommentID);
            getComments.setInt(3, numberOfComments);
            ResultSet result = getComments.executeQuery();
            while (result.next()) {
                comments.add(new Comment(result.getInt(1), result.getInt(2), result.getInt(3), result.getString(4), result.getTimestamp(5).toLocalDateTime().toLocalDate()));
            }
            result.close();
        } else {
            throw new Exception("Unknown Error");
        }
        getComments.close();
        return comments;
    }

    /**
     * the Post is liked by the User
     *
     * @param userId
     * @param postId
     * @param likes
     * @return true if successful, false if failed
     * @throws java.sql.SQLException
     */
    public boolean setLike(int userId, int postId, boolean likes) throws SQLException, Exception {
        PreparedStatement setLike;
        if (likes) {
            setLike = conn.prepareStatement("insert into sp_like values(?, ?)");
            setLike.setInt(1, postId);
            setLike.setInt(2, userId);
            setLike.executeQuery();
            setLike.close();
            return true;
        }
        if (!likes) {
            setLike = conn.prepareStatement("delete from sp_like where idpost = ? and iduser = ?");
            setLike.setInt(1, postId);
            setLike.setInt(2, userId);
            setLike.executeQuery();
            setLike.close();
            return true;
        }
        throw new Exception("If that happens everythings going down");
    }

    /**
     * the Post is commented by the User
     *
     * @param userId
     * @param postId
     * @param text
     * @return true if successful, false if failed
     * @throws java.sql.SQLException
     */
    public boolean addComment(int userId, int postId, String text) throws SQLException {
        PreparedStatement addComment = conn.prepareStatement("insert into sp_comment values(seq_comment.nextVal, ?, ?, ?, systimestamp)");
        addComment.setInt(1, userId);
        addComment.setInt(2, postId);
        addComment.setString(3, text);
        addComment.executeQuery();
        addComment.close();
        return true;
    }

    /**
     * the User follows or unfollows the other User, depends on the
     * follow-parameter
     *
     * @param followerId
     * @param followsId
     * @param follow
     * @return the follow-state after change
     * @throws java.sql.SQLException
     */
    public boolean setUserFollow(int followerId, int followsId, boolean follow) throws SQLException {
        PreparedStatement followUser;
        if (follow) {
            followUser = conn.prepareStatement("insert into sp_follow values(?, ?)");
            followUser.setInt(1, followerId);
            followUser.setInt(2, followsId);
            followUser.executeQuery();
        } else {
            followUser = conn.prepareStatement("delete from sp_follow where idFollower = ? and idol = ?");
            followUser.setInt(1, followerId);
            followUser.setInt(2, followsId);
            followUser.executeQuery();
        }
        followUser.close();
        return true;
    }

    /**
     *
     * @param coordinates
     * @param radius
     * @param types
     * @return
     * @throws java.sql.SQLException
     */
    public Collection<Location> getNearbyLocations(Coordinate coordinates, double radius, ArrayList<LocationType> types) throws SQLException, Exception {
        Collection<Location> locations = new ArrayList<>();
        String query = "select * from sp_location where sqrt((lat - ?)*(lat - ?) + (lng - ?)*(lng  - ?)) <= ? and type in (";
        for (LocationType t : types) {
            query += "?,";
        }
        query = query.substring(0, query.length() - 1) + ")";
        PreparedStatement getNearbyLocations = conn.prepareStatement(query);
        getNearbyLocations.setDouble(1, coordinates.getLat());
        getNearbyLocations.setDouble(2, coordinates.getLat());
        getNearbyLocations.setDouble(3, coordinates.getLng());
        getNearbyLocations.setDouble(4, coordinates.getLng());
        getNearbyLocations.setDouble(5, radius);

        for (int idx = 0; idx < types.size(); idx++) {
            getNearbyLocations.setString(idx + 6, types.get(idx).toString());
        }
        ResultSet result = getNearbyLocations.executeQuery();
        while (result.next()) {
            Timestamp starttime = result.getTimestamp(7);
            Timestamp endtime = result.getTimestamp(8);
            if (starttime == null && endtime == null) {
                locations.add(new Location(result.getInt(1), result.getInt(2), result.getString(3), new Coordinate(result.getInt(4), result.getInt(5)), LocationType.valueOf(result.getString(6))));
            } else if (starttime != null && endtime != null) {
                locations.add(new Event(result.getInt(1), result.getInt(2), result.getString(3), new Coordinate(result.getInt(4), result.getInt(5)), LocationType.valueOf(result.getString(6)), starttime, endtime));
            } else {
                throw new Exception("Unknwon Error");
            }
        }
        getNearbyLocations.close();
        result.close();
        return locations;
    }

    /**
     *
     * @param userID
     * @param planID
     * @return
     * @throws java.sql.SQLException
     */
    public boolean isPlanSubscribed(int userID, int planID) throws SQLException {
        PreparedStatement isPlanSubscribed = conn.prepareStatement("select case when ((select count(*) from sp_subscription where idPlan = ? and idUser = ?) = 1) then 'true' else 'false' end as isSubscribed from dual");
        isPlanSubscribed.setInt(1, planID);
        isPlanSubscribed.setInt(2, userID);
        ResultSet result = isPlanSubscribed.executeQuery();
        result.next();
        
        boolean subbed = Boolean.valueOf(result.getString(1));
        isPlanSubscribed.close();
        result.close();
        return subbed;
    }

    /**
     *
     * @param userId
     * @return
     * @throws java.sql.SQLException
     */
    public Collection<Plan> getSubscribedPlans(int userId) throws SQLException {
        Collection<Plan> plans = new ArrayList<>();
        PreparedStatement getSubscribedPlans = conn.prepareStatement("select * from sp_plan where idPlan in (select idPlan from sp_subscription where idUser = ?)");
        getSubscribedPlans.setInt(1, userId);
        ResultSet result = getSubscribedPlans.executeQuery();
        while (result.next()) {
            plans.add(new Plan(result.getInt(1), result.getInt(2), result.getString(3)));
        }
        getSubscribedPlans.close();
        result.close();
        return plans;
    }

    /**
     *
     * @param userID
     * @param planID
     * @param subscribe
     * @return
     * @throws java.sql.SQLException
     */
    public boolean setPlanSubscription(int userID, int planID, boolean subscribe) throws SQLException, Exception {
        PreparedStatement subscribePlan;
        if (subscribe) {
            subscribePlan = conn.prepareStatement("insert into sp_subscription values(?, ?)");
            subscribePlan.setInt(1, planID);
            subscribePlan.setInt(2, userID);
            subscribePlan.executeQuery();
            subscribePlan.close();
            return true;
        }
        if (!subscribe) {
            subscribePlan = conn.prepareStatement("delete from sp_subscription where idPlan = ? and idUser = ?");
            subscribePlan.setInt(1, planID);
            subscribePlan.setInt(2, userID);
            subscribePlan.executeQuery();
            subscribePlan.close();
            return true;
        }
        throw new Exception("Unknown Error");
    }

    /**
     *
     * @param exerciseID
     * @return
     * @throws java.sql.SQLException
     */
    public Exercise getExercise(int exerciseID) throws SQLException {
        PreparedStatement getExercise = conn.prepareStatement("select * from sp_exercise where idExercise = ?");
        getExercise.setInt(1, exerciseID);
        ResultSet result = getExercise.executeQuery();
        result.next();
        
        Exercise e = new Exercise(result.getInt(1), result.getInt(2), result.getString(3), result.getString(4));
        getExercise.close();
        result.close();
        return e;
    }

    /**
     *
     * @param userID
     * @return
     * @throws java.sql.SQLException
     */
    public Collection<User> getFollowedUsers(int userID) throws SQLException {
        Collection<User> users = new ArrayList<>();
        PreparedStatement getFollowedUsers = conn.prepareStatement("select * from sp_user where idUser in (select idOl from sp_follow where idFollower = ?)");
        getFollowedUsers.setInt(1, userID);
        ResultSet result = getFollowedUsers.executeQuery();
        while (result.next()) {
            if (Boolean.valueOf(result.getString(5))) {
                users.add(new ProUser(result.getInt(1), result.getString(2), null, result.getString(4)));
            } else if (!Boolean.valueOf(result.getString(5))) {
                users.add(new User(result.getInt(1), result.getString(2), null, result.getString(4)));
            }
        }
        getFollowedUsers.close();
        result.close();
        return users;
    }

    /**
     *
     * @param userID
     * @param lastFollowerId
     * @param numberOfFollower
     * @return
     * @throws java.sql.SQLException
     */
    public Collection<User> getFollowers(int userID, int lastFollowerId, int numberOfFollower) throws SQLException {
        Collection<User> users = new ArrayList<>();
        PreparedStatement getFollowers = conn.prepareStatement("select * from sp_follow inner join sp_user on iduser = idfollower where idol = ? and idfollower > ? and rownum <= ? order by idfollower");
        //PreparedStatement getFollowers = conn.prepareStatement("select * from sp_user where idUser in (select idfollower from sp_follow where idol = ?)");
        getFollowers.setInt(1, userID);
        getFollowers.setInt(2, lastFollowerId);
        getFollowers.setInt(3, numberOfFollower);
        ResultSet result = getFollowers.executeQuery();
        while (result.next()) {
            if (Boolean.valueOf(result.getString(5))) {
                users.add(new ProUser(result.getInt(1), result.getString(2), result.getString(3), result.getString(4)));
            } else if (!Boolean.valueOf(result.getString(5))) {
                users.add(new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4)));
            }
        }
        getFollowers.close();
        result.close();
        return users;
    }

    /**
     *
     * @param followerID
     * @param followsID
     * @return
     * @throws java.sql.SQLException
     */
    public boolean isFollowing(int followerID, int followsID) throws SQLException {
        PreparedStatement isFollowing = conn.prepareStatement("select case when ((select count(*) from sp_follow where idFollower = ? and idOl = ?) = 1) then 'true' else 'false' end as isFollowing from dual");
        isFollowing.setInt(1, followerID);
        isFollowing.setInt(2, followsID);
        ResultSet result = isFollowing.executeQuery();
        result.next();
        
        boolean following = Boolean.valueOf(result.getString(1));
        isFollowing.close();
        result.close();
        return following;
    }
}
