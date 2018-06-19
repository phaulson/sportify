package com.spogss.sportifycommunity.data.connection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spogss.sportifycommunity.data.Comment;
import com.spogss.sportifycommunity.data.ProUser;
import com.spogss.sportifycommunity.data.connection.asynctasks.AddCommentTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.AddPostTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.ClientQueryListener;
import com.spogss.sportifycommunity.data.connection.asynctasks.ConnectTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.FollowUserTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.GetLocationsTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.GetNearbyLocationsTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.GetProfileTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.LoadCommentModelsActivity;
import com.spogss.sportifycommunity.data.connection.asynctasks.LoadDailyWorkoutsTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.LoadPlanModelsTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.LoadPostModelsTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.LoadWorkoutsTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.SearchPlansTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.SearchUsersTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.SetDescriptionTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.SetLikeTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.SubscribeTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.UserLoginTask;
import com.spogss.sportifycommunity.data.connection.asynctasks.UserRegisterTask;
import com.spogss.sportifycommunity.data.Coordinate;
import com.spogss.sportifycommunity.data.DailyWorkout;
import com.spogss.sportifycommunity.data.Exercise;
import com.spogss.sportifycommunity.data.Location;
import com.spogss.sportifycommunity.data.LocationType;
import com.spogss.sportifycommunity.data.Plan;
import com.spogss.sportifycommunity.data.Post;
import com.spogss.sportifycommunity.data.User;
import com.spogss.sportifycommunity.data.Workout;
import com.spogss.sportifycommunity.fragment.TabFragmentSearch;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

//import org.json.JSONObject;
/**
 * Created by Pauli on 09.04.2018.
 */

public class SportifyClient {
    //TODO: call webservice in every method

    private static SportifyClient client;
    private User currentUser;
    private int numberOfPosts;
    private int numberOfUsers;
    private int numberOfPlans;
    private int numberOfComments;
    private boolean isPro;
    private final Gson GSON = new Gson();


    private static URL url;

