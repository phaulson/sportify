package com.spogss.sportifycommunity.data;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Created by Pauli on 09.04.2018.
 */

public class SportifyClient {
    private static SportifyClient client = new SportifyClient();
    private int currentUserID;
    private int numberOfPosts;

    /**
     * singleton for SportifyClient
     * @return the static SportifyClient
     */
    public static SportifyClient newInstance(){
        return client;
    }
    private SportifyClient(){
    }

    public int getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(int currentUserID) {
        this.currentUserID = currentUserID;
    }

    public int getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }

    /**
     * checks if login is okay and returns the userID
     * @param username
     * @param password
     * @return userId if successful, else -1
     */
    public int login(String username, String password) {
        return 0;
    }

    /**
     * if the username is not in use, a new User is added to the database
     * @param username
     * @param password
     * @return userID if successful, else -1
     */
    public int register(String username, String password){
        return 0;
    }

    /**
     * returns the User specified with this userID
     * @param idUser
     * @return the User
     */
    public User getProfile(int idUser){
        return null;
    }

    /**
     * changes the description of a User specified with this userID
     * @param idUser
     * @param newDescription
     * @return true if successful, false if failed
     */
    public boolean changeDescription(int idUser, String newDescription){
        return false;
    }

    /**
     * gets all Plans created by the ProUser specified with this userID
     * @param idUser
     * @return a collection of Plans
     */
    public Collection<Plan> getPlans(int idUser){
        return null;
    }

    /**
     * gets all DailyWorkouts of a Plan
     * @param idPlan
     * @return a collection of DailyWorkouts
     */
    public Collection<DailyWorkout> getDailyWorkouts(int idPlan){
        return null;
    }

    /**
     * gets all Workouts of a DailyWorkout
     * @param idDailyWorkout
     * @return a collection of Workouts
     */
    public Collection<Workout> getWorkouts(int idDailyWorkout){
        return null;
    }

    /**
     * gets all Exercises of a Workout
     * @param idWorkout
     * @return a collection of Exercises
     */
    public Collection<Exercise> getExercises(int idWorkout){
        return null;
    }

    /**
     * gets all Locations and Events created by a ProUser specified with this userID
     * @param idUser
     * @return a collection of Locations
     */
    public Collection<Location> getLocations(int idUser){
        return null;
    }

    /**
     * gets the next n posts created by the User specified with the creatorID, starting with a specified Post
     * @param idCreator
     * @param lastPostId
     * @return a collection of Posts
     */
    public Collection<Post> getPostsByCreator(int idCreator, int lastPostId){
        return null;
    }
    /**
     * gets the next n posts created by the User specified with the creatorID
     * @param idCreator
     * @return a collection of Posts
     */
    public Collection<Post> getPostsByCreator(int idCreator){
        return getPostsByCreator(idCreator, 0);
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
    public int addLocation(int idUser, Coordinate coordinates, String name, LocationType type, LocalDate startDate, LocalDate endDate){
        return 0;
    }

    /**
     * adds a new Plan and returns its id
     * @param idCreator
     * @param name
     * @return planID if successful, else -1
     */
    public int addPlan(int idCreator, String name){
        return 0;
    }

    /**
     * links DailyWorkouts to a Plan
     * @param planId
     * @param dailyWorkouts
     * @return true if successful, false if failed
     */
    public boolean linkDailyWorkouts(int planId, Collection<Integer> dailyWorkouts){
        return false;
    }

    /**
     * adds a new DailyWorkout and returns its id
     * @param creatorId
     * @param name
     * @return dailyWorkoutID if successful, else -1
     */
    public int addDailyWorkout(int creatorId, String name){
        return 0;
    }

    /**
     * links Workouts to a DailyWorkout
     * @param planId
     * @param workouts
     * @return true if successful, false when failed
     */
    public boolean linkWorkouts(int planId, Collection<Integer> workouts){
        return false;
    }

    /**
     * adds a new Workout and returns its id
     * @param creatorId
     * @param name
     * @return workoutID if successful, else -1
     */
    public int addWorkout(int creatorId, String name){
        return 0;
    }

    /**
     * links Exercises to  Workout
     * @param workoutId
     * @param exercises
     * @return true if successful, false if failed
     */
    public boolean linkExercises(int workoutId, Collection<Integer> exercises){
        return false;
    }

    /**
     * adds a new Exercise nd returns its id
     * @param name
     * @param description
     * @param creatorId
     * @return exerciseID if successful, else -1
     */
    public int addExerise(String name, String description, int creatorId) {
        return 0;
    }


    /**
     * get all DailyWorkouts
     * @return a collection of DailyWorkouts
     */
    public Collection<DailyWorkout> searchDailyWorkouts() {
        return null;
    }
    /**
     * get all DailyWorkouts with this creatorID
     * @param creatorID
     * @return a collection of DailyWorkouts
     */
    public Collection<DailyWorkout> searchDailyWorkouts(int creatorID) {
        return null;
    }
    /**
     * get all DailyWorkouts that contain that name
     * @param name
     * @return a collection of DailyWorkouts
     */
    public Collection<DailyWorkout> searchDailyWorkouts(String name) {
        return null;
    }
    /**
     * get all DailyWorkouts with that creatorID and that contain that name
     * @param creatorID
     * @param name
     * @return a collection of DailyWorkouts
     */
    public Collection<DailyWorkout> searchDailyWorkouts(int creatorID, String name) {
        return null;
    }

    /**
     * get all Workouts
     * @return a collection of Workouts
     */
    public Collection<Workout> searchWorkouts() {
        return null;
    }
    /**
     * get all Workouts with this creatorID
     * @param creatorID
     * @return a collection of Workouts
     */
    public Collection<Workout> searchWorkouts(int creatorID) {
        return null;
    }
    /**
     * get all Workouts that contain that name
     * @param name
     * @return a collection of Workouts
     */
    public Collection<Workout> searchWorkouts(String name) {
        return null;
    }
    /**
     * get all Workouts with that creatorID and that contain that name
     * @param creatorID
     * @param name
     * @return a collection of Workouts
     */
    public Collection<Workout> searchWorkouts(int creatorID, String name) {
        return null;
    }

    /**
     * get all Exercises
     * @return a collection of Exercises
     */
    public Collection<Exercise> searchExercises() {
        return null;
    }
    /**
     * get all Exercises with this creatorID
     * @param creatorID
     * @return a collection of Exercises
     */
    public Collection<Exercise> searchExercises(int creatorID) {
        return null;
    }
    /**
     * get all Exercises that contain that name
     * @param name
     * @return a collection of Exercises
     */
    public Collection<Exercise> searchExercises(String name) {
        return null;
    }
    /**
     * get all Exercises with that creatorID and that contain that name
     * @param creatorId
     * @param name
     * @return a collection of Exercises
     */
    public Collection<Exercise> searchExercises(int creatorId, String name) {
        return null;
    }

    /**
     * add a new Post and return its ID
     * @param creatorId
     * @param caption
     * @return postID if successful, else -1
     */
    public int addPost(int creatorId, String caption) {
        return 0;
    }

    /**
     * gets the next n number of Posts that creators are subscribed by the specified user
     * @param userId
     * @return a collection of Posts
     */
    public Collection<Post> getPosts(int userId) {
        return null;
    }
    /**
     * gets the next n number of Posts that creators are subscribed by the specified user, starting with a specified postID
     * @param userId
     * @param lastPostId
     * @return a collection of Posts
     */
    public Collection<Post> getPosts(int userId, int lastPostId) {
        return null;
    }

    /**
     * gets all Users that contain that name and their pro-attribute is equivalent to the pro-parameter
     * @param name
     * @param pro
     * @return a collection of Users
     */
    public Collection<User> searchUsers(String name, boolean pro) {
        return null;
    }

    /**
     * get all Plans
     * @return a collection of Plans
     */
    public Collection<Plan> searchPlans() {
        return null;
    }
    /**
     * get all Plans with this creatorID
     * @param creatorID
     * @return a collection of Plans
     */
    public Collection<Plan> searchPlans(int creatorID) {
        return null;
    }
    /**
     * get all Plans that contain that name
     * @param name
     * @return a collection of Plans
     */
    public Collection<Plan> searchPlans(String name) {
        return null;
    }
    /**
     * get all Plans with that creatorID and that contain that name
     * @param creatorID
     * @param name
     * @return a collection of Plans
     */
    public Collection<Plan> searchPlans(int creatorID, String name) {
        return null;
    }

    /**
     * gets the number of likes of a post
     * @param postId
     * @return number of likes if successful, else -1
     */
    public int getNumberOfLikes(int postId) {
        return 0;
    }

    /**
     * returns if the specified User liked the specified Post
     * @param userId
     * @param postId
     * @return true if the Post is liked by that User, else if not
     */
    public boolean isLiked(int userId, int postId) {
        return false;
    }

    /**
     * gets the next n number of Comments of a Post
     * @param postID
     * @return a collection of Comments
     */
    public Collection<Comment> getComments(int postID) {
        return getComments(postID, 0);
    }

    /**
     * gets the next n number of Comments of a Post, starting at a specified Comment
     * @param postID
     * @param lastCommentId
     * @return a collection of Comments
     */
    public Collection<Comment> getComments(int postID, int lastCommentId) {
        return null;
    }

    /**
     * the Post is liked by the User
     * @param userId
     * @param postId
     * @return true if successful, false if failed
     */
    public boolean like(int userId, int postId) {
        return false;
    }

    /**
     * the Post is commented by the User
     * @param userId
     * @param postId
     * @param text
     * @return true if successful, false if failed
     */
    public boolean addComment(int userId, int postId, String text) {
        return false;
    }

    /**
     * the User follows or unfollows the other User, depends on the follow-parameter
     * @param followerId
     * @param followsId
     * @param follow
     * @return the follow-state after change
     */
    public boolean setUserFollow(int followerId, int followsId, boolean follow) {
        return false;
    }

    /**
     * gets all Locations within a radius, starting at a specified point
     * @param coordinate
     * @param radius
     * @param types
     * @return a collection of Locations
     */
    public Collection<Location> getNearbyLocations(Coordinate coordinate, double radius, Collection<LocationType> types) {
        return null;
    }

    /**
     * gets all Plans the User has subscribed
     * @param userId
     * @return a collection Plans
     */
    public Collection<Plan> getSubscribedPlans(int userId) {
        return null;
    }

    /**
     * shows if a user has subscribed a plan
     * @param userId
     * @param planId
     * @return true if the plan is subscribed, false if not
     */
    public boolean isPlanSubscribed(int userId, int planId) {
        return false;
    }

    /**
     * the User subscribes or unsubscribes the Plan, depends on the subscribe-parameter
     * @param planId
     * @param userId
     * @param subscribe
     * @return the subscription-state after change
     */
    public boolean setPlanSubscription(int planId, int userId, boolean subscribe) {
        return false;
    }

    /**
     * gets the exercise with the id idExercise
     * @param idExercise
     * @return if found -> exercise else null
     */
    public Exercise getExercise(int idExercise){
        return null;
    }
    /**
     * gets all User that the user with the id idUser follows
     * @param idUser
     * @return a collection of users
     */
    public Collection<User> getUsersIFollow(int idUser){
        return null;
    }

    /**
     * gets all users that follow the user with the id idUser
     * @param idUser
     * @return a collection of user
     */
    public Collection<User> getUsersThatFollowMe(int idUser){
        return null;
    }

    /**
     * shows if a user follows another user
     * @param idFollower
     * @param idFollows
     * @return true if user follows the other user and false if not
     */
    public boolean isFollowing(int idFollower, int idFollows){
        return false;
    }
}