    /**
     * the ConnectTask calls newInstance for the first time
     * @param listener
     */
    public static void connectAsync(ClientQueryListener listener) {
        ConnectTask t = new ConnectTask();
        t.setListener(listener);
        t.execute();
    }
    /**
     * singleton for SportifyClient
     * @return the static SportifyClient
     */
    public static SportifyClient newInstance(){
        if (client == null) {
            try {
                URL url = new URL("http", "192.168.142.188", 8080, "SportifyWebService/webresources");
                //URL url = new URL("http", "10.0.0.8", 8080, "SportifyWebservice/webresources");
                client = new SportifyClient(url);
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return client;
    }
    private SportifyClient(URL url){
        this.url = url;
        this.isPro = false;
    }

    private String get(HttpMethod httpMethod, String route, String... params) throws Exception {
        ControllerSync controller = new ControllerSync(url);

        ArrayList<String> connectionParams = new ArrayList<>();
        connectionParams.add(httpMethod.toString());
        connectionParams.add(route);
        connectionParams.addAll(Arrays.asList(params));
        params = connectionParams.toArray(params);

        return controller.getJSONString(params);
    }

    public int getCurrentUserID() {
        return currentUser.getId();
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public int getNumberOfPosts() {
        return numberOfPosts;
    }
    public void setNumberOfPosts(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }
    public int getNumberOfUsers() {
        return numberOfUsers;
    }
    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }
    public int getNumberOfPlans() {
        return numberOfPlans;
    }
    public void setNumberOfPlans(int numberOfPlans) {
        this.numberOfPlans = numberOfPlans;
    }
    public int getNumberOfComments() {
        return numberOfComments;
    }
    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    /**
     * checks if login is okay and returns the userID
     * @param username
     * @param password
     * @return userId if successful, else -1
     */
    public int login(String username, String password) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("isPro", isPro);

            currentUser = GSON.fromJson(get(HttpMethod.POST, "login", jsonObject.toString()), User.class);
            //currentUser = manager.login(username, password);
            return getCurrentUserID();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * if the username is not in use, a new User is added to the database
     * @param username
     * @param password
     * @param isPro
     * @return userID if successful, else -1
     */
    public int register(String username, String password, boolean isPro){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("isPro", isPro);

            currentUser = GSON.fromJson(get(HttpMethod.POST, "register", jsonObject.toString()), User.class);
            //currentUser = manager.register(username, password, isPro);
            return getCurrentUserID();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * returns the User specified with this userID
     * @param idUser
     * @return the User
     */
    public User getProfile(int idUser){
        try {
            boolean isPro = GSON.fromJson(get(HttpMethod.POST, "isPro", idUser + ""), Boolean.class);

            if(isPro)
                return GSON.fromJson(get(HttpMethod.POST, "getProfile", idUser + ""), ProUser.class);
            else
                return GSON.fromJson(get(HttpMethod.POST, "getProfile", idUser + ""), User.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * changes the description of a User specified with this userID
     * @param idUser
     * @param newDescription
     * @return true if successful, false if failed
     */
    public boolean changeDescription(int idUser, String newDescription){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userID", idUser);
            jsonObject.put("description", newDescription);

            return GSON.fromJson(get(HttpMethod.POST, "changeDescription", jsonObject.toString()), Boolean.class);
            //return manager.changeDescription(idUser, newDescription);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * gets all Plans created by the ProUser specified with this userID
     * @param idUser
     * @return a collection of Plans
     */
    public Collection<Plan> getPlans(int idUser){
        try {
            return GSON.fromJson(get(HttpMethod.POST, "getPlans", idUser + ""), new TypeToken<Collection<Plan>>(){}.getType());
            //return manager.getPlans(idUser);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * gets all DailyWorkouts of a Plan
     * @param idPlan
     * @return a collection of DailyWorkouts
     */
    public Collection<DailyWorkout> getDailyWorkouts(int idPlan){
        try {
            return GSON.fromJson(get(HttpMethod.POST, "getDailyWorkouts", idPlan + ""), new TypeToken<Collection<DailyWorkout>>(){}.getType());
            //return manager.getDailyWorkouts(idPlan);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * gets all Workouts of a DailyWorkout
     * @param idDailyWorkout
     * @return a collection of Workouts
     */
    public Collection<Workout> getWorkouts(int idDailyWorkout){
        try {
            return GSON.fromJson(get(HttpMethod.POST, "getWorkouts", idDailyWorkout + ""), new TypeToken<Collection<Workout>>(){}.getType());
            //return manager.getWorkouts(idDailyWorkout);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * gets all Exercises of a Workout
     * @param idWorkout
     * @return a collection of Exercises
     */
    public Collection<Exercise> getExercises(int idWorkout){
        try {
            return GSON.fromJson(get(HttpMethod.POST, "getExercises", idWorkout + ""), new TypeToken<Collection<Exercise>>(){}.getType());
            //return manager.getExercises(idWorkout);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * gets all Locations and Events created by a ProUser specified with this userID
     * @param idUser
     * @return a collection of Locations
     */
    public Collection<Location> getLocations(int idUser){
        try {
            return GSON.fromJson(get(HttpMethod.POST, "getLocations", idUser + ""), new TypeToken<Collection<Location>>(){}.getType());
            //return manager.getLocations(idUser);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * gets the next n posts created by the User specified with the creatorID, starting with a specified Post
     * @param idCreator
     * @param lastPostId
     * @return a collection of Posts
     */
    public Collection<Post> getPostsByCreator(int idCreator, int lastPostId){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("creatorID", idCreator);
            jsonObject.put("lastPostID", lastPostId);
            jsonObject.put("numberOfPosts", numberOfPosts);

            return GSON.fromJson(get(HttpMethod.POST, "getPostsByCreator", jsonObject.toString()), new TypeToken<Collection<Post>>(){}.getType());
            //return manager.getPostsByCreator(idCreator, lastPostId, numberOfPosts);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * gets the next n posts created by the User specified with the creatorID
     * @param idCreator
     * @return a collection of Posts
     */
    public Collection<Post> getPostsByCreator(int idCreator){
        return getPostsByCreator(idCreator, -1);
    }

    /**
     * adds a new Location and returns its id. if startDate and endDate are specified, the type must be EVENT or OTHER.
     * if startDate and endDate are not specified, the TYP must not be EVENT
     * @param idUser
     * @param coordinates
     * @param name
     * @param type
     * @param startDate
     * @param endDate
     * @return locationID if successful, else -1
     */
    public int addLocation(int idUser, Coordinate coordinates, String name, LocationType type, Date startDate, Date endDate){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userID", idUser);
            jsonObject.put("coordinates", coordinates);
            jsonObject.put("name", name);
            jsonObject.put("type", type);
            jsonObject.put("startdate", startDate);
            jsonObject.put("enddate", endDate);

            return GSON.fromJson(get(HttpMethod.POST, "addLocation", jsonObject.toString()), Integer.class);
            //return manager.addLocation(idUser, coordinates, name, type, startDate, endDate);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * adds a new Plan and returns its id
     * @param idCreator
     * @param name
     * @return planID if successful, else -1
     */
    public int addPlan(int idCreator, String name){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("creatorID", idCreator);
            jsonObject.put("name", name);

            return GSON.fromJson(get(HttpMethod.POST, "addPlan", jsonObject.toString()), Integer.class);
            //return manager.addPlan(idCreator, name);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * links DailyWorkouts to a Plan
     * @param planId
     * @param dailyWorkouts
     * @return true if successful, false if failed
     */
    public boolean linkDailyWorkouts(int planId, Collection<Integer> dailyWorkouts){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("planID", planId);
            jsonObject.put("dailyWorkouts", dailyWorkouts);

            return GSON.fromJson(get(HttpMethod.POST, "linkDailyWorkouts", jsonObject.toString()), Boolean.class);
            //return manager.linkDailyWorkouts(planId, dailyWorkouts);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * adds a new DailyWorkout and returns its id
     * @param creatorId
     * @param name
     * @return dailyWorkoutID if successful, else -1
     */
    public int addDailyWorkout(int creatorId, String name){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("creatorID", creatorId);
            jsonObject.put("name", name);

            return GSON.fromJson(get(HttpMethod.POST, "addDailyWorkout", jsonObject.toString()), Integer.class);
            //return manager.addDailyWorkout(creatorId, name);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * links Workouts to a DailyWorkout
     * @param dailyWorkoutId
     * @param workouts
     * @return true if successful, false when failed
     */
    public boolean linkWorkouts(int dailyWorkoutId, Collection<Integer> workouts){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dailyWorkoutID", dailyWorkoutId);
            jsonObject.put("workouts", workouts);

            return GSON.fromJson(get(HttpMethod.POST, "linkWorkouts", jsonObject.toString()), Boolean.class);
            //return manager.linkWorkouts(planId, workouts);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * adds a new Workout and returns its id
     * @param creatorId
     * @param name
     * @return workoutID if successful, else -1
     */
    public int addWorkout(int creatorId, String name){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("creatorID", creatorId);
            jsonObject.put("name", name);

            return GSON.fromJson(get(HttpMethod.POST, "addWorkout", jsonObject.toString()), Integer.class);
            //return manager.addWorkout(creatorId, name);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * links Exercises to  Workout
     * @param workoutId
     * @param exercises
     * @return true if successful, false if failed
     */
    public boolean linkExercises(int workoutId, Collection<Integer> exercises){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("workoutID", workoutId);
            jsonObject.put("exercises", exercises);

            return GSON.fromJson(get(HttpMethod.POST, "linkExercises", jsonObject.toString()), Boolean.class);
            //return manager.linkExercises(workoutId, exercises);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * adds a new Exercise nd returns its id
     * @param name
     * @param description
     * @param creatorId
     * @return exerciseID if successful, else -1
     */
    public int addExerise(int creatorId, String name, String description) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("creatorID", creatorId);
            jsonObject.put("name", name);
            jsonObject.put("description", description);

            return GSON.fromJson(get(HttpMethod.POST, "addExercise", jsonObject.toString()), Integer.class);
            //return manager.addExercise(name, description, creatorId);
        } catch (Exception e) {
            return -1;
        }
    }


    /**
     * get all DailyWorkouts
     * @return a collection of DailyWorkouts
     */
    public Collection<DailyWorkout> searchDailyWorkouts() {
        return searchDailyWorkouts(-1, null);
    }
    /**
     * get all DailyWorkouts with this creatorID
     * @param creatorID
     * @return a collection of DailyWorkouts
     */
    public Collection<DailyWorkout> searchDailyWorkouts(int creatorID) {
        return searchDailyWorkouts(creatorID, null);
    }
    /**
     * get all DailyWorkouts that contain that name
     * @param name
     * @return a collection of DailyWorkouts
     */
    public Collection<DailyWorkout> searchDailyWorkouts(String name) {
        return searchDailyWorkouts(-1, name.toLowerCase());
    }
    /**
     * get all DailyWorkouts with that creatorID and that contain that name
     * @param creatorID
     * @param name
     * @return a collection of DailyWorkouts
     */
    public Collection<DailyWorkout> searchDailyWorkouts(int creatorID, String name) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("creatorID", creatorID);
            jsonObject.put("name", name);

            return GSON.fromJson(get(HttpMethod.POST, "searchDailyWorkouts", jsonObject.toString()), new TypeToken<Collection<DailyWorkout>>(){}.getType());
            //return manager.searchDailyWorkouts(creatorID, name.toLowerCase());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * get all Workouts
     * @return a collection of Workouts
     */
    public Collection<Workout> searchWorkouts() {
        return searchWorkouts(-1, null);
    }
    /**
     * get all Workouts with this creatorID
     * @param creatorID
     * @return a collection of Workouts
     */
    public Collection<Workout> searchWorkouts(int creatorID) {
        return searchWorkouts(creatorID, null);
    }
    /**
     * get all Workouts that contain that name
     * @param name
     * @return a collection of Workouts
     */
    public Collection<Workout> searchWorkouts(String name) {
        return searchWorkouts(-1, name.toLowerCase());
    }
    /**
     * get all Workouts with that creatorID and that contain that name
     * @param creatorID
     * @param name
     * @return a collection of Workouts
     */
    public Collection<Workout> searchWorkouts(int creatorID, String name) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("creatorID", creatorID);
            jsonObject.put("name", name);

            return GSON.fromJson(get(HttpMethod.POST, "searchWorkouts", jsonObject.toString()), new TypeToken<Collection<Workout>>(){}.getType());
            //return manager.searchWorkouts(creatorID, name.toLowerCase());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * get all Exercises
     * @return a collection of Exercises
     */
    public Collection<Exercise> searchExercises() {
        return searchExercises(-1, null);
    }
    /**
     * get all Exercises with this creatorID
     * @param creatorID
     * @return a collection of Exercises
     */
    public Collection<Exercise> searchExercises(int creatorID) {
        return searchExercises(creatorID, null);    }
    /**
     * get all Exercises that contain that name
     * @param name
     * @return a collection of Exercises
     */
    public Collection<Exercise> searchExercises(String name) {
        return searchExercises(-1, name.toLowerCase());
    }
    /**
     * get all Exercises with that creatorID and that contain that name
     * @param creatorId
     * @param name
     * @return a collection of Exercises
     */
    public Collection<Exercise> searchExercises(int creatorId, String name) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("creatorID", creatorId);
            jsonObject.put("name", name);

            return GSON.fromJson(get(HttpMethod.POST, "searchExercises", jsonObject.toString()), new TypeToken<Collection<Exercise>>(){}.getType());
            //return manager.searchExercises(creatorId, name.toLowerCase());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * add a new Post and return its ID
     * @param creatorId
     * @param caption
     * @return postID if successful, else -1
     */
    public int addPost(int creatorId, String caption) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("creatorID", creatorId);
            jsonObject.put("caption", caption);

            return GSON.fromJson(get(HttpMethod.POST, "addPost", jsonObject.toString()), Integer.class);
            //return manager.addPost(creatorId, caption);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * gets the next n number of Posts that creators are subscribed by the specified user
     * @param userId
     * @return a collection of Posts
     */
    public Collection<Post> getPosts(int userId) {
        return getPosts(userId, -1);
    }
    /**
     * gets the next n number of Posts that creators are subscribed by the specified user, starting with a specified postID
     * @param userId
     * @param lastPostId
     * @return a collection of Posts
     */
    public Collection<Post> getPosts(int userId, int lastPostId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userID", userId);
            jsonObject.put("lastPostID", lastPostId);
            jsonObject.put("numberOfPosts", numberOfPosts);

            return GSON.fromJson(get(HttpMethod.POST, "getPosts", jsonObject.toString()), new TypeToken<Collection<Post>>(){}.getType());
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * gets all Users that contain that name and their pro-attribute is equivalent to the pro-parameter
     * @param name
     * @param pro
     * @return a collection of Users
     */
    public Collection<User> searchUsers(String name, boolean pro, int lastUserID) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("isPro", pro);
            jsonObject.put("lastUserId", lastUserID);
            jsonObject.put("numberOfUsers", numberOfUsers);

            return GSON.fromJson(get(HttpMethod.POST, "searchUsers", jsonObject.toString()), new TypeToken<Collection<User>>(){}.getType());
            //return manager.searchUsers(name.toLowerCase(), pro);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * get all Plans
     * @return a collection of Plans
     */
    public Collection<Plan> searchPlans(int lastUserId) {
        return searchPlans(-1, null, lastUserId);
    }
    /**
     * get all Plans with this creatorID
     * @param creatorID
     * @return a collection of Plans
     */
    public Collection<Plan> searchPlans(int creatorID, int lastPlanId) {
        return searchPlans(creatorID, null, lastPlanId);
    }
    /**
     * get all Plans that contain that name
     * @param name
     * @return a collection of Plans
     */
    public Collection<Plan> searchPlans(String name, int lastPlanId) {
        return searchPlans(-1, name.toLowerCase(), lastPlanId);
    }
    /**
     * get all Plans with that creatorID and that contain that name
     * @param creatorID
     * @param name
     * @return a collection of Plans
     */
    public Collection<Plan> searchPlans(int creatorID, String name, int lastPlanId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("creatorID", creatorID);
            jsonObject.put("name", name);
            jsonObject.put("lastPlanId", lastPlanId);
            jsonObject.put("numberOfPlans", numberOfPlans);

            return GSON.fromJson(get(HttpMethod.POST, "searchPlans", jsonObject.toString()), new TypeToken<Collection<Plan>>(){}.getType());
            //return manager.searchUsers(name.toLowerCase(), pro);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * gets the number of likes of a post
     * @param postId
     * @return number of likes if successful, else -1
     */
    public int getNumberOfLikes(int postId) {
        try {
            return GSON.fromJson(get(HttpMethod.POST, "getNumberOfLikes", postId + ""), Integer.class);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * returns if the specified User liked the specified Post
     * @param userId
     * @param postId
     * @return true if the Post is liked by that User, else if not
     */
    public boolean isLiked(int userId, int postId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userID", userId);
            jsonObject.put("postID", postId);

            return GSON.fromJson(get(HttpMethod.POST, "isLiked", jsonObject.toString()), Boolean.class);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * gets the next n number of Comments of a Post
     * @param postID
     * @return a collection of Comments
     */
    public Collection<Comment> getComments(int postID) {
        return getComments(postID, -1);
    }

    /**
     * gets the next n number of Comments of a Post, starting at a specified Comment
     * @param postID
     * @param lastCommentId
     * @return a collection of Comments
     */
    public Collection<Comment> getComments(int postID, int lastCommentId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("postID", postID);
            jsonObject.put("lastCommentID", lastCommentId);
            jsonObject.put("numberOfComments", numberOfComments);

            return GSON.fromJson(get(HttpMethod.POST, "getComments", jsonObject.toString()), new TypeToken<Collection<Comment>>(){}.getType());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * the Post is liked by the User
     * @param userId
     * @param postId
     * @return true if successful, false if failed
     */
    public boolean setLike(int userId, int postId, boolean likes) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userID", userId);
            jsonObject.put("postID", postId);
            jsonObject.put("likes", likes);

            return GSON.fromJson(get(HttpMethod.POST, "setLike", jsonObject.toString()), Boolean.class);
            //return manager.setLike(userId, postId, likes);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * the Post is commented by the User
     * @param userId
     * @param postId
     * @param text
     * @return true if successful, false if failed
     */
    public int addComment(int userId, int postId, String text) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userID", userId);
            jsonObject.put("postID", postId);
            jsonObject.put("text", text);

            return GSON.fromJson(get(HttpMethod.POST, "addComment", jsonObject.toString()), Integer.class);
            //return manager.addComment(userId, postId, text);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * the User follows or unfollows the other User, depends on the follow-parameter
     * @param followerId
     * @param followsId
     * @param follow
     * @return the follow-state after change
     */
    public boolean setUserFollow(int followerId, int followsId, boolean follow) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("followerID", followerId);
            jsonObject.put("followsID", followsId);
            jsonObject.put("follow", follow);

            return GSON.fromJson(get(HttpMethod.POST, "setUserFollow", jsonObject.toString()), Boolean.class);
            //return manager.setUserFollow(followerId, followsId, follow);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * gets all Locations within a radius, starting at a specified point
     * @param coordinate
     * @param radius
     * @param types
     * @return a collection of Locations
     */
    public Collection<Location> getNearbyLocations(Coordinate coordinate, double radius, Collection<LocationType> types) {
        try {
            JSONObject jsonCoordinate = new JSONObject();
            jsonCoordinate.put("lat",coordinate.getLat());
            jsonCoordinate.put("lng",coordinate.getLng());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("coordinates", jsonCoordinate);
            jsonObject.put("radius", radius);
            //jsonObject.put("types", types);

            return GSON.fromJson(get(HttpMethod.POST, "getNearbyLocations", jsonObject.toString()), new TypeToken<Collection<Location>>(){}.getType());
            //return manager.getNearbyLocations(coordinate, radius, (ArrayList<LocationType>) types);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * gets all Plans the User has subscribed
     * @param userId
     * @return a collection Plans
     */
    public Collection<Plan> getSubscribedPlans(int userId) {
        try {
            return GSON.fromJson(get(HttpMethod.POST, "getSubscribedPlans", userId + ""), new TypeToken<Collection<Plan>>(){}.getType());
            //return manager.getSubscribedPlans(userId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * shows if a user has subscribed a plan
     * @param userId
     * @param planId
     * @return true if the plan is subscribed, false if not
     */
    public boolean isPlanSubscribed(int userId, int planId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userID", userId);
            jsonObject.put("planID", planId);

            return GSON.fromJson(get(HttpMethod.POST, "isPlanSubscribed", jsonObject.toString()), Boolean.class);
            //return manager.isPlanSubscribed(userId, planId);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * the User subscribes or unsubscribes the Plan, depends on the subscribe-parameter
     * @param planId
     * @param userId
     * @param subscribe
     * @return the subscription-state after change
     */
    public boolean setPlanSubscription(int planId, int userId, boolean subscribe) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userID", userId);
            jsonObject.put("planID", planId);
            jsonObject.put("subscribe", subscribe);

            return GSON.fromJson(get(HttpMethod.POST, "setPlanSubscription", jsonObject.toString()), Boolean.class);
            //return manager.setPlanSubscription(userId, planId, subscribe);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * gets the exercise with the id idExercise
     * @param idExercise
     * @return if found -> exercise else null
     */
    public Exercise getExercise(int idExercise){
        try {
            return GSON.fromJson(get(HttpMethod.POST, "getExercise", idExercise + ""), Exercise.class);
            //return manager.getExercise(idExercise);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * gets all User that the user with the id idUser follows
     * @param idUser
     * @return a collection of users
     */
    public Collection<User> getUsersIFollow(int idUser){
        try {
            return GSON.fromJson(get(HttpMethod.POST, "getFollowedUsers", idUser + ""), new TypeToken<Collection<User>>(){}.getType());
            //return manager.getFollowedUsers(idUser);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * gets all users that follow the user with the id idUser
     * @param idUser
     * @return a collection of user
     */
    public Collection<User> getUsersThatFollowMe(int idUser){
        try {
            return GSON.fromJson(get(HttpMethod.POST, "getFollowers", idUser + ""), new TypeToken<Collection<User>>(){}.getType());
            //return manager.getFollowers(idUser);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * shows if a user follows another user
     * @param idFollower
     * @param idFollows
     * @return true if user follows the other user and false if not
     */
    public boolean isFollowing(int idFollower, int idFollows){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("followerID", idFollower);
            jsonObject.put("followsID", idFollows);

            return GSON.fromJson(get(HttpMethod.POST, "isFollowing", jsonObject.toString()), Boolean.class);
            //return manager.isFollowing(idFollower, idFollows);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * get the number of users that follow the user with this userId
     * @param userid
     * @return number of followers
     */
    public int getNumberOfFollowers(int userid) {
        try {
            return GSON.fromJson(get(HttpMethod.POST, "getFollowersCount", userid + ""), Integer.class);
            //return manager.getFollowersCount(userid);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * get the number of users that the user with this userId follows
     * @param userid
     * @return number of followed users
     */
    public int getNumberOfFollowedUsers(int userid) {
        try {
            return GSON.fromJson(get(HttpMethod.POST, "getFollowedUsersCount", userid + ""), Integer.class);
            //return manager.getFollowedUsersCount(userid);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * return the number of users that are subscribed to the plan with this planID
     * @param planid
     * @return number of subscribers
     */
    public int getNumberOfSubscribers(int planid) {
        try {
            return GSON.fromJson(get(HttpMethod.POST, "getSubscribersCount", planid + ""), Integer.class);
            //return manager.getSubscribersCount(planid);
        } catch (Exception e) {
            return -1;
        }
    }


    //AsyncMethods
    /**
     * calls listener with (userid, username, password)
     * @param un
     * @param pw
     * @param listener
     *
     */
    public void loginAsync(String un, String pw, ClientQueryListener listener){
        UserLoginTask t = new UserLoginTask(un, pw);
        t.setListener(listener);
        t.execute();
    }

    /**
     * calls listener with (id, un, pw)
     * @param un
     * @param pw
     * @param listener
     *
     */
    public void registerAsync(String un, String pw, ClientQueryListener listener){
        UserRegisterTask t = new UserRegisterTask(un, pw);
        t.setListener(listener);
        t.execute();
    }

    /**
     * calls listener with (userId, lastPostId, byCreator)
     * @param userId
     * @param lastPostId
     * @param byCreator
     * @param listener
     */
    public void getPostsAsync(int userId, int lastPostId, boolean byCreator, ClientQueryListener listener){
        LoadPostModelsTask t = new LoadPostModelsTask(userId, lastPostId, byCreator);
        t.setListener(listener);
        t.execute();
    }

    /**
     * calls listener with (text, fragment, isPro)
     * @param text
     * @param fragment
     * @param isPro
     * @param listener
     */
    public void searchUsersAsync(String text, TabFragmentSearch fragment, boolean isPro, ClientQueryListener listener){
        SearchUsersTask t = new SearchUsersTask(text, fragment, isPro);
        t.setListener(listener);
        t.execute();
    }

    /**
     * calls listener with (text, fragment)
     * @param text
     * @param fragment
     * @param listener
     */
    public void searchPlansAsync(String text, TabFragmentSearch fragment, ClientQueryListener listener){
        SearchPlansTask t = new SearchPlansTask(text, fragment);
        t.setListener(listener);
        t.execute();
    }

    /**
     * calls listener with (postId, like)
     * @param post
     * @param like
     * @param listener
     */
    public void setLikeAsync(Post post, boolean like, ClientQueryListener listener){
        SetLikeTask t = new SetLikeTask(post, like);
        t.setListener(listener);
        t.execute();
    }

    /**
     * calls listener with (planId, subscribe)
     * @param planId
     * @param subscribe
     * @param listener
     */
    public void setPlanSubscriptionAsync(int planId, boolean subscribe, ClientQueryListener listener){
        SubscribeTask t = new SubscribeTask(planId, subscribe);
        t.setListener(listener);
        t.execute();
    }

    /**
     * calls listener with (description)
     * @param description
     * @param listener
     */
    public void changeDescriptionAsync(String description, ClientQueryListener listener){
        SetDescriptionTask t = new SetDescriptionTask(description);
        t.setListener(listener);
        t.execute();
    }

    /**
     * calls listener with (postId)
     * @param postId
     * @param listener
     */
    public void getDailyWorkoutsAsync(int postId, ClientQueryListener listener){
        LoadDailyWorkoutsTask t = new LoadDailyWorkoutsTask(postId);
        t.setListener(listener);
        t.execute();
    }

    /**
     * calls listener with (caption)
     * @param caption
     * @param listener
     */
    public void addPostAsync(String caption, ClientQueryListener listener){
        AddPostTask t = new AddPostTask(caption);
        t.setListener(listener);
        t.execute();
    }

    /**
     * calls listener with (userId)
     * @param userId
     * @param listener
     */
    public void getProfileAsync(int userId, ClientQueryListener listener){
        GetProfileTask t = new GetProfileTask(userId);
        t.setListener(listener);
        t.execute();
    }

    /**
     * calls listener with (followsId, follow)
     * @param followsId
     * @param follow
     * @param listener
     */
    public void setUserFollowAsync(int followsId, boolean follow, ClientQueryListener listener){
        FollowUserTask t = new FollowUserTask(followsId, follow);
        t.setListener(listener);
        t.execute();
    }

    /**
     * calls listener with (userId)
     * @param userId
     * @param listener
     */
    public void getPlansAsync(int userId, ClientQueryListener listener){
        LoadPlanModelsTask t = new LoadPlanModelsTask(userId);
        t.setListener(listener);
        t.execute();
    }

    /**
     * calls listener with (dailyWorkoutId)
     * @param dailyWorkoutId
     * @param listener
     */
    public void getWorkoutsAsync(int dailyWorkoutId, ClientQueryListener listener){
        LoadWorkoutsTask t = new LoadWorkoutsTask(dailyWorkoutId);
        t.setListener(listener);
        t.execute();
    }

    public void getNearbyLocationsAsync(Coordinate position,double radius,Collection<LocationType> types, ClientQueryListener listener){
        GetNearbyLocationsTask t = new GetNearbyLocationsTask(position,radius,types);
        t.setListener(listener);
        t.execute();
    }

    public void getLocationsAsync(int userid, ClientQueryListener listener){
        GetLocationsTask t = new GetLocationsTask(userid);
        t.setListener(listener);
        t.execute();
    }

    public void getCommentsAsync(int postId, int lastCommentId, ClientQueryListener listener){
        LoadCommentModelsActivity t = new LoadCommentModelsActivity(postId, lastCommentId);
        t.setListener(listener);
        t.execute();
    }

    public void addCommentAsync(int postId, String text, ClientQueryListener listener){
        AddCommentTask t = new AddCommentTask(postId, text);
        t.setListener(listener);
        t.execute();
    }
}
